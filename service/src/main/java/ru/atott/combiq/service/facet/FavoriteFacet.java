package ru.atott.combiq.service.facet;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.IdsFilterBuilder;
import org.springframework.context.ApplicationContext;
import ru.atott.combiq.dao.Types;
import ru.atott.combiq.dao.entity.UserEntity;
import ru.atott.combiq.dao.repository.UserRepository;
import ru.atott.combiq.service.util.ApplicationContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class FavoriteFacet implements Facet {

    private boolean favorite;

    public FavoriteFacet(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public Optional<FilterBuilder> getFilter(FacetContext context) {
        if (context.getUserContext().isAnonimous()) {

            return Optional.of(FacetUtils.getNothingToFindFilter());
        } else {
            ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
            UserRepository userRepository = applicationContext.getBean(UserRepository.class);
            UserEntity userEntity = userRepository.findOne(context.getUserContext().getUserId());
            Set<String> favoriteQuestionIds = userEntity.getFavoriteQuestions();

            if (CollectionUtils.isEmpty(favoriteQuestionIds)) {
                return Optional.of(FacetUtils.getNothingToFindFilter());
            }

            String[] favoriteQuestionIdsArray = favoriteQuestionIds.toArray(new String[favoriteQuestionIds.size()]);
            IdsFilterBuilder idsFilter = FilterBuilders.idsFilter(Types.question).addIds(favoriteQuestionIdsArray);

            if (favorite) {
                return Optional.of(idsFilter);
            } else {
                return Optional.of(FilterBuilders.notFilter(idsFilter));
            }
        }
    }
}
