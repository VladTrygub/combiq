package ru.atott.combiq.service.question;

import ru.atott.combiq.service.site.UserContext;

import java.util.Set;


public interface FavoriteQuestionService {

    void like(UserContext uc, String questionId);

    void dislike(UserContext uc, String questionId);

    Set<String> getFavoriteQuestions(UserContext uc);

    boolean isFavoriteQuestion(UserContext uc, String questionId);
}

