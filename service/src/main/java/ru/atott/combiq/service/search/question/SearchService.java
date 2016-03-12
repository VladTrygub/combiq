package ru.atott.combiq.service.search.question;

import ru.atott.combiq.service.bean.Question;
import ru.atott.combiq.service.site.UserContext;

import java.util.Optional;

public interface SearchService {

    SearchResponse searchQuestions(SearchContext context);

    long countQuestions(SearchContext context);

    Optional<SearchResponse> searchAnotherQuestions(UserContext uc, Question question);

    GetQuestionResponse getQuestion(UserContext uc, GetQuestionContext context);

    Question getQuestionByLegacyId(String legacyId);
}
