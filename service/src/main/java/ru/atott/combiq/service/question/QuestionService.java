package ru.atott.combiq.service.question;

import ru.atott.combiq.service.bean.Question;
import ru.atott.combiq.service.site.UserContext;

import java.util.List;

public interface QuestionService {

    void saveComment(UserContext uc, String questionId, String comment);

    void updateComment(UserContext uc, String questionId, String commentId, String comment);

    void saveQuestionBody(UserContext uc, String questionId, String body);

    void saveQuestion(UserContext uc, Question question);

    void deleteQuestion(UserContext uc, String questionId);

    void restoreQuestion(UserContext uc, String questionId);

    Question getQuestion(String id);

    List<String> refreshMentionedClassNames(Question question);
}
