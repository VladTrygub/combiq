package ru.atott.combiq.web.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.atott.combiq.service.bean.UserQualifier;
import ru.atott.combiq.service.site.UserContext;
import ru.atott.combiq.service.site.UserContextProvider;
import ru.atott.combiq.web.filter.RequestHolderFilter;

import java.util.HashSet;

@Service
public class AuthServiceImpl implements AuthService, UserContextProvider {

    private static String launchDependentSalt = String.valueOf(System.currentTimeMillis());

    @Override
    public CombiqUser getUser() {
        CombiqUser user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CombiqUser) {
            user = (CombiqUser) principal;
        }
        return user;
    }

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public UserContext getUserContext() {
        CombiqUser user = getUser();

        UserContext uc = new UserContext();
        uc.setAnonimous(user == null);

        if (user != null) {
            uc.setUserName(user.getName());
            uc.setUserQualifier(new UserQualifier(user.getType(), user.getLogin()));
            uc.setUserId(user.getId());
            uc.setRoles(new HashSet<>(user.getRoles()));
        }

        return uc;
    }

    @Override
    public String getUserId() {
        CombiqUser user = getUser();
        if (user == null) {
            return null;
        }
        return user.getId();
    }

    @Override
    public String getLaunchDependentSalt() {
        return launchDependentSalt;
    }

    @Override
    public String getSessionId() {
        return RequestHolderFilter.REQUEST.get().getSession(true).getId();
    }
}
