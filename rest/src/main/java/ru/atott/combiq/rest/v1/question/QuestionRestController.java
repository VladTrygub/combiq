package ru.atott.combiq.rest.v1.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.atott.combiq.rest.mapper.QuestionBeanMapper;
import ru.atott.combiq.rest.mapper.QuestionSearchBeanMapper;
import ru.atott.combiq.rest.request.QuestionRequest;
import ru.atott.combiq.rest.utils.RestContext;
import ru.atott.combiq.rest.v1.BaseRestController;
import ru.atott.combiq.service.bean.Question;
import ru.atott.combiq.service.markdown.MarkdownService;
import ru.atott.combiq.service.question.QuestionService;
import ru.atott.combiq.service.search.question.SearchContext;
import ru.atott.combiq.service.search.question.SearchContextFactory;
import ru.atott.combiq.service.search.question.SearchResponse;
import ru.atott.combiq.service.search.question.SearchService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@RestController
public class QuestionRestController extends BaseRestController {

    @Autowired
    private SearchContextFactory searchContextFactory;

    @Autowired
    private SearchService searchService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private MarkdownService markdownService;

    @RequestMapping(value = "/rest/v1/question", method = RequestMethod.GET)
    @ResponseBody
    public Object search(
            @RequestParam(value = "dsl", required = false) Optional<String> dsl,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize) {

        SearchContext searchContext = searchContextFactory.listByDsl(page, pageSize, dsl.orElse(""));
        SearchResponse searchResponse = searchService.searchQuestions(searchContext);
        QuestionSearchBeanMapper questionSearchBeanMapper = new QuestionSearchBeanMapper();
        return questionSearchBeanMapper.map(getContext(), searchResponse);
    }

    @RequestMapping(value = "/rest/v1/question/{questionId}", method = RequestMethod.GET)
    public Object get(
            @PathVariable("questionId") String questionId) {

        Question question = questionService.getQuestion(questionId);

        if (question == null) {
            return responseNotFound();
        }

        QuestionBeanMapper questionMapper = new QuestionBeanMapper();
        return questionMapper.map(getContext(), question);
    }

    @RequestMapping(value = "/rest/v1/question", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAnyRole('sa','contenter')")
    public Object createQuestion(
            @Valid @RequestBody QuestionRequest request) {

        RestContext context = getContext();

        Question question = new Question();
        updateAndSaveQuestion(context, question, request);

        questionService.saveQuestion(context.getUc(), question);

        QuestionBeanMapper questionBeanMapper = new QuestionBeanMapper();
        return questionBeanMapper.map(context, question);
    }

    @RequestMapping(value = "/rest/v1/question/{questionId}", method = RequestMethod.PUT)
    @ResponseBody
    @PreAuthorize("hasAnyRole('sa','contenter')")
    public Object saveQuestion(
            @PathVariable("questionId") String questionId,
            @Valid @RequestBody QuestionRequest request) {

        Question question = questionService.getQuestion(questionId);

        if (question == null) {
            return responseNotFound();
        }

        RestContext context = getContext();
        updateAndSaveQuestion(context, question, request);

        QuestionBeanMapper questionBeanMapper = new QuestionBeanMapper();
        return questionBeanMapper.map(context, question);
    }

    private void updateAndSaveQuestion(RestContext context, Question question, QuestionRequest request) {
        question.setTitle(request.getTitle());
        question.setBody(markdownService.toMarkdownContent(context.getUc(), request.getBody()));
        question.setLevel(request.getLevel());
        question.setTags(request.getTags() != null ? request.getTags() : Collections.emptyList());
        question.setLastModify(new Date());

        questionService.saveQuestion(context.getUc(), question);
    }
}
