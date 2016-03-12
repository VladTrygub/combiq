package ru.atott.combiq.service.user;

import ru.atott.combiq.service.bean.Question;

import java.util.List;

/**
 * Created by Леонид on 11.03.2016.
 */
public interface UserStars {

    public void like(String userId, String questionId);

    public void dislike(String userId, String questionId);

    public List<Question> getAll(String userId);
}
