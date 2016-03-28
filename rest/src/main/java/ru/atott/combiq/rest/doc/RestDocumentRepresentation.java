package ru.atott.combiq.rest.doc;

import org.dom4j.Element;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class RestDocumentRepresentation {
    private String code;
    private String description;
    private List<RestDocumentResponseExample> examples;
    private List<String> exampleClassNames;

    public RestDocumentRepresentation(String representation, Element methodJavaDoc) {
        this.code = StringUtils.split(representation, ".")[1];
        this.description = JavaDocUtils.getRepresentationDescription(representation, methodJavaDoc);
        this.examples = JavaDocUtils
                .getRepresentationExamples(representation, methodJavaDoc).stream()
                .filter(example -> example != null)
                .collect(Collectors.toList());
        this.exampleClassNames = JavaDocUtils.getRepresentationExamplesClasses(representation, methodJavaDoc);
    }

    public RestDocumentRepresentation(String code, String description, List<RestDocumentResponseExample> examples) {
        this.code = code;
        this.description = description;
        this.examples = examples;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public List<RestDocumentResponseExample> getExamples() {
        return examples;
    }

    public List<String> getExampleClassNames() {
        return exampleClassNames;
    }
}
