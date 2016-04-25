package ru.atott.combiq.service.confirm;

import java.util.Map;

public interface ConfirmHandler {

    ConfirmType getConfirmationType();

    void confirm(Map<String, Object> state);
}
