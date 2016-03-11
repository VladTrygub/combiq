package ru.atott.combiq.web.controller;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.atott.combiq.service.bean.Question;
import ru.atott.combiq.service.search.SearchContext;
import ru.atott.combiq.service.search.SearchResponse;
import ru.atott.combiq.service.search.SearchService;
import ru.atott.combiq.web.bean.CountQuestionSearchBean;
import ru.atott.combiq.web.bean.PagingBean;
import ru.atott.combiq.web.bean.PagingBeanBuilder;
import ru.atott.combiq.web.utils.SearchQuestionContextFactory;
import ru.atott.combiq.web.view.SearchViewBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static ru.atott.combiq.web.utils.ViewUtils.getCountWord;

@Controller
public class SearchController extends BaseController {

    private PagingBeanBuilder pagingBeanBuilder = new PagingBeanBuilder();

    @Autowired
    private SearchService searchService;

    @Autowired
    private SearchQuestionContextFactory searchQuestionContextFactory;

    @RequestMapping(value = "/questions/search", method = RequestMethod.GET)
    public ModelAndView search(HttpServletRequest request,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(value = "q", defaultValue = "") String dsl) {
        page = getZeroBasedPage(page);
        SearchContext context = searchQuestionContextFactory.listByDsl(page, dsl);
        return getView(request, context, dsl);
    }

    @ResponseBody
    @RequestMapping(value = "/questions/search/count", method = RequestMethod.GET)
    public Object countSearch(HttpServletRequest request,
                              @RequestParam(value = "q", defaultValue = "") String dsl) {
        SearchContext context = searchQuestionContextFactory.listByDsl(0, dsl);
        long countQuestions = searchService.countQuestions(context);
        return new CountQuestionSearchBean(countQuestions);
    }

    @RequestMapping(value = "/questions", method = RequestMethod.GET)
    public ModelAndView questions(HttpServletRequest request,
                                  @RequestParam(defaultValue = "1") int page) {
        page = getZeroBasedPage(page);
        SearchContext context = searchQuestionContextFactory.list(page);
        String subTitle = "Вопросы для подготовки к собеседованию Java";
        return getView(request, context, null, subTitle, true);
    }

    @RequestMapping(value = "/questions/tagged/{tags}")
    public ModelAndView taggedQuestions(HttpServletRequest request,
                                        @RequestParam(defaultValue = "1") int page,
                                        @PathVariable("tags") String tags) {
        page = getZeroBasedPage(page);
        ArrayList<String> tagsList = Lists.newArrayList(StringUtils.split(tags, ','));
        SearchContext context = searchQuestionContextFactory.listByTags(page, tagsList);
        return getView(request, context, null);
    }

    @RequestMapping(value = "/questions/level/{level}")
    public ModelAndView level(HttpServletRequest request,
                              @RequestParam(defaultValue = "1") int page,
                              @PathVariable("level") String level) {
        page = getZeroBasedPage(page);
        SearchContext context = searchQuestionContextFactory.listByLevel(page, level);
        return getView(request, context, null);
    }

    @RequestMapping(value = "/questions/deleted", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('sa','contenter')")
    public ModelAndView deleted(HttpServletRequest request,
                                @RequestParam(defaultValue = "1") int page) {
        page = getZeroBasedPage(page);
        SearchContext context = searchQuestionContextFactory.listByDeleted(page, true);
        return getView(request, context, null);
    }

    @RequestMapping(value = "/questions/user/{userId}", method = RequestMethod.GET)
    public ModelAndView userQuestion(HttpServletRequest request,
                               @RequestParam(defaultValue = "1") int page,
                               @PathVariable("userId") String userId) {
        page = getZeroBasedPage(page);
        SearchContext context = searchQuestionContextFactory.listByUser(page, userId);
        return getView(request, context, null);
    }

    private ModelAndView getView(HttpServletRequest request, SearchContext context, String dsl,
                                 String subTitle, boolean questionsCatalog) {
        SearchResponse questionsResponse = searchService.searchQuestions(context);

        PagingBean paging = pagingBeanBuilder.build(questionsResponse.getQuestions(), questionsResponse.getQuestions().getNumber(), request);
        List<Question> questions = questionsResponse.getQuestions().getContent();
        dsl = StringUtils.defaultIfBlank(dsl, context.getDslQuery().toDsl());
        Long totalElements=questionsResponse.getQuestions().getTotalElements();

        SearchViewBuilder viewBuilder = new SearchViewBuilder();
        viewBuilder.setQuestions(questions);
        viewBuilder.setPaging(paging);
        viewBuilder.setDsl(dsl);
        viewBuilder.setPopularTags(questionsResponse.getPopularTags());
        viewBuilder.setSubTitle(subTitle);
        viewBuilder.setQuestionsCatalog(questionsCatalog);
        viewBuilder.setDslQuery(context.getDslQuery());
        viewBuilder.setQuestionsCount(totalElements);
        viewBuilder.setAnswerWord(getCountWord(totalElements));
        return viewBuilder.build();
    }

    private ModelAndView getView(HttpServletRequest request, SearchContext context, String dsl) {
        return getView(request, context, dsl, null, false);
    }
}
