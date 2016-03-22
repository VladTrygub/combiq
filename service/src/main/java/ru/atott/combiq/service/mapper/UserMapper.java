package ru.atott.combiq.service.mapper;

import ru.atott.combiq.dao.entity.UserEntity;
import ru.atott.combiq.service.bean.User;
import ru.atott.combiq.service.bean.UserType;
import ru.atott.combiq.service.user.UserRoles;

import java.util.ArrayList;

public class UserMapper implements Mapper<UserEntity, User> {

    @Override
    public User map(UserEntity source) {
        User user = new User();
        user.setEmail(source.getEmail());
        user.setId(source.getId());
        user.setName(source.getName());
        user.setPasswordHash(source.getPassword());
        user.setLogin(source.getLogin());
        user.setLocation(source.getLocation());
        user.setHome(source.getHome());
        user.setType(UserType.valueOf(source.getType()));
        user.setAvatarUrl(source.getAvatarUrl());
        user.setRegisterDate(source.getRegisterDate());
        user.setFavoriteQuestions(source.getFavoriteQuestions());
        user.setNickName(source.getNickName());

        ArrayList<String> roles = new ArrayList<>();

        if (source.getRoles() != null) {
            roles.addAll(source.getRoles());
        }

        roles.add(UserRoles.user);

        user.setRoles(roles);
        return user;
    }
}
