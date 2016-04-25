package ru.atott.combiq.service.email;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Component;
import ru.atott.combiq.service.ServiceException;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class EmailContentProvider {

    private volatile Map<EmailType, EmailDescriptor> emailDescriptors;

    private ConcurrentHashMap<EmailType, String> emailTemplates = new ConcurrentHashMap<>();

    public EmailContent getEmailContent(EmailType type, Object model, List<String> to) {
        EmailDescriptor descriptor = getEmailDescriptors().get(type);
        Validate.notNull(descriptor);

        String templateString = getEmailTemplate(type);
        String body = processTemplate(templateString, model);

        EmailContent emailContent = new EmailContent();
        emailContent.setBody(body);
        emailContent.setHtml(descriptor.isHtml());
        emailContent.setTitle(descriptor.getTitle());
        emailContent.setTo(new ArrayList<>());

        if (!CollectionUtils.isEmpty(descriptor.getTo())) {
            emailContent.getTo().addAll(descriptor.getTo());
        }

        if (!CollectionUtils.isEmpty(to)) {
            emailContent.getTo().addAll(to);
        }

        return emailContent;
    }

    private String processTemplate(String templateString, Object model) {
        try {
            Configuration configuration = new Configuration();
            configuration.setObjectWrapper(new DefaultObjectWrapper());
            try (StringReader reader = new StringReader(templateString)) {
                Template template = new Template("templateName", reader, configuration);

                try (Writer writer = new StringWriter()) {
                    template.process(Collections.singletonMap("model", model), writer);

                    return writer.toString();
                }
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private String getEmailTemplate(EmailType type) {
        return emailTemplates.computeIfAbsent(type, emailType -> {
            try {
                try (InputStream stream = this.getClass().getResourceAsStream("/email/" + type.name() + ".ftl")) {
                    return IOUtils.toString(stream, "utf-8");
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        });
    }

    private Map<EmailType, EmailDescriptor> getEmailDescriptors() {
        try {
            if (emailDescriptors == null) {
                synchronized (this) {
                    if (emailDescriptors == null) {
                        try (InputStream stream = this.getClass().getResourceAsStream("/email/email.json")) {
                            String json = IOUtils.toString(stream, "utf-8");
                            Type typeToken = (new TypeToken<List<EmailDescriptor>>() { }).getType();
                            List<EmailDescriptor> list = new Gson().fromJson(json, typeToken);
                            emailDescriptors = list.stream()
                                    .collect(Collectors.toMap(EmailDescriptor::getType, descriptor -> descriptor));
                        }
                    }
                }
            }
            return emailDescriptors;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private static class EmailDescriptor {

        private EmailType type;

        private String title;

        private boolean html;

        private List<String> to;

        public EmailType getType() {
            return type;
        }

        public void setType(EmailType type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isHtml() {
            return html;
        }

        public void setHtml(boolean html) {
            this.html = html;
        }

        public List<String> getTo() {
            return to;
        }

        public void setTo(List<String> to) {
            this.to = to;
        }
    }
}
