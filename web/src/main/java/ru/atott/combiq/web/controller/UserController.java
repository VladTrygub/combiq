package ru.atott.combiq.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ru.atott.combiq.service.bean.User;
import ru.atott.combiq.service.question.AskedQuestionService;
import ru.atott.combiq.service.question.FavoriteQuestionService;
import ru.atott.combiq.service.site.UrlResolver;
import ru.atott.combiq.service.user.UserService;
import ru.atott.combiq.web.bean.SuccessBean;
import ru.atott.combiq.web.request.NickEditRequest;
import ru.atott.combiq.web.utils.ViewUtils;

import java.util.Optional;

@Controller
public class UserController extends BaseController {

    @Autowired
    private FavoriteQuestionService favoriteQuestionService;

    @Autowired
    private AskedQuestionService askedQuestionService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/questions/{questionId}/like", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAnyRole('user')")
    public Object like(@PathVariable("questionId") String questionId) {
        favoriteQuestionService.like(getUc(), questionId);
        return new SuccessBean();
    }

    @RequestMapping(value = "/questions/{questionId}/dislike", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAnyRole('user')")
    public Object dislike(@PathVariable("questionId") String questionId) {
        favoriteQuestionService.dislike(getUc(), questionId);
        return new SuccessBean();
    }

    @RequestMapping(value = {"/users/{userId}", "/users/{userId}/{nick}"})
    public ModelAndView profile(@PathVariable("userId") String userId,
                                @PathVariable Optional<String> nick) {
        User user = userService.findById(userId);
        if (user == null) {
            return notFound();
        }
        String userNick = UrlResolver.encodeUrlComponent(user.getNick());
        if(userNick != null && (!nick.isPresent() || !userNick.equals(nick.get()))){
            return new ModelAndView(new RedirectView("/users/" + user.getId() + "/" + userNick));
        }
        if(nick.isPresent() && (userNick == null)){
            return new ModelAndView(new RedirectView("/users/" + user.getId()));
        }

        ModelAndView modelAndView = new ModelAndView("user/profile");
        modelAndView.addObject("headAvatarUrl", ViewUtils.getHeadAvatarUrl(user.getType(), user.getAvatarUrl()));
        modelAndView.addObject("userName", user.getName());
        if(!StringUtils.isEmpty(user.getNick()))
            modelAndView.addObject("nick",user.getNick());
        else
            modelAndView.addObject ("nick","Не выбран");
        modelAndView.addObject("userRegisterDate", user.getRegisterDate());
        return modelAndView;
    }

    @RequestMapping(value = "/questions/{questionId}/asked", method = RequestMethod.POST)
    public void addCount(@PathVariable("questionId") String questionId){
        askedQuestionService.addAskedCount(getUc(), questionId);
    }

    @RequestMapping(value = "/users/setNick", method = RequestMethod.POST)
    @ResponseBody
    public Object setName(@RequestBody NickEditRequest nickEditRequest) {
        String nick=nickEditRequest.getNick();
        if(nick.equals(super.getCombiqUser().getNick()))
            return new SuccessBean(false,"Это ваш текущий Ник!");
        userService.updateNick(super.getUc().getUserId(), nick);
        super.getCombiqUser().setNick(nick);
        return new SuccessBean();
    }


}
