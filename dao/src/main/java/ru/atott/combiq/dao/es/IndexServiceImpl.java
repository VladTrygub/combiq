package ru.atott.combiq.dao.es;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Client;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class IndexServiceImpl implements IndexService, ApplicationContextAware {

    @Autowired
    private Client client;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private NameVersionDomainResolver domainResolver;

    private ApplicationContext applicationContext;

    @Override
    public void updateMappingByEntities() {
        Repositories repositories = new Repositories(applicationContext);
        List<Class<?>> entities = StreamSupport.stream(repositories.spliterator(), false)
                .filter(repositoryClass -> repositoryClass.isAnnotationPresent(Document.class))
                .collect(Collectors.toList());
        updateMappingByEntities(entities);
    }

    @Override
    public void updateMappingByEntities(List<Class<?>> entityClasses) {
        entityClasses.stream()
                .filter(entityClass -> entityClass.isAnnotationPresent(Document.class))
                .forEach(entityClass -> {
                    elasticsearchTemplate.putMapping(entityClass);
                });
    }

    @Override
    public String createIndexByDomain(String domain) throws IOException {
        domainResolver.reset();

        Long version = domainResolver.getVersionOrDefault(domain, 0L) + 1;
        String indexName = domainResolver.resolveIndexName(domain, version);
        InputStream indexStream = this.getClass().getResourceAsStream("/ru/atott/combiq/dao/es/index/" + domain + ".json");
        String indexJson = IOUtils.toString(indexStream, "utf-8");
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.source(indexJson);
        client.admin().indices().create(request).actionGet();

        refreshIndexByIndexName(indexName);

        domainResolver.reset();
        return indexName;
    }

    @Override
    public String updateMappingByDomain(String domain) throws IOException {
        String indexName = domainResolver.resolveIndexName(domain);
        String json = getIndexMapping("/ru/atott/combiq/dao/es/index/" + domain + ".json");
        putMapping(client, indexName, json);
        return indexName;
    }

    @Override
    public void refreshIndexByDomain(String domain) {
        refreshIndexByIndexName(domainResolver.resolveIndexName(domain));
    }

    @Override
    public void refreshIndexByIndexName(String indexName) {
        client.admin().indices().prepareRefresh(indexName).get();
    }

    private static String getIndexMapping(String indexFile) throws IOException {
        InputStream indexStream = IndexServiceImpl.class.getResourceAsStream(indexFile);
        String indexJson = IOUtils.toString(indexStream, "utf-8");
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = (JsonObject) parser.parse(indexJson);
        JsonObject mappingsJsonObject = jsonObject.getAsJsonObject("mappings");
        return mappingsJsonObject.toString();
    }

    private static void putMapping(Client client, String index, String typeMappingsJson) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = (JsonObject) parser.parse(typeMappingsJson);
        jsonObject.entrySet().forEach(mapping -> {
            String type = mapping.getKey();
            String json = "{\"" + type+ "\":" + mapping.getValue().toString() + "}";
            putMapping(client, index, type, json);
        });
    }

    private static void putMapping(Client client, String index, String type, String typeMappingJson) {
        PutMappingRequest putMappingRequest = new PutMappingRequest(index);
        putMappingRequest.type(type);
        putMappingRequest.source(typeMappingJson);
        client.admin().indices().putMapping(putMappingRequest).actionGet();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
