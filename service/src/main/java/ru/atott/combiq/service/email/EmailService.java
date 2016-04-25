package ru.atott.combiq.service.email;

import java.util.Collections;
import java.util.List;

public interface EmailService {

    void sendEmail(EmailType type, Object model, List<String> to);

    default void sendEmail(EmailType type, Object model, String to) {
        sendEmail(type, model, Collections.singletonList(to));
    }

    default void sendEmail(EmailType type, Object model) {
        sendEmail(type, model, (List<String>) null);
    }
}
