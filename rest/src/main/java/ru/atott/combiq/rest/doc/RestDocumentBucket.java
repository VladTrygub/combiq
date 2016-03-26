package ru.atott.combiq.rest.doc;

import java.util.List;

public class RestDocumentBucket {

    private List<RestDocumentService> services;

    public RestDocumentBucket(List<RestDocumentService> services) {
        this.services = services;
    }

    public List<RestDocumentService> getServices() {
        return services;
    }
}
