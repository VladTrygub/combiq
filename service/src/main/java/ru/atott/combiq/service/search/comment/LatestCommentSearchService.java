package ru.atott.combiq.service.search.comment;

import ru.atott.combiq.service.bean.Question;

import java.util.List;

public interface LatestCommentSearchService {

    List<LatestComment> getLatestComments(int count, int commentLength);

    List<LatestComment> get3LatestComments();

    List<LatestComment> get5LatestComments();
}
