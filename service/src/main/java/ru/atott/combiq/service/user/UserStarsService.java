package ru.atott.combiq.service.user;

import ru.atott.combiq.service.site.UserContext;

import java.util.Set;


public interface UserStarsService {

    void like(UserContext uc, String questionId);

    void dislike(UserContext uc, String questionId);

    Set<String> getFavoriteQuestions(UserContext uc);

    boolean isFavoriteQuestion(UserContext uc, String questionId);

    void addAskedCount(UserContext uc, String questionId);

    boolean isAsked(UserContext uc, String questionId);
}

