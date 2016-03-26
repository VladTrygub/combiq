package ru.atott.combiq.rest.doc;

import java.util.List;

public class RestDocumentService {
    private List<RestDocumentMethod> methods;
    private String uri;

    public RestDocumentService(List<RestDocumentMethod> methods, String uri) {
        this.methods = methods;
        this.uri = uri;
    }

    public List<RestDocumentMethod> getMethods() {
        return methods;
    }

    public String getUri() {
        return uri;
    }
}
