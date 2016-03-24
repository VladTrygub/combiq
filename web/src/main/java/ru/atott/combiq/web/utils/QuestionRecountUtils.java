package ru.atott.combiq.web.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.atott.combiq.service.user.UserStarsService;

@Component
public class QuestionRecountUtils {

    @Autowired
    private UserStarsService userStarsService;

    @Scheduled(cron = "0 1 * * * ?")
    public void recount(){
        userStarsService.recount();
    }
}
