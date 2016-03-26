package ru.atott.combiq.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.atott.combiq.service.site.UrlResolver;
import ru.atott.combiq.service.bean.Questionnaire;
import ru.atott.combiq.service.bean.QuestionnaireHead;
import ru.atott.combiq.service.question.QuestionnaireService;
import ru.atott.combiq.service.site.UserContext;
import ru.atott.combiq.web.bean.SuccessBean;
import ru.atott.combiq.web.request.ContentRequest;
import ru.atott.combiq.service.site.RequestUrlResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class QuestionnaireController extends BaseController {

    @Autowired
    private QuestionnaireService questionnaireService;

    @RequestMapping(value = "/questionnaire/{questionnaireId}")
    public Object view(
            @PathVariable("questionnaireId") String questionnaireId,
            HttpServletRequest request) {
        Questionnaire questionnaire = questionnaireService.getQuestionnaire(getUc(), questionnaireId);

        if (questionnaire == null) {
            QuestionnaireHead questionnaireByLegacy = questionnaireService.getQuestionnaireByLegacyId(questionnaireId);
            if (questionnaireByLegacy != null) {
                UrlResolver urlResolver = new RequestUrlResolver(request);
                return movedPermanently(urlResolver.getQuestionnaireUrl(questionnaireByLegacy, request.getQueryString()));
            }
        }

        ModelAndView modelAndView = new ModelAndView("questionnaire");
        modelAndView.addObject("questionnaire", questionnaire);
        return modelAndView;
    }

    @RequestMapping(value = "/questionnaire/{questionnaireId}/{title}.pdf", method = RequestMethod.GET)
    public void view(@PathVariable("questionnaireId") String questionnaireId,
                     @PathVariable("title") String title,
                     HttpServletResponse response) throws Exception {
        Questionnaire questionnaire = questionnaireService.getQuestionnaire(getUc(), questionnaireId);

        response.setContentType("application/pdf");

        questionnaireService.exportQuestionnareToPdf(questionnaire, response.getOutputStream());
        response.flushBuffer();
    }

    @RequestMapping(value = "/questionnaire/{questionnaireId}/title", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAnyRole('sa','contenter')")
    public Object updateTitle(@PathVariable("questionnaireId") String questionnaireId,
                              @RequestBody ContentRequest title) {
        UserContext uc = getUc();
        questionnaireService.updateQuestionnaireTitle(uc, questionnaireId, title.getContent());
        return new SuccessBean();
    }
}
