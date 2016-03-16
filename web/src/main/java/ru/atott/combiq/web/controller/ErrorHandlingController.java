package ru.atott.combiq.web.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.atott.combiq.web.aop.CommonViewAttributesInjector;
import ru.atott.combiq.web.controller.question.QuestionNotFoundException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ErrorHandlingController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(ErrorHandlingController.class);

    @Autowired
    private CommonViewAttributesInjector commonViewAttributesInjector;

    @ExceptionHandler({
            NoHandlerFoundException.class,
            QuestionNotFoundException.class
    })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ModelAndView handle404(HttpServletRequest httpRequest, Exception exception) {
        logger.warn("Request {} raised 404", httpRequest.getRequestURL());

        return notFound();
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handle500(HttpServletRequest httpRequest, Exception exception) {
        logger.error("Request {} raised {}", httpRequest.getRequestURL(), exception);

        ModelAndView modelAndView = new ModelAndView("error/500");
        modelAndView.addObject("requestUrl", httpRequest.getRequestURL());
        modelAndView.addObject("stacktrace", ExceptionUtils.getStackTrace(exception));
        commonViewAttributesInjector.inject(httpRequest, modelAndView);
        return modelAndView;
    }
}
