package ru.atott.combiq.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.atott.combiq.service.user.UserStars;
import ru.atott.combiq.web.bean.SuccessBean;
import ru.atott.combiq.web.request.EditCommentRequest;

/**
 * Created by Леонид on 11.03.2016.
 */
public class UserController extends BaseController {

    @Autowired
    private UserStars userStars;


    @RequestMapping(value = "/questions/{questionId}/like", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAnyRole('user')")
    public Object like(@PathVariable("questionId") String questionId) {
        userStars.like(getUc().getUserId(),questionId);
        return new SuccessBean();
    }
    @RequestMapping(value = "/questions/{questionId}/dislike", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAnyRole('user')")
    public Object dislike(@PathVariable("questionId") String questionId) {
        userStars.dislike(getUc().getUserId(),questionId);
        return new SuccessBean();
    }
}
