package ru.atott.combiq.service.question;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.global.GlobalBuilder;
import org.elasticsearch.search.aggregations.bucket.global.InternalGlobal;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.atott.combiq.dao.Types;
import ru.atott.combiq.dao.entity.TagEntity;
import ru.atott.combiq.dao.es.NameVersionDomainResolver;
import ru.atott.combiq.dao.repository.TagRepository;
import ru.atott.combiq.service.bean.DetailedQuestionTag;
import ru.atott.combiq.service.bean.Question;
import ru.atott.combiq.service.bean.QuestionTag;
import ru.atott.combiq.service.bean.Tag;
import ru.atott.combiq.service.mapper.QuestionTagMapper;
import ru.atott.combiq.service.question.TagService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private Client client;

    @Autowired
    private NameVersionDomainResolver domainResolver;

    @Override
    public List<QuestionTag> getTags(List<String> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            return Collections.emptyList();
        }

        Iterable<TagEntity> tagEntities = tagRepository.findAll(tags);
        QuestionTagMapper mapper = new QuestionTagMapper();
        return mapper.toList(tagEntities);
    }

    @Override
    public Map<String, QuestionTag> getTagsMap(List<String> tags) {
        return getTags(tags).stream()
                .collect(Collectors.toMap(QuestionTag::getTag, tag -> tag));
    }

    @Override
    public List<QuestionTag> getTags() {
        Iterable<TagEntity> tagEntities = tagRepository.findAll();

        QuestionTagMapper mapper = new QuestionTagMapper();
        return mapper.toList(tagEntities);
    }

    @Override
    public List<DetailedQuestionTag> getAllQuestionTags() {
        GlobalBuilder global = AggregationBuilders.global("global");

        TermsBuilder popularTags = AggregationBuilders
                .terms("popularTags")
                .field("tags")
                .size(Integer.MAX_VALUE);

        global.subAggregation(popularTags);

        SearchRequestBuilder query = client
                .prepareSearch(domainResolver.resolveQuestionIndex())
                .setTypes(Types.question)
                .setQuery(QueryBuilders.matchAllQuery())
                .setSize(0)
                .addAggregation(global);

        org.elasticsearch.action.search.SearchResponse searchResponse = query.execute().actionGet();

        InternalGlobal internalGlobal = searchResponse.getAggregations().get("global");

        StringTerms popularTagsAgg = internalGlobal.getAggregations().get("popularTags");
        List<DetailedQuestionTag> result = popularTagsAgg.getBuckets().stream()
                .map(bucket -> {
                    String tagValue = bucket.getKey();
                    long count = bucket.getDocCount();
                    return new DetailedQuestionTag(tagValue, count);
                })
                .collect(Collectors.toList());

        List<String> tags = result.stream()
                .map(Tag::getValue)
                .collect(Collectors.toList());

        Map<String, QuestionTag> tagsMap = this.getTagsMap(tags);

        result.forEach(tag -> {
            tag.setDetails(tagsMap.get(tag.getValue()));
        });

        return result;
    }

    @Override
    public QuestionTag getTag(String name){
        TagEntity tagEntity = tagRepository.findOne(name);

        if (tagEntity == null) {
            QuestionTag questionTag = new QuestionTag();
            questionTag.setTag(name);
            return questionTag;
        }

        return new QuestionTagMapper().map(tagEntity);
    }

    @Override
    public void save(QuestionTag tag) {
        TagEntity tagEntity = tagRepository.findOne(tag.getTag());

        if (tagEntity == null) {
            tagEntity = new TagEntity();
            tagEntity.setName(tag.getTag());
        }

        tagEntity.setSuggestViewOthersQuestionsLabel(tag.getSuggestViewOthersQuestionsLabel());
        tagEntity.setDescription(tag.getDescription());

        tagRepository.save(tagEntity);

    }
}
