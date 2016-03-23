package ru.atott.combiq.rest.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.atott.combiq.rest.bean.ErrorBean;

public class RestErrorAnswerInterceptor implements MethodInterceptor {

    private static Logger logger = LoggerFactory.getLogger(RestErrorAnswerInterceptor.class);

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {
            return methodInvocation.proceed();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            ErrorBean errorBean = new ErrorBean(e.getClass().getName() + ": " + e.getMessage());
            return new ResponseEntity<>(errorBean, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
