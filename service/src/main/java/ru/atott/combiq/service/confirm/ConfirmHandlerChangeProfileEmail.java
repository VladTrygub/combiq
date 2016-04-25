package ru.atott.combiq.service.confirm;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.atott.combiq.dao.entity.UserEntity;
import ru.atott.combiq.dao.repository.UserRepository;
import ru.atott.combiq.service.email.EmailService;
import ru.atott.combiq.service.email.EmailType;
import ru.atott.combiq.service.site.UserContext;

import java.util.HashMap;
import java.util.Map;

@Component
public class ConfirmHandlerChangeProfileEmail extends ConfirmHandlerBase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public String createConfirmation(UserContext uc, String desiredEmail) {
        Validate.notNull(desiredEmail);
        Validate.notNull(uc);
        Validate.isTrue(!uc.isAnonimous());

        Map<String, Object> state = new HashMap<>();
        state.put("userId", uc.getUserId());
        state.put("desiredEmail", desiredEmail.toLowerCase());

        emailService.sendEmail(EmailType.changeProfileEmailConfirmation, null, desiredEmail);

        return createConfirmation(state);
    }

    @Override
    public ConfirmType getConfirmationType() {
        return ConfirmType.CHANGE_PROFILE_EMAIL;
    }

    @Override
    public void confirm(Map<String, Object> state) {
        Validate.notNull(state);

        String userId = (String) state.get("userId");
        String desiredEmail = (String) state.get("desiredEmail");

        Validate.notNull(userId);
        Validate.notNull(desiredEmail);

        UserEntity userEntity = userRepository.findOne(userId);
        userEntity.setEmail(desiredEmail);
        userRepository.save(userEntity);
    }
}
