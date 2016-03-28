package ru.atott.combiq.service.question;

import ru.atott.combiq.service.site.UserContext;

public interface AskedQuestionService {

    void addAskedCount(UserContext uc, String questionId);

    boolean isAskedQuestion(UserContext uc, String questionId);

    /**
     * Пересчитать asked счетчики,
     * переложить значения askedToday в askedCount.
     */
    void recountAskedCounts();
}
