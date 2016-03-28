package ru.atott.combiq.data.service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface QuestionIndexService {

    String importQuestions(String env) throws IOException, ExecutionException, InterruptedException;

    String updateQuestionTimestamps() throws IOException, ExecutionException, InterruptedException;

    void updateHumanUrlTitles();

    void migrateQuestionIdsToNumbers();

    void migrateQuestionnaireIdsToNumbers();
}
