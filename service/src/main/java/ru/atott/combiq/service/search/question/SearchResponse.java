package ru.atott.combiq.service.search.question;

import org.springframework.data.domain.Page;
import ru.atott.combiq.service.bean.Question;
import ru.atott.combiq.service.bean.Tag;

import java.util.List;

public class SearchResponse {

    private Page<Question> questions;

    private List<Tag> popularTags;

    public Page<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Page<Question> questions) {
        this.questions = questions;
    }

    public List<Tag> getPopularTags() {
        return popularTags;
    }

    public void setPopularTags(List<Tag> popularTags) {
        this.popularTags = popularTags;
    }
}
