package ru.atott.combiq.dao.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Document(indexName = "#{domainResolver.resolvePersonalIndex()}", type = "user")
public class UserEntity {

    public static final String REGISTER_DATE_FIELD = "registerDate";

    @Id
    private String id;
    private String name;
    @Field(index = FieldIndex.not_analyzed, type = FieldType.String)
    private String nick;
    private String email;
    private String password;
    private String login;
    private String location;
    private String home;
    private String type;
    private String avatarUrl;
    private List<String> roles;
    private Date registerDate;
    private Set<String> favoriteQuestions;
    private Set<String> askedQuestions;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Set<String> getFavoriteQuestions() {
        return favoriteQuestions;
    }

    public Set<String> getAskedQuestions() {
        return askedQuestions;
    }

    public void setAskedQuestions(Set<String> askedQuestions) {
        this.askedQuestions = askedQuestions;
    }

    public void setFavoriteQuestions(Set<String> favoriteQuestions) {
        this.favoriteQuestions = favoriteQuestions;
    }
}
