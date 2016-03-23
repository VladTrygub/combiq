package ru.atott.combiq.rest.bean;

public class ErrorBean {

    private String error;

    public ErrorBean() { }

    public ErrorBean(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
