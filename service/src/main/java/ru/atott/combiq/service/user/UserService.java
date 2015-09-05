package ru.atott.combiq.service.user;

import ru.atott.combiq.service.bean.User;

public interface UserService {
    User findByEmail(String email);

    User findByLogin(String login);

    User registerUserViaGithub(GithubRegistrationContext context);

    User updateGithubUser(GithubRegistrationContext registrationContext);
}
