package ru.atott.combiq.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ru.atott.combiq.dao.es.IndexService;

@Component
public class CombiqApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = LoggerFactory.getLogger(CombiqApplicationListener.class);

    private boolean initialized;

    @Autowired
    private IndexService indexService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (!initialized) {
            try {
                indexService.updateQuestionMapping();
                indexService.updatePersonalMapping();
                indexService.updateSiteMapping();
                indexService.updateMappingByEntities();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            initialized = true;
        }
    }
}
