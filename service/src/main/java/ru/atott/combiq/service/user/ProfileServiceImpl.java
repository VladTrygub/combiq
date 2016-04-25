package ru.atott.combiq.service.user;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.atott.combiq.service.confirm.ConfirmHandlerChangeProfileEmail;
import ru.atott.combiq.service.site.UserContext;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ConfirmHandlerChangeProfileEmail confirmHandlerChangeProfileEmail;

    @Override
    public void applyProfileEmail(UserContext uc, String newEmail) {
        Validate.notEmpty(newEmail);
        Validate.isTrue(!uc.isAnonimous());

        confirmHandlerChangeProfileEmail.createConfirmation(uc, newEmail);
    }
}
