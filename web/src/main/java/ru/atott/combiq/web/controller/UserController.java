package ru.atott.combiq.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.atott.combiq.service.bean.User;
import ru.atott.combiq.service.user.UserService;
import ru.atott.combiq.web.utils.ViewUtils;

@Controller
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/users/{userId}")
    public ModelAndView profile(@PathVariable("userId") String userId) {
        User user = userService.findById(userId);

        if (user == null) {
            return notFound();
        }

        ModelAndView modelAndView = new ModelAndView("user/profile");
        modelAndView.addObject("headAvatarUrl", ViewUtils.getHeadAvatarUrl(user.getType(), user.getAvatarUrl()));
        modelAndView.addObject("userName", user.getName());
        modelAndView.addObject("userRegisterDate", user.getRegisterDate());
        return modelAndView;
    }
}
