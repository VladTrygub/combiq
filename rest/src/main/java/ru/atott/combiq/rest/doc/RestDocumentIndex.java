package ru.atott.combiq.rest.doc;

import com.google.common.collect.Lists;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.atott.combiq.rest.RestException;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RestDocumentIndex {

    private List<RestDocumentBucket> buckets;

    public RestDocumentIndex(List<List<Class>> controllerBuckets) {
        try {
            InputStream javaDocStream = RestDocumentIndex.class.getResourceAsStream("/rest-javadoc.xml");
            SAXReader reader = new SAXReader();
            Document javaDocDocument = reader.read(javaDocStream);
            buckets = controllerBuckets.stream()
                    .map(controller -> getBucket(controller, javaDocDocument))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RestException(e.getMessage(), e);
        }
    }

    public RestDocumentIndex(List<List<Class>> controllerBuckets, Document javaDocDocument) {
        buckets = controllerBuckets.stream()
                .map(controller -> getBucket(controller, javaDocDocument))
                .collect(Collectors.toList());
    }

    public List<RestDocumentBucket> getBuckets() {
        return buckets;
    }

    private RestDocumentBucket getBucket(List<Class> controllers, Document javaDocDocument) {
        List<RestDocumentMethod> restMethods = controllers.stream()
                .flatMap(controller -> {
                    Method[] methods = controller.getMethods();
                    return Lists.newArrayList(methods).stream()
                            .filter(method -> method.getAnnotation(RequestMapping.class) != null)
                            .map(javaMethod ->
                                    new RestDocumentMethod(javaMethod, JavaDocUtils.getMethodDocElement(javaDocDocument, javaMethod)));
                })
                .collect(Collectors.toList());

        Set<String> serviceUris = restMethods.stream()
                .map(RestDocumentMethod::getUri)
                .collect(Collectors.toSet());

        List<RestDocumentService> services = serviceUris.stream()
                .map(serviceUri -> {
                    List<RestDocumentMethod> serviceRestMethods = restMethods.stream()
                            .filter(restMethod -> restMethod.getUri().equals(serviceUri))
                            .collect(Collectors.toList());

                    return new RestDocumentService(serviceRestMethods, serviceUri);
                })
                .sorted((a, b) -> a.getUri().compareTo(b.getUri()))
                .collect(Collectors.toList());

        return new RestDocumentBucket(services);
    }
}
