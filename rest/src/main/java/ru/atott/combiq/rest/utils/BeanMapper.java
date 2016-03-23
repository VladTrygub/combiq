package ru.atott.combiq.rest.utils;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface BeanMapper<S, D> {

    D map(RestContext restContext, S source);

    default D safeMap(RestContext restContext, S source) {
        if (source == null) {
            return null;
        }

        return map(restContext, source);
    }

    default List<D> toList(RestContext restContext, Collection<S> source) {
        return source.stream()
                .map(item -> safeMap(restContext, item))
                .collect(Collectors.toList());
    }

    default List<D> toList(RestContext restContext, Iterable<S> source) {
        return toList(restContext, Lists.newArrayList(source));
    }
}
