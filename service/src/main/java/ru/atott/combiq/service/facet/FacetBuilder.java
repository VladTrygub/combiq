package ru.atott.combiq.service.facet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.FilterBuilder;
import ru.atott.combiq.service.dsl.DslQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FacetBuilder {

    private DslQuery dslQuery;

    private List<String> questionIds = new ArrayList<>();

    public FacetBuilder setDslQuery(DslQuery dslQuery) {
        this.dslQuery = dslQuery;
        return this;
    }

    public FacetBuilder addQuestionIds(List<String> questionIds) {
        if (questionIds != null) {
            this.questionIds.addAll(questionIds);
        }
        return null;
    }

    public FacetBuilder addQuestionId(String questionId) {
        this.questionIds.add(questionId);
        return null;
    }

    public List<Facet> build() {
        List<Facet> facets = new ArrayList<>();

        // Фасет по уровню.
        if (dslQuery != null && StringUtils.isNotBlank(dslQuery.getLevel())) {
            facets.add(new LevelFacet(NumberUtils.toLong(StringUtils.substring(dslQuery.getLevel(), 1))));
        }

        // Фасет по тэгам.
        if (dslQuery != null && dslQuery.getTags() != null) {
            dslQuery.getTags().forEach(dslTag -> {
                facets.add(new TagFacet(dslTag.getValue()));
            });
        }

        // Фасет по идентификаторам вопросов.
        if (!questionIds.isEmpty()) {
            facets.add(new QuestionIdsFacet(questionIds));
        }

        // Фасет по количеству комментариев к вопросам.
        if (dslQuery != null && dslQuery.getMinCommentQuantity() != null) {
            facets.add(new CommentQuantityFacet(dslQuery.getMinCommentQuantity()));
        }

        // Фасет по ключевым словам.
        if (dslQuery != null && dslQuery.getTerms() != null) {
            dslQuery.getTerms().forEach(term -> {
                facets.add(new TermFacet(term.getValue()));
            });
        }

        // Фасет по пользователю.
        if (dslQuery != null && StringUtils.isNotBlank(dslQuery.getUser())) {
            facets.add(new UserFacet(dslQuery.getUser()));
        }

        // Фасет по удаленным вопросам.
        facets.add(new DeletedFacet(dslQuery != null && dslQuery.getDeleted() != null && dslQuery.getDeleted()));

        // Фасет по фаворитным вопорсам.
        if (dslQuery != null && dslQuery.getFavorite() != null) {
            facets.add(new FavoriteFacet(dslQuery.getFavorite()));
        }

        return facets;
    }
}
