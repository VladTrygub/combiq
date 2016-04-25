package ru.atott.combiq.service.confirm;

import org.apache.commons.lang.Validate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import ru.atott.combiq.dao.entity.ConfirmationEntity;
import ru.atott.combiq.dao.repository.ConfirmationRepository;

import java.util.Map;
import java.util.Objects;

@Service
public class ConfirmationResolverImpl implements ConfirmationResolver, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private ConfirmationRepository confirmationRepository;

    @Override
    public void resolve(String confirmationId) {
        Validate.notNull(confirmationId);

        ConfirmationEntity confirmationEntity = confirmationRepository.findOne(confirmationId);
        Validate.notNull(confirmationEntity);

        Map<String, ConfirmHandler> beansOfType = applicationContext.getBeansOfType(ConfirmHandler.class);
        beansOfType.values().stream()
                .filter(handler -> Objects.equals(handler.getConfirmationType().name(), confirmationEntity.getConfirmationType()))
                .findAny()
                .ifPresent(confirmationHandler -> confirmationHandler.confirm(confirmationEntity.getState()));

        confirmationRepository.delete(confirmationEntity);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
