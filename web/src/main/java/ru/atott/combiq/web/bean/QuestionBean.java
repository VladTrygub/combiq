package ru.atott.combiq.web.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;
import ru.atott.combiq.dao.entity.MarkdownContent;
import ru.atott.combiq.service.bean.Question;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class QuestionBean {

    private String id;

    private String level;

    private String title;

    private MarkdownContent body;

    private List<String> tags;

    private Date lastModify;

    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MarkdownContent getBody() {
        return body;
    }

    public void setBody(MarkdownContent body) {
        this.body = body;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public static QuestionBean of(Question question) {
        QuestionBean bean = new QuestionBean();
        bean.setBody(question.getBody());
        bean.setId(question.getId());
        bean.setLevel(question.getLevel());
        bean.setTags(question.getTags());
        bean.setTitle(question.getTitle());
        bean.setLastModify(question.getLastModify());
        return bean;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("level", level)
                .append("title", title)
                .append("body", body)
                .append("tags", tags)
                .append("lastModify", new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z").format(lastModify))
                .toString();
    }
}
