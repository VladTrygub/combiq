package ru.atott.combiq.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProjectController extends BaseController {

    @RequestMapping(value = "/project")
    public ModelAndView project() {
        ModelAndView modelAndView = new ModelAndView("project/project");
        return modelAndView;
    }

    @RequestMapping(value = "/project/wtf")
    public ModelAndView wtf() {
        ModelAndView modelAndView = new ModelAndView("project/wtf");
        return modelAndView;
    }

    @RequestMapping(value = "/project/resources")
    public ModelAndView resources() {
        ModelAndView modelAndView = new ModelAndView("project/resources");
        return modelAndView;
    }
}
