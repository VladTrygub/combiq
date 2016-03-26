package ru.atott.combiq.data.service;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.atott.combiq.dao.es.NameVersionDomainResolver;
import ru.atott.combiq.data.service.DeleteIndecesService;

@Service
public class DeleteIndecesServiceImpl implements DeleteIndecesService {
    @Autowired
    private Client client;
    @Autowired
    private NameVersionDomainResolver domainResolver;

    @Override
    public void deleteIndex(String domain, Long version) {
        String indexName = domainResolver.resolveIndexName(domain, version);
        client.admin()
                .indices()
                .delete(new DeleteIndexRequest(indexName))
                .actionGet();
    }
}
