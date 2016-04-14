package ru.atott.combiq.web.security;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.atott.combiq.service.bean.User;
import ru.atott.combiq.service.bean.UserQualifier;
import ru.atott.combiq.service.user.UserService;

public class CombiqUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String qualifier) throws UsernameNotFoundException {
        try {
            UserQualifier userQualifier = new UserQualifier(qualifier);
            User user = userService.findByLoginAndType(userQualifier.getLogin(), userQualifier.getType());
            if (user != null) {
                String passwordHash = user.getPasswordHash();
                switch (user.getType()) {
                    case github:
                        passwordHash = DigestUtils.sha1Hex("github");
                        break;
                    case vk:
                        passwordHash = DigestUtils.sha1Hex("vk");
                        break;
                    case stackexchange:
                        passwordHash = DigestUtils.sha1Hex("stackexchange");
                        break;
                    case facebook:
                        passwordHash = DigestUtils.sha1Hex("facebook");
                        break;
                }

                CombiqUser combiqUser = new CombiqUser(qualifier, passwordHash, user.getRoles());
                combiqUser.setType(user.getType());
                combiqUser.setLogin(user.getLogin());
                combiqUser.setId(user.getId());
                combiqUser.setAvatarUrl(user.getAvatarUrl());
                combiqUser.setEmail(user.getEmail());
                combiqUser.setName(user.getName());
                combiqUser.setNick(user.getNick());
                return combiqUser;
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
        throw new UsernameNotFoundException(String.format("User '%s' is not found.", qualifier));
    }
}
