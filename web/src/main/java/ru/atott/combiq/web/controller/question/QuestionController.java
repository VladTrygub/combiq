package ru.atott.combiq.web.controller.question;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.atott.combiq.service.UrlResolver;
import ru.atott.combiq.service.bean.Question;
import ru.atott.combiq.service.dsl.DslParser;
import ru.atott.combiq.service.question.QuestionService;
import ru.atott.combiq.service.question.TagService;
import ru.atott.combiq.service.search.comment.LatestComment;
import ru.atott.combiq.service.search.comment.LatestCommentSearchService;
import ru.atott.combiq.service.search.question.GetQuestionContext;
import ru.atott.combiq.service.search.question.GetQuestionResponse;
import ru.atott.combiq.service.search.question.SearchService;
import ru.atott.combiq.service.markdown.MarkdownService;
import ru.atott.combiq.service.user.UserStarsService;
import ru.atott.combiq.web.bean.QuestionBean;
import ru.atott.combiq.web.bean.SuccessBean;
import ru.atott.combiq.web.controller.BaseController;
import ru.atott.combiq.web.request.ContentRequest;
import ru.atott.combiq.web.request.EditCommentRequest;
import ru.atott.combiq.web.request.QuestionRequest;
import ru.atott.combiq.web.security.AuthService;
import ru.atott.combiq.web.utils.RequestUrlResolver;
import ru.atott.combiq.web.view.QuestionViewBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
public class QuestionController extends BaseController {

    @Autowired
    private AuthService authService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private LatestCommentSearchService latestCommentSearchService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TagService tagService;

    @Autowired
    private MarkdownService markdownService;

    @Autowired
    private UserStarsService userStarsService;

    @RequestMapping(value = "/questions/{questionId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object view(@PathVariable("questionId") String questionId) {
        Question question = questionService.getQuestion(questionId);

        if (question == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return QuestionBean.of(question);
    }

    @RequestMapping(value = {
            "/questions/{questionId}",
            "/questions/{questionId}/{humanUrlTitle}"
    })
    public Object view(@PathVariable("questionId") String questionId,
                       @PathVariable("humanUrlTitle") Optional<String> humanUrlTitle,
                       @RequestParam(value = "index", required = false) Integer index,
                       @RequestParam(value = "dsl", required = false) String dsl,
                       HttpServletRequest request) {
        UrlResolver urlResolver = new RequestUrlResolver(request);

        GetQuestionContext context = new GetQuestionContext();
        context.setUserId(authService.getUserId());
        context.setId(questionId);

        if (index != null) {
            context.setProposedIndexInDslResponse(index);
            context.setDsl(DslParser.parse(dsl));
        }

        GetQuestionResponse questionResponse = searchService.getQuestion(getUc(), context);

        RedirectView redirectView = redirectToCanonicalUrlIfNeed(questionId, humanUrlTitle.orElse(null), questionResponse, request);

        if (redirectView != null) {
            return redirectView;
        }

        List<Question> anotherQuestions = null;
        Question question = questionResponse.getQuestion();
        if (question == null) {
            question = questionService.getQuestion(questionId);
        } else {
            if (questionResponse.getQuestion().isLanding()) {
                anotherQuestions = searchService
                        .searchAnotherQuestions(getUc(), questionResponse.getQuestion())
                        .map(response -> response.getQuestions().getContent())
                        .orElse(null);
            }
        }

        if (question == null) {
            throw new QuestionNotFoundException();
        }

        List<LatestComment> questionsWithLatestComments = Collections.emptyList();
        if (CollectionUtils.isEmpty(question.getComments())) {
            questionsWithLatestComments = latestCommentSearchService.get3LatestComments();
        }
        QuestionViewBuilder viewBuilder = new QuestionViewBuilder();
        viewBuilder.setQuestion(question);
        viewBuilder.setPositionInDsl(questionResponse.getPositionInDsl());
        viewBuilder.setDsl(dsl);
        viewBuilder.setTags(tagService.getTags(question.getTags()));
        viewBuilder.setCanonicalUrl(urlResolver.externalize(urlResolver.getQuestionUrl(question)));
        viewBuilder.setAnotherQuestions(anotherQuestions);
        viewBuilder.setQuestionsWithLatestComments(questionsWithLatestComments);
        viewBuilder.setFavorite(userStarsService.isFavoriteQuestion(getUc(), questionId));
        return viewBuilder.build();
    }

    @RequestMapping(value = "/questions/{questionId}/content", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAnyRole('sa','contenter')")
    public Object postContent(@PathVariable("questionId") String questionId,
                              @RequestBody ContentRequest contentRequest) {
        questionService.saveQuestionBody(getUc(), questionId, contentRequest.getContent());
        return new SuccessBean();
    }

    @RequestMapping(value = "/questions/{questionId}/comment", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAnyRole('user')")
    public Object postComment(@PathVariable("questionId") String questionId,
                              @RequestBody EditCommentRequest request) {
        if (request.getCommentId() == null) {
            questionService.saveComment(getUc(), questionId, request.getContent());
        } else {
            questionService.updateComment(getUc(), questionId, request.getCommentId(), request.getContent());
        }
        return new SuccessBean();
    }

    private RedirectView redirectToCanonicalUrlIfNeed(String questionId,
                                                      String humanUrlTitle,
                                                      GetQuestionResponse searchResponse,
                                                      HttpServletRequest request) {
        Question question = searchResponse.getQuestion();

        if (question == null) {
            question = searchService.getQuestionByLegacyId(questionId);

            if (question == null) {
                return null;
            }

            UrlResolver urlResolver = new RequestUrlResolver(request);
            String questionUrl = urlResolver.getQuestionUrl(question, request.getQueryString());
            return movedPermanently(questionUrl);
        }

        if (StringUtils.isNotBlank(question.getHumanUrlTitle())
                && !Objects.equals(question.getHumanUrlTitle(), humanUrlTitle)) {
            UrlResolver urlResolver = new RequestUrlResolver(request);
            String questionUrl = urlResolver.getQuestionUrl(question, request.getQueryString());
            return movedPermanently(questionUrl);
        }

        return null;
    }

    @RequestMapping(value = "/questions",method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAnyRole('sa','contenter')")
    public Object saveQuestion(@Valid @RequestBody QuestionRequest questionRequest) {
        Question question;

        if (questionRequest.getId() != null) {
            question = questionService.getQuestion(questionRequest.getId());
        } else {
            question = new Question();
        }

        if (question == null) {
            return new SuccessBean(false, "Question " + questionRequest.getId() + " is not found.");
        }

        question.setTitle(questionRequest.getTitle());
        question.setBody(markdownService.toMarkdownContent(getUc(), questionRequest.getBody()));
        question.setLevel(questionRequest.getLevel());
        question.setTags(questionRequest.getTags() != null ? questionRequest.getTags() : Collections.emptyList());
        question.setLastModify(new Date());

        questionService.saveQuestion(getUc(), question);

        return QuestionBean.of(question);
    }

    @RequestMapping(value = "/questions/{questionId}/delete", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAnyRole('sa','contenter')")
    public SuccessBean deleteQuestion(@PathVariable("questionId") String questionId) {
        questionService.deleteQuestion(getUc(),questionId);
        return new SuccessBean(true);
    }

    @RequestMapping(value = "/questions/{questionId}/restore", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAnyRole('sa','contenter')")
    public SuccessBean restoreQuestion(@PathVariable("questionId") String questionId) {
        questionService.restoreQuestion(getUc(),questionId);
        return new SuccessBean(true);
    }

    @RequestMapping(value = "/questions/{questionId}/comment/{commentId}/delete", method = RequestMethod.POST)
    @ResponseBody
    public SuccessBean deleteComment(@PathVariable("questionId") String questionId,
                                @PathVariable("commentId") String commentId) {
        questionService.deleteComment(questionId, commentId, getUc());
        return new SuccessBean(true);
    }
}
