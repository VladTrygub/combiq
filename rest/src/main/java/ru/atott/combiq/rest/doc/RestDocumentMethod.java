package ru.atott.combiq.rest.doc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.FeatureDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RestDocumentMethod {

    private Method javaMethod;

    private Element methodJavaDoc;

    private String uri;

    private RequestMethod method;

    private String comment;

    private List<RestDocumentMethodParameter> parameters;

    private String acceptableRepresentation;

    private List<RestDocumentRepresentation> representations;

    private boolean deprecated;

    private String deprecationReason;

    public RestDocumentMethod(Method javaMethod, Element methodJavaDoc) {
        this.javaMethod = javaMethod;
        this.methodJavaDoc = methodJavaDoc;

        RequestMapping requestMapping = javaMethod.getAnnotation(RequestMapping.class);

        uri = requestMapping.value()[0];
        method = RequestMethod.GET;

        if (requestMapping.method() != null && requestMapping.method().length > 0) {
            method = requestMapping.method()[0];
        }

        this.comment = methodJavaDoc.element("comment").getTextTrim();
        this.parameters = Lists.newArrayList(javaMethod.getParameters()).stream()
                .filter(parameter -> parameter.getAnnotation(RequestBody.class) == null)
                .filter(parameter -> !parameter.getType().isAssignableFrom(HttpServletRequest.class))
                .filter(parameter -> !parameter.getType().isAssignableFrom(HttpServletResponse.class))
                .filter(parameter -> !parameter.getType().isAssignableFrom(BindingResult.class))
                .map(parameter -> new RestDocumentMethodParameter(parameter, methodJavaDoc))
                .collect(Collectors.toList());

        String acceptableRepresentationLink = JavaDocUtils.getAcceptableRepresentationLink(methodJavaDoc);
        if (StringUtils.isNotBlank(acceptableRepresentationLink)) {
            this.acceptableRepresentation = JavaDocUtils.getRepresentationByLink(acceptableRepresentationLink);
            if (StringUtils.isBlank(acceptableRepresentation)) {
                acceptableRepresentation = null;
            }
        }

        Set<String> representations = JavaDocUtils.getRepresentations(methodJavaDoc);
        this.representations = representations.stream()
                .map(representation -> new RestDocumentRepresentation(representation, methodJavaDoc))
                .sorted((a, b) -> a.getCode().compareTo(b.getCode()))
                .collect(Collectors.toList());

        Map<String, String> fieldsBeanClassNames = JavaDocUtils.getFieldsBeanClassNames(methodJavaDoc);
        this.parameters.stream()
                .forEach(parameter -> {
                    if (fieldsBeanClassNames.containsKey(parameter.getName())) {
                        String className = fieldsBeanClassNames.get(parameter.getName());
                        try {
                            PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(Class.forName(className));
                            List<String> fields = Arrays.asList(propertyDescriptors).stream()
                                    .filter(propertyDescriptor -> {
                                        if ("class".equals(propertyDescriptor.getName())) {
                                            return false;
                                        } else if (propertyDescriptor.getReadMethod().getAnnotation(JsonIgnore.class) != null) {
                                            return false;
                                        } else {
                                            return true;
                                        }
                                    })
                                    .map(FeatureDescriptor::getName)
                                    .collect(Collectors.toList());
                            parameter.setFieldsAvailableValues(fields);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });

        this.deprecated = javaMethod.isAnnotationPresent(Deprecated.class);
        this.deprecationReason = JavaDocUtils.getDeprecationReason(methodJavaDoc);
    }

    public Method getJavaMethod() {
        return javaMethod;
    }

    public String getUri() {
        return uri;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public String getQuailifier() {
        return getMethod() + " " + getUri();
    }

    public Element getMethodJavaDoc() {
        return methodJavaDoc;
    }

    public String getComment() {
        return comment;
    }

    public List<RestDocumentMethodParameter> getParameters() {
        return parameters;
    }

    public String getAcceptableRepresentation() {
        return acceptableRepresentation;
    }

    public List<RestDocumentRepresentation> getRepresentations() {
        return representations;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public String getDeprecationReason() {
        return deprecationReason;
    }
}
