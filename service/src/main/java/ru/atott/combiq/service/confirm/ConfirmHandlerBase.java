package ru.atott.combiq.service.confirm;

import org.springframework.beans.factory.annotation.Autowired;
import ru.atott.combiq.dao.entity.ConfirmationEntity;
import ru.atott.combiq.dao.repository.ConfirmationRepository;

import java.util.Map;

public abstract class ConfirmHandlerBase implements ConfirmHandler {

    @Autowired
    private ConfirmationRepository confirmationRepository;

    protected String createConfirmation(Map<String, Object> state) {
        ConfirmationEntity confirmationEntity = new ConfirmationEntity();
        confirmationEntity.setConfirmationType(getConfirmationType().name());
        confirmationEntity.setState(state);
        confirmationEntity = confirmationRepository.save(confirmationEntity);
        return confirmationEntity.getId();
    }
}
