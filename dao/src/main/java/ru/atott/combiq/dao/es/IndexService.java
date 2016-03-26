package ru.atott.combiq.dao.es;

import ru.atott.combiq.dao.Domains;

import java.io.IOException;
import java.util.List;

public interface IndexService {

    default String createSiteIndex() throws IOException {
        return createIndexByDomain(Domains.site);
    }

    default String createQuestionIndex() throws IOException {
        return createIndexByDomain(Domains.question);
    }

    default String createPersonalIndex() throws IOException {
        return createIndexByDomain(Domains.personal);
    }

    default String createSystemIndex() throws IOException {
        return createIndexByDomain(Domains.system);
    }

    default String updateSiteMapping() throws IOException {
        return updateMappingByDomain(Domains.site);
    }

    default String updateQuestionMapping() throws IOException {
        return updateMappingByDomain(Domains.question);
    }

    default String updatePersonalMapping() throws IOException {
        return updateMappingByDomain(Domains.personal);
    }

    void updateMappingByEntities();

    void updateMappingByEntities(List<Class<?>> entityClasses);

    String  createIndexByDomain(String domain) throws IOException;

    String  updateMappingByDomain(String domain) throws IOException;

    void refreshIndexByDomain(String domain);

    void refreshIndexByIndexName(String indexName);
}
