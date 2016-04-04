package ru.atott.combiq.rest.bean;

import java.util.Date;

public class CommentBean {

    private String id;

    private MarkdownContentBean markdown;

    private String userId;

    private String userName;

    private Date postDate;

    private String editUserId;

    private String editUserName;

    private Date editDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MarkdownContentBean getMarkdown() {
        return markdown;
    }

    public void setMarkdown(MarkdownContentBean markdown) {
        this.markdown = markdown;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getEditUserId() {
        return editUserId;
    }

    public void setEditUserId(String editUserId) {
        this.editUserId = editUserId;
    }

    public String getEditUserName() {
        return editUserName;
    }

    public void setEditUserName(String editUserName) {
        this.editUserName = editUserName;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }
}
