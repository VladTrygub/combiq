package ru.atott.combiq.service.search.comment;

import ru.atott.combiq.service.bean.Question;

public class LatestComment {

    private Question question;

    private String commentSimplifiedHtml;

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getCommentSimplifiedHtml() {
        return commentSimplifiedHtml;
    }

    public void setCommentSimplifiedHtml(String commentSimplifiedHtml) {
        this.commentSimplifiedHtml = commentSimplifiedHtml;
    }
}
