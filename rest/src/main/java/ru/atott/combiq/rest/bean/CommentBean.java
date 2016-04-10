package ru.atott.combiq.rest.bean;

import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;

public class CommentBean {

    public static CommentBean EXAMPLE;

    public static List<CommentBean> EXAMPLE_LIST;

    static {
        EXAMPLE = new CommentBean();
        EXAMPLE.setId("12");
        EXAMPLE.setAuthor(UserBean.EXAMPLE);
        EXAMPLE.setContent(MarkdownContentBean.EXAMPLE);
        EXAMPLE.setEditDate(new Date());
        EXAMPLE.setPostDate(new Date());
        EXAMPLE.setEditedBy(UserBean.EXAMPLE);

        EXAMPLE_LIST = Lists.newArrayList(EXAMPLE);
    }

    private String id;

    private MarkdownContentBean content;

    private Date postDate;

    private Date editDate;

    private UserBean author;

    private UserBean editedBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MarkdownContentBean getContent() {
        return content;
    }

    public void setContent(MarkdownContentBean content) {
        this.content = content;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public UserBean getAuthor() {
        return author;
    }

    public void setAuthor(UserBean author) {
        this.author = author;
    }

    public UserBean getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(UserBean editedBy) {
        this.editedBy = editedBy;
    }
}
