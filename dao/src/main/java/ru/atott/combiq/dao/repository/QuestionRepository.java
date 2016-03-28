package ru.atott.combiq.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import ru.atott.combiq.dao.entity.QuestionEntity;

import java.util.Collection;
import java.util.List;

@Component
public interface QuestionRepository extends PagingAndSortingRepository<QuestionEntity, String> {

    Page<QuestionEntity> findByTagsIn(Collection<String> tags, Pageable pageable);

    QuestionEntity findOneByLegacyId(String legacyId);

    List<QuestionEntity> findByAskedTodayGreaterThan(long from);
}
