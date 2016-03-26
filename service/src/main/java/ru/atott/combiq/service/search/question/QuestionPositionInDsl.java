package ru.atott.combiq.service.search.question;

import org.apache.commons.lang3.builder.ToStringBuilder;
import ru.atott.combiq.service.site.UrlResolver;
import ru.atott.combiq.service.bean.Question;
import ru.atott.combiq.service.dsl.DslQuery;

public class QuestionPositionInDsl {

    private long index;

    private long total;

    private Question nextQuestion;

    private Question previousQuestion;

    private DslQuery dslQuery;

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Question getNextQuestion() {
        return nextQuestion;
    }

    public void setNextQuestion(Question nextQuestion) {
        this.nextQuestion = nextQuestion;
    }

    public Question getPreviousQuestion() {
        return previousQuestion;
    }

    public void setPreviousQuestion(Question previousQuestion) {
        this.previousQuestion = previousQuestion;
    }

    public DslQuery getDslQuery() {
        return dslQuery;
    }

    public void setDslQuery(DslQuery dslQuery) {
        this.dslQuery = dslQuery;
    }

    public String resolveNextQuestionUrl(UrlResolver urlResolver) {
        if (nextQuestion == null) {
            return null;
        }

        return urlResolver.getQuestionUrl(nextQuestion,
                "index=" + (index + 1) + "&dsl=" + UrlResolver.encodeUrlComponent(dslQuery.toDsl()));
    }

    public String resolvePreviouesQuestionUrl(UrlResolver urlResolver) {
        if (previousQuestion == null) {
            return null;
        }

        return urlResolver.getQuestionUrl(previousQuestion,
                "index=" + (index - 1) + "&dsl=" + UrlResolver.encodeUrlComponent(dslQuery.toDsl()));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("index", index)
                .append("total", total)
                .append("nextQuestion", nextQuestion)
                .append("previousQuestion", previousQuestion)
                .append("dslQuery", dslQuery)
                .toString();
    }
}
