package ru.atott.combiq.rest.doc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import ru.atott.combiq.rest.RestException;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class RestDocumentExampleResolver {

    private RestDocumentExampleResolver() { }

    private static Map<String, Object> examples = Collections.synchronizedMap(new HashMap<>());

    public static String getRefByLink(String link) {
        return StringUtils.substringBetween(link, "{@link", "}").trim();
    }

    public static String resolveExampleStringByLink(String link) {
        return resolveExampleStringByBean(resolveExampleBeanByLink(link));
    }

    public static String resolveExampleStringByRef(String ref) {
        return resolveExampleStringByBean(resolveExampleBeanByRef(ref));
    }

    public static String resolveExampleStringByBean(Object bean) {
        if (bean == null) {
            return StringUtils.EMPTY;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ"));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            throw new RestException(e.getMessage(), e);
        }
    }

    public static Object resolveExampleBeanByLink(String link) {
        String ref = getRefByLink(link);
        return resolveExampleBeanByRef(ref);
    }

    public static Object resolveExampleBeanByRef(String reference) {
        String className = StringUtils.substringBefore(reference, "#");
        String field = StringUtils.substringAfter(reference, "#");
        return resolveExampleBeanByRef(className, field);
    }

    public static Object resolveExampleBeanByRef(String className, String field) {
        if (StringUtils.isBlank(className)) {
            return null;
        }

        if (StringUtils.isBlank(field)) {
            return null;
        }

        String ref = className + "#" + field;

        if (examples.containsKey(ref)) {
            return examples.get(ref);
        }

        Object result = null;

        Class exampleClass = ClassResolver.resolveClass(className);

        if (exampleClass != null) {
            // Загрузить из поля экземпляра.

            try {
                Object bean = exampleClass.newInstance();

                result = Arrays.asList(bean.getClass().getDeclaredFields()).stream()
                        .filter(f -> f.getName().equals(field))
                        .findAny()
                        .map(f -> {
                            try {
                                return f.get(bean);
                            } catch (Exception e) {
                                throw new RestException(e.getMessage(), e);
                            }
                        })
                        .orElse(null);
            } catch (Exception e) {
                throw new RestException(e.getMessage(), e);
            }
        }

        examples.put(ref, result);

        return result;
    }
}
