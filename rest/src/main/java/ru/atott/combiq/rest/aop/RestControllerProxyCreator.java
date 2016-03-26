package ru.atott.combiq.rest.aop;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.bind.annotation.RestController;

public class RestControllerProxyCreator extends AbstractAutoProxyCreator {

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        if (FactoryBean.class.isAssignableFrom(beanClass)) {
            return DO_NOT_PROXY;
        }

        if (beanClass.isAnnotationPresent(RestController.class)) {
            return PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
        }

        return DO_NOT_PROXY;
    }
}
