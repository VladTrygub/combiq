package ru.atott.combiq.rest.utils;

import org.springframework.context.ApplicationContext;
import ru.atott.combiq.service.site.UrlResolver;
import ru.atott.combiq.service.site.UserContext;

public class RestContext {

    private UserContext uc;

    private UrlResolver urlResolver;

    private ApplicationContext applicationContext;

    public UserContext getUc() {
        return uc;
    }

    public void setUc(UserContext uc) {
        this.uc = uc;
    }

    public UrlResolver getUrlResolver() {
        return urlResolver;
    }

    public void setUrlResolver(UrlResolver urlResolver) {
        this.urlResolver = urlResolver;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
