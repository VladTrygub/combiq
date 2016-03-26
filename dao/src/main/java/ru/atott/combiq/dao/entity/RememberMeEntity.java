package ru.atott.combiq.dao.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import ru.atott.combiq.dao.Types;

import java.util.Date;

@Document(indexName = "#{domainResolver.resolveSystemIndex()}", type = Types.rememberMe)
public class RememberMeEntity {

    @Id
    private String series;

    private Date lastused;

    private String token;

    private String username;

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public Date getLastused() {
        return lastused;
    }

    public void setLastused(Date lastused) {
        this.lastused = lastused;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
