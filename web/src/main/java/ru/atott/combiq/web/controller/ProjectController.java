package ru.atott.combiq.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.atott.combiq.service.site.ContentService;

@Controller
public class ProjectController extends BaseController {

    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "/project")
    public ModelAndView project() {
        ModelAndView modelAndView = new ModelAndView("project/project");
        modelAndView.addObject("pageContent", contentService.getContent(getUc(), "project"));
        return modelAndView;
    }

    @RequestMapping(value = "/project/wtf")
    public ModelAndView wtf() {
        ModelAndView modelAndView = new ModelAndView("project/wtf");
        modelAndView.addObject("pageContent", contentService.getContent(getUc(), "project-wtf"));
        return modelAndView;
    }

    @RequestMapping(value = "/project/wtf/android")
    public ModelAndView wtfAndroid() {
        ModelAndView modelAndView = new ModelAndView("project/wtf-android");
        modelAndView.addObject("pageContent", contentService.getContent(getUc(), "project-wtf-android"));
        return modelAndView;
    }

    @RequestMapping(value = "/project/resources")
    public ModelAndView resources() {
        return new ModelAndView("project/resources");
    }
}
