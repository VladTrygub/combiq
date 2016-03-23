package ru.atott.combiq.rest.bean;

import java.util.List;

public class QuestionSearchBean {

    private String dsl;

    private int page;

    private int pageSize;

    private List<QuestionBean> questions;

    private long total;

    public String getDsl() {
        return dsl;
    }

    public void setDsl(String dsl) {
        this.dsl = dsl;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<QuestionBean> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionBean> questions) {
        this.questions = questions;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
