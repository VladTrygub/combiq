package ru.atott.combiq.rest.doc;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class JavaDocUtils {
    public static Element getParamDocElement(Element methodJavaDoc, String fieldName) {
        return (Element) methodJavaDoc.elements().stream()
                .filter(e -> ((Element) e).getName().equals("tag") && ((Element) e).attribute("name").getValue().equals("@param"))
                .filter(e -> {
                    Element param = (Element) e;
                    String text = param.attribute("text").getValue();
                    if (text.startsWith(fieldName)) {
                        return true;
                    }
                    return false;
                })
                .findFirst()
                .orElse(null);
    }

    public static Element getMethodDocElement(Document document, Method method) {
        Element packageElement = (Element) document.getRootElement().elements().stream()
                .filter(pElement -> {
                    String packageName = ((Element) pElement).attribute("name").getValue();
                    return method.getDeclaringClass().getPackage().getName().equals(packageName);
                })
                .findFirst()
                .get();

        Element classElement = (Element) packageElement.elements().stream()
                .filter(cElement -> {
                    String className = ((Element) cElement).attribute("name").getValue();
                    return method.getDeclaringClass().getSimpleName().equals(className);
                })
                .findFirst()
                .get();

        Element methodElement = (Element) classElement.elements().stream()
                .filter(mElement -> {
                    return ((Element) mElement).getName().equals("method");
                })
                .filter(mElement -> {
                    String methodName = ((Element) mElement).attribute("name").getValue();
                    return method.getName().equals(methodName);
                })
                .findFirst()
                .get();

        return methodElement;
    }

    public static String getAcceptableRepresentationLink(Element methodJavaDoc) {
        return (String) methodJavaDoc.elements().stream()
                .filter(e -> {
                    Element tag = ((Element) e);
                    return tag.getName().equals("tag")
                            && Objects.equals(tag.attribute("name").getValue(), "@request.body.example");
                })
                .map(e -> ((Element) e).attribute("text").getValue())
                .findFirst()
                .orElse(null);
    }

    public static String getDeprecationReason(Element methodJavaDoc) {
        return (String) methodJavaDoc.elements().stream()
                .filter(e -> {
                    Element tag = ((Element) e);
                    return tag.getName().equals("tag")
                            && Objects.equals(tag.attribute("name").getValue(), "@deprecated");
                })
                .map(e -> ((Element) e).attribute("text").getValue())
                .findFirst()
                .orElse(null);
    }

    public static String getRepresentationByLink(String link) {
        try {
            return RestDocumentExampleResolver.resolveExampleStringByLink(link);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static Set<String> getRepresentations(Element methodJavaDoc) {
        List<String> tags = getTags(methodJavaDoc);
        return tags.stream()
                .map(tag -> {
                    String result = StringUtils.trimLeadingCharacter(tag, '@');
                    String[] splits = result.split("\\.");
                    return splits[0] + "." + splits[1];
                })
                .collect(Collectors.toSet());
    }

    public static String getRepresentationDescription(String representation, Element methodJavaDoc) {
        String tagName = "@" + representation + ".doc";
        Element tag = getTag(methodJavaDoc, tagName);
        if (tag != null) {
            return tag.attribute("text").getValue();
        } else {
            return null;
        }
    }

    public static List<RestDocumentResponseExample> getRepresentationExamples(String representation, Element methodJavaDoc) {
        try {
            String tagName = "@" + representation + ".example";
            List<Element> tags = getTags(methodJavaDoc, tagName);
            return tags.stream()
                    .map(tag -> {
                        String link = tag.attribute("text").getValue();
                        String reference = RestDocumentExampleResolver.getRefByLink(link);
                        String example = getRepresentationByLink(link);

                        if (example == null) {
                            return null;
                        }

                        RestDocumentResponseExample result = new RestDocumentResponseExample();
                        result.setRef(reference);
                        result.setValue(example);
                        return result;
                    })
                    .filter(v -> v != null)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public static List<String> getRepresentationExamplesClasses(String representation, Element methodJavaDoc) {
        try {
            String tagName = "@" + representation + ".example";
            List<Element> tags = getTags(methodJavaDoc, tagName);
            return tags.stream()
                    .map(tag -> {
                        String className = org.apache.commons.lang3.StringUtils.substringBefore(tag.attribute("text").getValue(), "#");
                        return org.apache.commons.lang3.StringUtils.replace(className, "{@link ", "").trim();
                    })
                    .distinct()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.singletonList(e.getMessage());
        }
    }

    public static Map<String, String> getFieldsBeanClassNames(Element methodJavaDoc) {
        try {
            List<String> tags = getAllTags(methodJavaDoc).stream()
                    .filter(tag -> tag.startsWith("@fields."))
                    .collect(Collectors.toList());

            Map<String, String> result = new HashMap<>();

            tags
                .forEach(tag -> {
                    String parameterName = org.apache.commons.lang3.StringUtils.substringBetween(tag, "fields.", ".beanClassName");
                    String link = getTag(methodJavaDoc, tag).attribute("text").getValue();
                    String className = org.apache.commons.lang3.StringUtils.substringBetween(link, "{@link", "}").trim();
                    if (!StringUtils.isEmpty(parameterName) && !StringUtils.isEmpty(className)) {
                        result.put(parameterName, className);
                    }
                });

            return result;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private static List<Element> getTags(Element doc, String tagName) {
        return (List) doc.elements().stream()
                .filter(e -> ((Element) e).getName().equals("tag") && ((Element) e).attribute("name").getValue().equals(tagName))
                .collect(Collectors.toList());
    }

    private static Element getTag(Element doc, String tagName) {
        return (Element) doc.elements().stream()
                .filter(e -> ((Element) e).getName().equals("tag") && ((Element) e).attribute("name").getValue().equals(tagName))
                .findFirst()
                .orElse(null);
    }

    private static List<String> getTags(Element methodJavaDoc) {
        return (List<String>) methodJavaDoc.elements().stream()
                .filter(e -> ((Element) e).getName().equals("tag")
                        && StringUtils.startsWithIgnoreCase(((Element) e).attribute("name").getValue(), "@response."))
                .map(e -> ((Element) e).attribute("name").getValue())
                .collect(Collectors.toList());
    }

    private static List<String> getAllTags(Element methodJavaDoc) {
        return (List<String>) methodJavaDoc.elements().stream()
                .filter(e -> ((Element) e).getName().equals("tag")
                        && StringUtils.startsWithIgnoreCase(((Element) e).attribute("name").getValue(), "@"))
                .map(e -> ((Element) e).attribute("name").getValue())
                .collect(Collectors.toList());
    }

    private static Class getExampleClass(String name) throws ClassNotFoundException {
        if (name.indexOf('.') == -1) {
            String[] searchPackages = { "ru.sportmaster.rest.v1.category" };
            for (int i = 0; i < searchPackages.length; i++) {
                try {
                    return Class.forName(searchPackages[i] + "." + name);
                } catch (ClassNotFoundException e){
                    // not in this package, try another
                }
            }
        } else {
            return Class.forName(name);
        }
        return null;
    }
}
