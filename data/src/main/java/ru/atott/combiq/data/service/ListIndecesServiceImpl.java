package ru.atott.combiq.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.atott.combiq.dao.es.NameVersionDomainResolver;
import ru.atott.combiq.data.service.ListIndecesService;

import java.util.List;

@Service
public class ListIndecesServiceImpl implements ListIndecesService {
    @Autowired
    private NameVersionDomainResolver domainResolver;

    @Override
    public String[] list(String domain) {
        domainResolver.reset();
        List<String> domainIndeces = domainResolver.getDomainIndeces(domain);
        return domainIndeces.stream().toArray(String[]::new);
    }
}
