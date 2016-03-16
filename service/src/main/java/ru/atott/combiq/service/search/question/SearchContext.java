package ru.atott.combiq.service.search.question;

import org.apache.commons.lang3.builder.ToStringBuilder;
import ru.atott.combiq.service.dsl.DslQuery;
import ru.atott.combiq.service.site.UserContext;

import java.util.Collections;
import java.util.List;

public class SearchContext {

    private int from = 0;

    private int size = 20;

    private DslQuery dslQuery;

    private String userId;

    private List<String> questionIds;

    private UserContext userContext;


    public void setQuestionId(String questionId) {
        this.questionIds = Collections.singletonList(questionId);
    }

    public List<String> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<String> questionIds) {
        this.questionIds = questionIds;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public DslQuery getDslQuery() {
        return dslQuery;
    }

    public void setDslQuery(DslQuery dslQuery) {
        this.dslQuery = dslQuery;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserContext getUserContext() {
        return userContext;
    }

    public void setUserContext(UserContext userContext) {
        this.userContext = userContext;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("from", from)
                .append("size", size)
                .append("dslQuery", dslQuery)
                .append("userId", userId)
                .append("questionIds", questionIds)
                .append("userContext", userContext)
                .toString();
    }
}
