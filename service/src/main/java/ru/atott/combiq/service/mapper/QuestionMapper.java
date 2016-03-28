package ru.atott.combiq.service.mapper;

import ru.atott.combiq.dao.entity.MarkdownContent;
import ru.atott.combiq.dao.entity.QuestionEntity;
import ru.atott.combiq.service.bean.Question;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionMapper implements Mapper<QuestionEntity, Question> {

    public QuestionMapper() { }

    @Override
    public Question map(QuestionEntity source) {
        String questionId = source.getId();

        Question question = new Question();
        question.setId(questionId);
        question.setTitle(source.getTitle());

        List<String> tags = source.getTags() == null ? Collections.emptyList() : source.getTags();
        question.setTags(tags.stream().map(String::toLowerCase).collect(Collectors.toList()));

        question.setLevel("D" + source.getLevel());
        if (source.getReputation() == null) {
            question.setReputation(0);
        } else {
            question.setReputation(source.getReputation());
        }
        question.setTip(source.getTip());
        if (source.getBody() != null) {
            question.setBody(source.getBody());
        } else {
            question.setBody(new MarkdownContent());
        }
        question.setComments(source.getComments());
        if (question.getComments() == null) {
            question.setComments(Collections.emptyList());
        }
        question.setDeleted(source.isDeleted());
        question.setAuthorId(source.getAuthorId());
        question.setAuthorName(source.getAuthorName());
        question.setLanding(source.isLanding());
        question.setClassNames(source.getClassNames());
        question.setHumanUrlTitle(source.getHumanUrlTitle());
        question.setStars(source.getStars());
        question.setLastModify(source.getLastModify());
        if (source.getAskedCount() == null) {
            question.setAskedCount(source.getAskedToday());
        } else {
            question.setAskedCount(source.getAskedCount() + source.getAskedToday());
        }
        return question;
    }
}
