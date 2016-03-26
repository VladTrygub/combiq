package ru.atott.combiq.dao.es.index;

import java.io.IOException;
import java.util.List;

public interface IndexService {

    String createSiteIndex(String env) throws IOException;

    String updateSiteMapping(String env) throws IOException;

    String createQuestionIndex(String env) throws IOException;

    String updateQuestionMapping(String env) throws IOException;

    String createPersonalIndex(String env) throws IOException;

    String updatePersonalMapping(String env) throws IOException;

    String createSystemIndex(String env) throws IOException;

    void updateIndecesMappingByEntities();

    void updateIndecesMappingByEntities(List<Class<?>> entityClasses);
}
