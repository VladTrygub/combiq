package ru.atott.combiq.rest.bean;

import java.util.Date;
import java.util.List;

public class QuestionBean {

    private String id;

    private String title;

    private List<String> tags;

    private String uri;

    private int commentsCount;

    private Date changeDate;

    private String level;

    private MarkdownContentBean body;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public MarkdownContentBean getBody() {
        return body;
    }

    public void setBody(MarkdownContentBean body) {
        this.body = body;
    }
}
