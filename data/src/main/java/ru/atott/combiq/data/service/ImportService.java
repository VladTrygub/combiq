package ru.atott.combiq.data.service;

import java.io.IOException;

public interface ImportService {

    String importQuestionnareOds(String filename, String questionnaireName) throws Exception;

    String importJdk8ClassesDictionary() throws IOException;
}
