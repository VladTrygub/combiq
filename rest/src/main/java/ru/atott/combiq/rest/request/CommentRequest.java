package ru.atott.combiq.rest.request;

public class CommentRequest {

    private String content;

    public static CommentRequest EXAMPLE;

    static {
        EXAMPLE = new CommentRequest();
        EXAMPLE.setContent("Пример комментария");
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
