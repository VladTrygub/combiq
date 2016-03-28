package ru.atott.combiq.web.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.atott.combiq.service.question.AskedQuestionService;

@Component
public class AskedQuestionRecountJob {

    @Autowired
    private AskedQuestionService askedQuestionService;

    @Scheduled(cron = "0 1 3 * * ?")
    public void recount() {
        askedQuestionService.recountAskedCounts();
    }
}
