package ru.atott.combiq.web.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.atott.combiq.service.user.UserStarsService;
import ru.atott.combiq.web.bean.SuccessBean;



@Controller
public class UserController extends BaseController {

    @Autowired
    private UserStarsService userStarsService;


    @RequestMapping(value = "/questions/{questionId}/like", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAnyRole('user','sa','contenter')")
    public Object like(@PathVariable("questionId") String questionId) {
        userStarsService.like(getUc().getUserId(), questionId);
        return new SuccessBean();
    }

    @RequestMapping(value = "/questions/{questionId}/dislike", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAnyRole('user','sa','contenter')")
    public Object dislike(@PathVariable("questionId") String questionId) {
        userStarsService.dislike(getUc().getUserId(), questionId);
        return new SuccessBean();
    }

}
