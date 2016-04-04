package ru.atott.combiq.rest.v1.question;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.atott.combiq.rest.bean.CommentBean;
import ru.atott.combiq.rest.bean.MessageBean;
import ru.atott.combiq.rest.mapper.CommentBeanMapper;
import ru.atott.combiq.rest.request.CommentRequest;
import ru.atott.combiq.rest.request.QuestionRequest;
import ru.atott.combiq.rest.v1.BaseRestController;
import ru.atott.combiq.service.question.QuestionService;

@RestController
public class CommentRestController extends BaseRestController {

    @Autowired
    private QuestionService questionService;

    /**
     * Create операция для комментария к вопросу
     *
     * @request.body.example
     *      {@link CommentRequest#EXAMPLE}
     *
     * @param questionId
     *      Id вопроса
     *
     * @response.200.doc
     *      В случае успеха возврашает созданный коментарий
     *
     */

    @RequestMapping(value = "/rest/v1/question/{questionId}/comment", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('user')")
    public Object createComment(@PathVariable("questionId") String questionId,
                              @RequestBody CommentRequest request) {
        questionService.saveComment(getContext().getUc(), questionId, request.getContent());
        CommentBeanMapper mapper = new CommentBeanMapper();
        return mapper.map(getContext(), questionService.getQuestion(questionId).getLastComment());
    }

    /**
     * Update операция для комментария к вопросу
     *
     * @request.body.example
     *      {@link CommentRequest#EXAMPLE}
     *
     * @param questionId
     *      Id вопроса
     *
     * @param commentId
     *      Id коментария
     *
     * @response.200.doc
     *      В случае успеха возврашает измененный коментарий
     *
     */

    @RequestMapping(value = "/rest/v1/question/{questionId}/comment/{commentId}", method = RequestMethod.PUT)
    @PreAuthorize("hasAnyRole('user')")
    public Object udpateComment(@PathVariable("questionId") String questionId,
                                @PathVariable("commentId") String commentId,
                                @RequestBody CommentRequest request) {
        questionService.updateComment(getContext().getUc(), questionId,
                commentId, request.getContent());
        return getComment(questionId, commentId);
    }


    /**
     * Delete операция для комментария к вопросу
     *
     * @param questionId
     *      Id вопроса
     *
     * @param commentId
     *      Id коментария
     *
     * @request.body.example
     *      {@link CommentRequest#EXAMPLE}
     *
     * @response.200.doc
     *      В случае успеха сообщение "Success"
     */

    @RequestMapping(value = "/rest/v1/question/{questionId}/comment/{commentId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyRole('user')")
    public Object deleteComment(@PathVariable("questionId") String questionId,
                                @PathVariable("commentId") String commentId) {
        questionService.deleteComment(getContext().getUc(), questionId, commentId);
        return new MessageBean("Success");
    }


    /**
     * GET операция для комментариев к вопросу
     *
     * @param questionId
     *      Id вопроса
     *
     *
     * @request.body.example
     *      {@link CommentRequest#EXAMPLE}
     *
     * @response.200.doc
     *      В случае успеха все коментарии к вопросу в виде списка
     */
    @RequestMapping(value = "/rest/v1/question/{questionId}/comments", method = RequestMethod.GET)
    public Object getComments(@PathVariable("questionId") String questionId) {
        CommentBeanMapper mapper = new CommentBeanMapper();
        return mapper.toList(getContext(), questionService.getQuestion(questionId).getComments());
    }

    /**
     * GET операция для комментария к вопросу
     *
     * @param questionId
     *      Id вопроса
     *
     * @param commentId
     *      Id коментария
     *
     * @request.body.example
     *      {@link CommentRequest#EXAMPLE}
     *
     * @response.200.doc
     *      В случае успеха коментарий
     */

    @RequestMapping(value = "/rest/v1/question/{questionId}/comment/{commentId}", method = RequestMethod.GET)
    public CommentBean getComment(@PathVariable("questionId") String questionId,
                                  @PathVariable("commentId") String commentId) {
        CommentBeanMapper mapper = new CommentBeanMapper();
        return mapper.map(getContext(), questionService.getQuestion(questionId).getComments()
                .stream().filter(x -> x.getId().equals(commentId)).findFirst().get());
    }
}
