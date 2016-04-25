package ru.atott.combiq.service.user;

import ru.atott.combiq.service.site.UserContext;

public interface ProfileService {

    void applyProfileEmail(UserContext uc, String newEmail);
}
