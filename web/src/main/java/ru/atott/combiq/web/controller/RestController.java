package ru.atott.combiq.web.controller;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.atott.combiq.rest.doc.RestDocumentIndex;
import ru.atott.combiq.rest.v1.question.CommentRestController;
import ru.atott.combiq.rest.v1.question.QuestionRestController;
import ru.atott.combiq.rest.v1.user.ProfileRestController;

import java.util.List;

@SuppressWarnings("unchecked")
@Controller
public class RestController extends BaseController {

    private static List<List<Class>> restControllerBuckets = Lists.<List<Class>>newArrayList(
            Lists.newArrayList(
                    QuestionRestController.class,
                    CommentRestController.class
            ),
            Lists.newArrayList(
                    ProfileRestController.class
            )
    );

    private volatile RestDocumentIndex documentIndex;

    @RequestMapping(value = "/rest/v1")
    public ModelAndView get() {

        if (documentIndex == null) {
            synchronized (this) {
                if (documentIndex == null) {
                    documentIndex = new RestDocumentIndex(restControllerBuckets);
                }
            }
        }

        ModelAndView modelAndView = new ModelAndView("rest");
        modelAndView.addObject("documentIndex", documentIndex);
        return modelAndView;
    }
}
