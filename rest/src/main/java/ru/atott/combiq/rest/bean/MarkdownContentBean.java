package ru.atott.combiq.rest.bean;

import ru.atott.combiq.dao.entity.MarkdownContent;

public class MarkdownContentBean {

    public static MarkdownContentBean EXAMPLE;

    static {
        EXAMPLE = new MarkdownContentBean();
        EXAMPLE.setMarkdown("Привет");
        EXAMPLE.setHtml("<p>Привет</p>");
    }

    private String markdown;

    private String html;

    public MarkdownContentBean() { }

    public MarkdownContentBean(MarkdownContent content) {
        if (content != null) {
            markdown = content.getMarkdown();
            html = content.getHtml();
        }
    }

    public String getMarkdown() {
        return markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
