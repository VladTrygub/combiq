package ru.atott.combiq.data.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;
import ru.atott.combiq.data.service.ImportService;
import ru.atott.combiq.data.service.QuestionIndexService;

import java.io.IOException;

@Component
public class ImportCommands implements CommandMarker {

    @Autowired
    private ImportService importService;

    @Autowired
    private QuestionIndexService questionIndexService;

    @CliCommand(
            value = "import questionnare ods",
            help = "import ODS questionnaire file with columns: question, level, tags, tip")
    public String importQuestionnareOds(
            @CliOption(key = "file", mandatory = true) String file,
            @CliOption(key = "name", mandatory = true) String name) throws Exception {
        try {
            return importService.importQuestionnareOds(file, name);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @CliCommand(value = "import dictionary jdk8classes")
    public String importJdk8ClassesDictionary() throws IOException {
        try {
            return importService.importJdk8ClassesDictionary();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @CliCommand(value = "import questions")
    public String importQuestions() throws Exception {
        try {
            return questionIndexService.importQuestions(CommandsContext.env);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
