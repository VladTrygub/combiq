package ru.atott.combiq.rest.doc;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class RestDocumentMethodParameter {
    private Parameter javaParameter;
    private String name;
    private String type;
    private String defaultValue;
    private String description;
    private boolean deprecated;
    private List<String> fieldsAvailableValues;

    public RestDocumentMethodParameter(Parameter javaParameter, Element methodJavaDoc) {
        this.javaParameter = javaParameter;
        this.name = javaParameter.getName();

        RequestParam requestParam = javaParameter.getAnnotation(RequestParam.class);
        if (requestParam != null && requestParam.value() != null) {
            name = requestParam.value();
            defaultValue = requestParam.defaultValue();
            if (defaultValue != null && requestParam.defaultValue().charAt(0) == '\n') {
                defaultValue = null;
            }
        }

        PathVariable pathVariable = javaParameter.getAnnotation(PathVariable.class);
        if (pathVariable != null) {
            name = pathVariable.value();
        }

        type = javaParameter.getType().getSimpleName();

        if (type.equals("Optional")) {
            type = ((Class)((ParameterizedType) javaParameter.getParameterizedType()).getActualTypeArguments()[0]).getSimpleName();
        }

        Element paramJavaDoc = JavaDocUtils.getParamDocElement(methodJavaDoc, name);
        if (paramJavaDoc != null) {
            description = paramJavaDoc.attribute("text").getValue();
            description = description.substring(name.length()).trim();
        }

        if (javaParameter.getAnnotation(Deprecated.class) != null) {
            deprecated = true;
        }
    }

    public List<String> getFieldsAvailableValues() {
        return fieldsAvailableValues;
    }

    public void setFieldsAvailableValues(List<String> fieldsAvailableValues) {
        this.fieldsAvailableValues = fieldsAvailableValues;
    }

    public Parameter getJavaParameter() {
        return javaParameter;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDeprecated() {
        return deprecated;
    }
}
