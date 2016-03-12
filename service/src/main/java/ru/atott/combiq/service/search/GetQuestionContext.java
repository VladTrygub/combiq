package ru.atott.combiq.service.search;

import ru.atott.combiq.service.dsl.DslQuery;

public class GetQuestionContext {

    private String id;

    private DslQuery dsl;

    private Integer proposedIndexInDslResponse;

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DslQuery getDsl() {
        return dsl;
    }

    public void setDsl(DslQuery dsl) {
        this.dsl = dsl;
    }

    public Integer getProposedIndexInDslResponse() {
        return proposedIndexInDslResponse;
    }

    public void setProposedIndexInDslResponse(Integer proposedIndexInDslResponse) {
        this.proposedIndexInDslResponse = proposedIndexInDslResponse;
    }
}
