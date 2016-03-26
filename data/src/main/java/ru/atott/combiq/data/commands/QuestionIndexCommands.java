package ru.atott.combiq.data.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.stereotype.Component;
import ru.atott.combiq.data.service.QuestionIndexService;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Component
public class QuestionIndexCommands implements CommandMarker {

    @Autowired
    private QuestionIndexService questionIndexService;

    @CliCommand(value = "update question timestamps", help = "Set missing timestamp values for questions.")
    public String updateTimestamps() throws InterruptedException, ExecutionException, IOException {
        return questionIndexService.updateQuestionTimestamps();
    }

    @CliCommand(value = "update question humanUrlTitles", help = "Set missing humanUrlTitle values for questions.")
    public String updateHumanUrlTitles() {
        questionIndexService.updateHumanUrlTitles();
        return "Done";
    }

    @CliCommand(value = "migrate question stringIdsToNumbers")
    public String migrateIdsToNumbers() {
        questionIndexService.migrateQuestionIdsToNumbers();
        questionIndexService.migrateQuestionnaireIdsToNumbers();
        return "Done";
    }
}
