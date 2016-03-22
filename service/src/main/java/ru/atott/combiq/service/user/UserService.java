package ru.atott.combiq.service.user;

import org.springframework.data.domain.Page;
import ru.atott.combiq.service.bean.User;
import ru.atott.combiq.service.bean.UserQualifier;
import ru.atott.combiq.service.bean.UserType;

import java.util.Date;
import java.util.List;

public interface UserService {

    User findByLoginAndType(String login, UserType provider);

    User findByQualifier(UserQualifier userQualifier);

    User findById(String userId);

    long getCountRegisteredUsers();

    long getCountRegisteredUsersSince(Date since);

    User registerUserViaGithub(GithubRegistrationContext context);

    User registerUserViaVk(VkRegistrationContext context);

    User registerUserViaFacebook(FacebookRegistrationContext context);

    User registerUserViaStackexchange(StackexchangeRegistrationContext context);

    User updateGithubUser(GithubRegistrationContext registrationContext);

    User updateVkUser(VkRegistrationContext registrationContext);

    User updateFacebookUser(FacebookRegistrationContext registrationContext);

    User updateStackexchangeUser(StackexchangeRegistrationContext registrationContext);

    Page<User> getRegisteredUsers(long page, long size);

    List<User> getLastRegisteredUsers(long count);

    void grantRole(UserQualifier userQualifier, String role) throws UserNotFoundException;

    void revokeRole(UserQualifier userQualifier, String role) throws UserNotFoundException;

    void updateNickName(String id, String nickName);
}
