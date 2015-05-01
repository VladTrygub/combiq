package ru.atott.combiq.web.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ru.atott.combiq.service.bean.UserType;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class CombiqUser extends User {
    private UserType type;
    private String login;

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public CombiqUser(String username, String password) {
        this(username, password, Collections.emptyList());
    }

    public CombiqUser(String username, String password, Collection<String> roles) {
        super(username, password,
                roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}