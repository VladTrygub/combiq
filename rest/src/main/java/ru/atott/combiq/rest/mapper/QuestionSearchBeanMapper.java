package ru.atott.combiq.rest.mapper;

import ru.atott.combiq.rest.bean.QuestionSearchBean;
import ru.atott.combiq.rest.utils.BeanMapper;
import ru.atott.combiq.rest.utils.RestContext;
import ru.atott.combiq.service.search.question.SearchResponse;

public class QuestionSearchBeanMapper implements BeanMapper<SearchResponse, QuestionSearchBean> {

    @Override
    public QuestionSearchBean map(RestContext restContext, SearchResponse source) {
        QuestionBeanMapper questionBeanMapper = new QuestionBeanMapper();

        QuestionSearchBean bean = new QuestionSearchBean();
        bean.setDsl(source.getDslQuery().toDsl());
        bean.setPage(source.getQuestions().getNumber());
        bean.setPageSize(source.getQuestions().getSize());
        bean.setQuestions(questionBeanMapper.toList(restContext, source.getQuestions()));
        bean.setTotal(source.getQuestions().getTotalElements());

        return bean;
    }
}
