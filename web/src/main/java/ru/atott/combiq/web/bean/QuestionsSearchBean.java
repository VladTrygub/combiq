package ru.atott.combiq.web.bean;

import org.springframework.data.domain.Page;
import ru.atott.combiq.service.bean.Question;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionsSearchBean {

    private int page;

    private int size;

    private List<QuestionBean> questions;

    private long totalElements;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<QuestionBean> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionBean> questions) {
        this.questions = questions;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public static QuestionsSearchBean of(Page<Question> questions){
        QuestionsSearchBean searchBean = new QuestionsSearchBean();
        searchBean.setPage(questions.getNumber());
        searchBean.setSize(questions.getSize());
        searchBean.setTotalElements(questions.getTotalElements());
        searchBean.setQuestions(questions.getContent()
                                    .stream().map(QuestionBean::of)
                                    .collect(Collectors.toList()));
        return searchBean;
    }
}
