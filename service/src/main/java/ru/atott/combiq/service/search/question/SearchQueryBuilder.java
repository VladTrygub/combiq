package ru.atott.combiq.service.search.question;

import com.google.common.collect.Lists;
import org.apache.commons.lang.Validate;
import org.elasticsearch.action.count.CountRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.global.GlobalBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.sort.SortOrder;
import ru.atott.combiq.dao.Types;
import ru.atott.combiq.dao.entity.QuestionEntity;
import ru.atott.combiq.dao.es.NameVersionDomainResolver;
import ru.atott.combiq.service.facet.Facet;
import ru.atott.combiq.service.facet.FacetContext;
import ru.atott.combiq.service.facet.FacetBuilder;

import java.util.ArrayList;
import java.util.List;

public class SearchQueryBuilder {

    private SearchContext searchContext;

    private NameVersionDomainResolver domainResolver;

    private Client client;

    public SearchQueryBuilder() { }

    public SearchQueryBuilder setSearchContext(SearchContext searchContext) {
        this.searchContext = searchContext;
        return this;
    }

    public SearchQueryBuilder setDomainResolver(NameVersionDomainResolver domainResolver) {
        this.domainResolver = domainResolver;
        return this;
    }

    public SearchQueryBuilder setClient(Client client) {
        this.client = client;
        return this;
    }

    public SearchRequestBuilder buildSearchRequest() {
        Validate.notNull(searchContext);
        Validate.notNull(domainResolver);
        Validate.notNull(client);

        SearchRequestBuilder result = client
                .prepareSearch(domainResolver.resolveQuestionIndex())
                .setTypes(Types.question)
                .setQuery(createQuery(searchContext))
                .addSort("askedCount", SortOrder.DESC)
                .addSort(QuestionEntity.TIMESTAMP_FIELD, SortOrder.DESC)
                .setFrom(searchContext.getFrom())
                .setSize(searchContext.getSize());

        getAggregationBuilders().stream().forEach(result::addAggregation);

        return result;
    }

    public CountRequestBuilder buildCountRequest() {
        Validate.notNull(searchContext);
        Validate.notNull(domainResolver);
        Validate.notNull(client);

        return client
                .prepareCount(domainResolver.resolveQuestionIndex())
                .setTypes(Types.question)
                .setQuery(createQuery(searchContext));
    }

    private FilteredQueryBuilder createQuery(SearchContext searchContext) {
        FacetBuilder facetBuilder = new FacetBuilder();
        facetBuilder.setDslQuery(searchContext.getDslQuery());
        facetBuilder.addQuestionIds(searchContext.getQuestionIds());
        List<Facet> facets = facetBuilder.build();

        FacetContext facetContext = new FacetContext();
        facetContext.setAllFacets(facets);
        facetContext.setUserContext(searchContext.getUserContext());

        List<FilterBuilder> filters = new ArrayList<>();
        List<QueryBuilder> queries = new ArrayList<>();

        facets.forEach(facet -> {
            facet.getFilter(facetContext).ifPresent(filters::add);
            facet.getQuery(facetContext).ifPresent(queries::add);
        });

        FilterBuilder filterBuilder = FilterBuilders.matchAllFilter();
        if (!filters.isEmpty()) {
            filterBuilder = new AndFilterBuilder(filters.toArray(new FilterBuilder[filters.size()]));
        }
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        if (!queries.isEmpty()) {
            queryBuilder = QueryBuilders.boolQuery();
            final QueryBuilder finalQueryBuilder = queryBuilder;
            queries.forEach(((BoolQueryBuilder) finalQueryBuilder)::must);
        }

        return QueryBuilders.filteredQuery(queryBuilder, filterBuilder);
    }

    private List<AggregationBuilder> getAggregationBuilders() {
        GlobalBuilder global = AggregationBuilders.global("global");

        TermsBuilder popularTags = AggregationBuilders
                .terms("popularTags")
                .field("tags")
                .size(10);

        global.subAggregation(popularTags);

        return Lists.newArrayList(global);
    }
}
