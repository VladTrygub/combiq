package ru.atott.combiq.service.dsl;

import org.apache.commons.lang3.StringUtils;

public class DslQuerySerializer {
    public DslQuerySerializer() { }

    public String serialize(DslQuery query) {
        StringBuilder builder = new StringBuilder();

        if (StringUtils.isNoneBlank(query.getLevel())) {
            builder.append("level:").append(query.getLevel());
        }

        query.getTerms().forEach(term -> {
            builder.append(" ");
            builder.append(term.getValue());
        });

        query.getTags().forEach(tag -> {
            builder.append(" ");
            builder.append("[").append(tag.getValue()).append("]");
        });

        if (query.getMinCommentQuantity() != null) {
            builder.append(" ");
            builder.append("comments:").append(query.getMinCommentQuantity());
        }

        if (query.getDeleted() != null) {
            builder.append(" ");
            builder.append("deleted:").append(query.getDeleted().booleanValue());
        }

        if (StringUtils.isNotBlank(query.getUser())) {
            builder.append(" ");
            builder.append("user:").append(query.getUser());
        }

        if (query.getFavorite() != null) {
            builder.append(" ");
            builder.append("favorite:").append(query.getFavorite().booleanValue());
        }

        return builder.toString().trim();
    }
}
