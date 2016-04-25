package ru.atott.combiq.dao.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
import ru.atott.combiq.dao.entity.ConfirmationEntity;

@Component
public interface ConfirmationRepository extends ElasticsearchRepository<ConfirmationEntity, String> {

}
