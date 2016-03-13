package ru.atott.combiq.web.view;

import org.springframework.web.servlet.ModelAndView;
import ru.atott.combiq.service.bean.Question;
import ru.atott.combiq.service.bean.Tag;
import ru.atott.combiq.service.dsl.DslQuery;
import ru.atott.combiq.web.bean.PagingBean;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SearchViewBuilder {
    private List<Question> questions;
    private List<Tag> popularTags;
    private PagingBean paging;
    private String dsl;
    private String subTitle;
    private boolean questionsCatalog;
    private DslQuery dslQuery;
    private Long questionsCount;

    public Long getQuestionsCount() {
        return questionsCount;
    }

    public void setQuestionsCount(Long questionsCount) {
        this.questionsCount = questionsCount;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public PagingBean getPaging() {
        return paging;
    }

    public void setPaging(PagingBean paging) {
        this.paging = paging;
    }

    public String getDsl() {
        return dsl;
    }

    public void setDsl(String dsl) {
        this.dsl = dsl;
    }

    public List<Tag> getPopularTags() {
        return popularTags;
    }

    public void setPopularTags(List<Tag> popularTags) {
        this.popularTags = popularTags;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public boolean isQuestionsCatalog() {
        return questionsCatalog;
    }

    public void setQuestionsCatalog(boolean questionsCatalog) {
        this.questionsCatalog = questionsCatalog;
    }

    public DslQuery getDslQuery() {
        return dslQuery;
    }

    public void setDslQuery(DslQuery dslQuery) {
        this.dslQuery = dslQuery;
    }

    public ModelAndView build() {
        ModelAndView mav = new ModelAndView("search");
        mav.addObject("questions", questions);
        mav.addObject("paging", paging);
        mav.addObject("dsl", dsl);
        mav.addObject("popularTags", popularTags);
        mav.addObject("subTitle", subTitle);
        mav.addObject("questionsCatalog", questionsCatalog);
        mav.addObject("showMatrixCompetenceAdvice", dslQuery != null && isNotBlank(dslQuery.getLevel()));
        mav.addObject("searchOnlyWithComments", dslQuery != null
                && dslQuery.getMinCommentQuantity() != null
                && dslQuery.getMinCommentQuantity() > 0);
        mav.addObject("questionsCount",questionsCount);
        return mav;
    }
}
