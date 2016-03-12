package ru.atott.combiq.web.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.atott.combiq.service.user.UserStars;
import ru.atott.combiq.web.bean.SuccessBean;
import ru.atott.combiq.web.view.SearchViewBuilder;


/**
 * Created by Леонид on 11.03.2016.
 */

@Controller
public class UserController extends BaseController {

    @Autowired
    private UserStars userStars;


    @RequestMapping(value = "/questions/{questionId}/like", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAnyRole('user','sa','contenter')")
    public Object like(@PathVariable("questionId") String questionId) {
        userStars.like(getUc().getUserId(),questionId);
        return new SuccessBean();
    }

    @RequestMapping(value = "/questions/{questionId}/dislike", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAnyRole('user','sa','contenter')")
    public Object dislike(@PathVariable("questionId") String questionId) {
        userStars.dislike(getUc().getUserId(),questionId);
        return new SuccessBean();
    }

    @RequestMapping(value = "/user/favorite", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAnyRole('user','sa','contenter')")
    public ModelAndView getAll(){
        SearchViewBuilder viewBuilder = new SearchViewBuilder();
        viewBuilder.setQuestions(userStars.getAll(getUc().getUserId()));
        return viewBuilder.build();
    }
}
