package ru.atott.combiq.rest.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.atott.combiq.rest.bean.MessageBean;
import ru.atott.combiq.rest.utils.RestContext;
import ru.atott.combiq.service.site.RequestUrlResolver;
import ru.atott.combiq.service.site.UserContextProvider;
import ru.atott.combiq.service.util.ApplicationContextHolder;

import javax.servlet.http.HttpServletRequest;

public class BaseRestController {

    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private UserContextProvider userContextProvider;

    protected RestContext getContext() {
        RestContext restContext = new RestContext();
        restContext.setApplicationContext(ApplicationContextHolder.getApplicationContext());
        restContext.setUc(userContextProvider.getUserContext());
        restContext.setUrlResolver(new RequestUrlResolver(httpRequest));
        return restContext;
    }

    protected Object responseNotFound() {
        return response(HttpStatus.NOT_FOUND);
    }

    protected Object responseOk() {
        return response(HttpStatus.OK);
    }

    protected Object response(HttpStatus httpStatus) {
        return response(new MessageBean(httpStatus.getReasonPhrase()), httpStatus);
    }

    protected Object response(Object answer, HttpStatus httpStatus) {
        return new ResponseEntity<>(answer, httpStatus);
    }
}
