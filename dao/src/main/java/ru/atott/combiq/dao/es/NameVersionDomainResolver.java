package ru.atott.combiq.dao.es;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.atott.combiq.dao.DaoException;
import ru.atott.combiq.dao.Domains;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component("domainResolver")
public class NameVersionDomainResolver {

    private String delimiter = "_";

    private Map<String, Long> domainVersions;

    private List<String> rawIndeces;

    @Value("${es.index.prefix}")
    private String prefix;

    @Autowired
    private Client client;

    public String getDelimiter() {
        return delimiter;
    }

    public String getPrefix() {
        return prefix;
    }

    public List<String> getDomainIndeces(String domain) {
        if (rawIndeces == null) {
            updateDomainVersions();
        }
        return rawIndeces.stream()
                .filter(indexName -> Objects.equals(prefix + domain, NameVersionUtils.getName(indexName, delimiter)))
                .collect(Collectors.toList());
    }

    public String resolveIndexName(String domain, long version) {
        return prefix + domain + delimiter + version;
    }

    public String resolveIndexName(String domain) {
        return resolveIndexName(domain, getVersion(domain));
    }

    public String resolvePersonalIndex() {
        return resolveIndexName(Domains.personal);
    }

    public String resolveQuestionIndex() {
        return resolveIndexName(Domains.question);
    }

    public String resolveSiteIndex() {
        return resolveIndexName(Domains.site);
    }

    public String resolveSystemIndex() {
        return resolveIndexName(Domains.system);
    }

    public boolean canBeResolved(String domain) {
        if (domainVersions == null) {
            updateDomainVersions();
        }
        return domainVersions.containsKey(prefix + domain);
    }

    public Long getVersionOrDefault(String domain, Long defaultVersion) {
        if (canBeResolved(domain)) {
            return getVersion(domain);
        }
        return defaultVersion;
    }

    public Long getVersion(String domain) {
        if (domainVersions == null) {
            updateDomainVersions();
        }
        if (!domainVersions.containsKey(prefix + domain)) {
            return 1L;
        }
        return domainVersions.get(prefix + domain);
    }

    public void reset() {
        updateDomainVersions();
    }

    private void updateDomainVersions() {
        rawIndeces = Lists.newArrayList(client
                .admin().cluster().prepareState().execute().actionGet()
                .getState().getMetaData().concreteAllIndices());

        List<String> indexNames = rawIndeces.stream()
                .filter(indexName -> NameVersionUtils.getName(indexName, delimiter) != null)
                .collect(Collectors.toList());

        Set<String> domains = indexNames.stream()
                .map(indexName -> NameVersionUtils.getName(indexName, delimiter))
                .collect(Collectors.toSet());

        domainVersions = domains.stream()
                .map(domain -> {
                    List<Long> versions = indexNames.stream()
                            .filter(indexName -> Objects.equals(NameVersionUtils.getName(indexName, delimiter), domain))
                            .map(indexName -> NameVersionUtils.getVersion(indexName, delimiter))
                            .collect(Collectors.toList());
                    versions.sort(Long::compare);
                    return domain + delimiter + versions.get(versions.size() - 1);
                })
                .collect(
                        Collectors.toMap(
                                indexName -> NameVersionUtils.getName(indexName, delimiter),
                                indexName -> NameVersionUtils.getVersion(indexName, delimiter)));
    }
}
