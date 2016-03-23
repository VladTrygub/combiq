package ru.atott.combiq.service.search.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.atott.combiq.service.dsl.DslParser;
import ru.atott.combiq.service.dsl.DslQuery;
import ru.atott.combiq.service.dsl.DslTag;
import ru.atott.combiq.service.site.UserContextProvider;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SearchContextFactory {

    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 50;

    @Autowired
    private UserContextProvider userContextProvider;

    public SearchContext list(int page) {
        SearchContext context = new SearchContext();
        context.setUserContext(userContextProvider.getUserContext());
        context.setFrom(page * DEFAULT_SIZE);
        context.setSize(DEFAULT_SIZE);
        context.setDslQuery(new DslQuery());
        context.setUserId(context.getUserContext().getUserId());
        return context;
    }

    public SearchContext listByTags(int page, List<String> tags) {
        DslQuery query = new DslQuery();
        query.setTags(tags.stream().map(DslTag::new).collect(Collectors.toList()));
        SearchContext context = new SearchContext();
        context.setUserContext(userContextProvider.getUserContext());
        context.setFrom(page * DEFAULT_SIZE);
        context.setSize(DEFAULT_SIZE);
        context.setDslQuery(query);
        context.setUserId(context.getUserContext().getUserId());
        return context;
    }

    public SearchContext listByLevel(int page, String level) {
        DslQuery query = new DslQuery();
        query.setLevel(level);

        SearchContext context = new SearchContext();
        context.setUserContext(userContextProvider.getUserContext());
        context.setFrom(page * DEFAULT_SIZE);
        context.setSize(DEFAULT_SIZE);
        context.setDslQuery(query);
        context.setUserId(context.getUserContext().getUserId());
        return context;
    }

    public SearchContext listByDsl(int page, String dsl) {
        return listByDsl(page, DEFAULT_SIZE, dsl);
    }

    public SearchContext listByDsl(int page, int size, String dsl) {
        DslQuery dslQuery = DslParser.parse(dsl);
        size = Math.min(size, MAX_SIZE);

        SearchContext context = new SearchContext();
        context.setUserContext(userContextProvider.getUserContext());
        context.setFrom(page * size);
        context.setSize(size);
        context.setDslQuery(dslQuery);
        context.setUserId(context.getUserContext().getUserId());
        return context;
    }

    public SearchContext listByDeleted(int page, boolean deleted) {
        DslQuery query = new DslQuery();
        query.setDeleted(deleted);

        SearchContext context = new SearchContext();
        context.setUserContext(userContextProvider.getUserContext());
        context.setFrom(page * DEFAULT_SIZE);
        context.setSize(DEFAULT_SIZE);
        context.setDslQuery(query);
        context.setUserId(context.getUserContext().getUserId());
        return context;
    }

    public SearchContext listByUser(int page, String userId) {
        DslQuery query = new DslQuery();
        query.setUser(userId);

        SearchContext context = new SearchContext();
        context.setUserContext(userContextProvider.getUserContext());
        context.setFrom(page * DEFAULT_SIZE);
        context.setSize(DEFAULT_SIZE);
        context.setDslQuery(query);
        context.setUserId(context.getUserContext().getUserId());
        return context;
    }
}
