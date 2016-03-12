package ru.atott.combiq.service.search;

import ru.atott.combiq.service.bean.Question;

public class GetQuestionResponse {
    private Question question;
    private QuestionPositionInDsl positionInDsl;

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public QuestionPositionInDsl getPositionInDsl() {
        return positionInDsl;
    }

    public void setPositionInDsl(QuestionPositionInDsl positionInDsl) {
        this.positionInDsl = positionInDsl;
    }
}
