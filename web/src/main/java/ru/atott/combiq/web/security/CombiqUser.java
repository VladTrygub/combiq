package ru.atott.combiq.web.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ru.atott.combiq.service.bean.UserType;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CombiqUser extends User {
    private UserType type;
    private String login;
    private String id;
    private String avatarUrl;
    private String email;
    private String name;
    private Set<String> roles;

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
                roles.stream()
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        this.roles = Sets.newHashSet(roles);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getHeadAvatarUrl() {
        if (avatarUrl == null) {
            return "/static/images/icons/user-48.png";
        }

        switch (type) {
            case github:
                return avatarUrl.contains("?") ? avatarUrl + "&s=46" : avatarUrl + "?s=46";
            case vk:
                return avatarUrl;
            case stackexchange:
                return avatarUrl;
            case facebook:
                return avatarUrl;
        }

        return null;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
