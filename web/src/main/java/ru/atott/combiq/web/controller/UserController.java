package ru.atott.combiq.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.atott.combiq.service.bean.User;
import ru.atott.combiq.service.question.AskedQuestionService;
import ru.atott.combiq.service.user.UserService;
import ru.atott.combiq.service.question.FavoriteQuestionService;
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

    @RequestMapping(value = {"/users/{userId}","/users/{userId}/{nickName}"})
    public ModelAndView profile(@PathVariable("userId") String userId,
                                @PathVariable Optional<String> nickName) {
        User user = userService.findById(userId);

        if (user == null) {
            return notFound();
        }
        if(!nickName.isPresent()&&user.getNickName()!=null){
            return notFound();
        }
        if(nickName.isPresent()&&(user.getNickName()==null||!user.getNickName().equals(nickName.get()))){
            return notFound();
        }

        ModelAndView modelAndView = new ModelAndView("user/profile");
        modelAndView.addObject("headAvatarUrl", ViewUtils.getHeadAvatarUrl(user.getType(), user.getAvatarUrl()));
        modelAndView.addObject("userName", user.getName());
        if(!(user.getNickName()==null)&&!user.getNickName().isEmpty())
            modelAndView.addObject("nickName",user.getNickName());
        else
            modelAndView.addObject ("nickName","Не выбран");
        modelAndView.addObject("userRegisterDate", user.getRegisterDate());
        return modelAndView;
    }

    @RequestMapping(value = "/questions/{questionId}/asked", method = RequestMethod.POST)
    public void addCount(@PathVariable("questionId") String questionId){
        askedQuestionService.addAskedCount(getUc(), questionId);
    }

    @RequestMapping(value = "/users/setNickName", method = RequestMethod.POST)
    @ResponseBody
    public Object setName(@RequestBody NickEditRequest nickNameEditRequest) {
        String nickName=nickNameEditRequest.getNickName();
        if(userService.isNickNameUniq(nickName)){
            userService.updateNickName(super.getUc().getUserId(), nickName);
            super.getCombiqUser().setNickName(nickName);
            return new SuccessBean();
        } else if(super.getCombiqUser().getNickName().equals(nickName))
            return new SuccessBean(false, "Это ваш текущий Ник name, вы можете выбрать другой");

        return new SuccessBean(false,"Этот Ник занят, выберите пожалуйста другой");
    }


}
