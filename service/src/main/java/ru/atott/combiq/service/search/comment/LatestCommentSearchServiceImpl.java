package ru.atott.combiq.service.search.comment;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.lang3.tuple.ImmutablePair;
import org.elasticsearch.common.lang3.tuple.Pair;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
import org.springframework.stereotype.Service;
import ru.atott.combiq.dao.Types;
import ru.atott.combiq.dao.entity.QuestionComment;
import ru.atott.combiq.dao.entity.QuestionEntity;
import ru.atott.combiq.dao.es.NameVersionDomainResolver;
import ru.atott.combiq.service.ServiceException;
import ru.atott.combiq.service.mapper.QuestionMapper;
import ru.atott.combiq.service.markdown.MarkdownService;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class LatestCommentSearchServiceImpl implements LatestCommentSearchService {

    private Logger logger = LoggerFactory.getLogger(LatestCommentSearchServiceImpl.class);

    private DefaultResultMapper defaultResultMapper;

    @Autowired
    private Client client;

    @Autowired
    private NameVersionDomainResolver domainResolver;

    @Autowired
    private MarkdownService markdownService;

    private LoadingCache<Pair<Long, Long>, List<LatestComment>> questionsWithLatestCommentsCache =
            CacheBuilder
                    .newBuilder()
                    .refreshAfterWrite(30, TimeUnit.MINUTES)
                    .build(new CacheLoader<Pair<Long, Long>, List<LatestComment>>() {
                        @Override
                        public List<LatestComment> load(Pair<Long, Long> key) throws Exception {
                            return getLatestComments(key.getLeft().intValue(), key.getRight().intValue());
                        }
                    });

    public LatestCommentSearchServiceImpl() {
        SimpleElasticsearchMappingContext mappingContext = new SimpleElasticsearchMappingContext();
        defaultResultMapper = new DefaultResultMapper(mappingContext);
    }

    @Override
    public List<LatestComment> getLatestComments(int count, int commentLength) {
        QueryBuilder query = QueryBuilders
                .filteredQuery(
                        QueryBuilders.matchAllQuery(),
                        FilterBuilders.existsFilter("comments.id"));

        SearchRequestBuilder requestBuilder = client
                .prepareSearch(domainResolver.resolveQuestionIndex())
                .setTypes(Types.question)
                .setQuery(query)
                .addSort("comments.postDate", SortOrder.DESC)
                .setSize(count);

        org.elasticsearch.action.search.SearchResponse response = requestBuilder.execute().actionGet();

        List<QuestionEntity> entities = defaultResultMapper
                .mapResults(response, QuestionEntity.class, new PageRequest(0, count))
                .getContent();

        QuestionMapper questionMapper = new QuestionMapper();
        return questionMapper
                .toList(entities).stream()
                .map(question -> {
                    QuestionComment lastComment = question.getLastComment();
                    String simplifiedHtml = markdownService.toSimplifiedHtml(null, lastComment.getContent().getMarkdown(), commentLength);

                    LatestComment latestComment = new LatestComment();
                    latestComment.setQuestion(question);
                    latestComment.setCommentSimplifiedHtml(simplifiedHtml);
                    return latestComment;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<LatestComment> get3LatestComments() {
        try {
            return questionsWithLatestCommentsCache.get(new ImmutablePair<>(3L, 240L));
        } catch (ExecutionException e) {
            logger.error(e.getMessage(), e);

            return Collections.emptyList();
        }
    }

    @Override
    public List<LatestComment> get5LatestComments() {
        try {
            return questionsWithLatestCommentsCache.get(new ImmutablePair<>(5L, 80L));
        } catch (ExecutionException e) {
            logger.error(e.getMessage(), e);

            return Collections.emptyList();
        }
    }
}
