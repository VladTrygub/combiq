package ru.atott.combiq.service.dsl;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collections;
import java.util.List;

public class DslQuery {

    private List<DslTag> tags = Collections.emptyList();

    private List<DslTerm> terms = Collections.emptyList();

    private String level;

    private Long minCommentQuantity;

    private String user;

    private Boolean deleted;

    private Boolean favorite;

    public List<DslTag> getTags() {
        return tags;
    }

    public void setTags(List<DslTag> tags) {
        this.tags = tags;
    }

    public List<DslTerm> getTerms() {
        return terms;
    }

    public void setTerms(List<DslTerm> terms) {
        this.terms = terms;
    }

    public String toDsl() {
        DslQuerySerializer serializer = new DslQuerySerializer();
        return serializer.serialize(this);
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Long getMinCommentQuantity() {
        return minCommentQuantity;
    }

    public void setMinCommentQuantity(Long minCommentQuantity) {
        this.minCommentQuantity = minCommentQuantity;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("tags", tags)
                .append("terms", terms)
                .append("level", level)
                .append("minCommentQuantity", minCommentQuantity)
                .append("user", user)
                .append("deleted", deleted)
                .append("favorite", favorite)
                .toString();
    }
}
