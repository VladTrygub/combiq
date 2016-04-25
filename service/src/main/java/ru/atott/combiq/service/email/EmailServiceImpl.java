package ru.atott.combiq.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.atott.combiq.service.ServiceException;

import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    @Qualifier(value = "mailSender")
    private JavaMailSender mailSender;

    @Autowired
    private EmailContentProvider emailContentProvider;

    @Override
    public void sendEmail(EmailType type, Object model, List<String> to) {
        EmailContent content = emailContentProvider.getEmailContent(type, model, to);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            mimeMessage.setContent(content.getBody(), "text/html;charset=utf-8");
            helper.setTo(content.getTo().toArray(new String[content.getTo().size()]));
            helper.setSubject(content.getTitle());
            helper.setFrom("noreply@combiq.ru");

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
