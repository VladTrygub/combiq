package ru.atott.combiq.service.user;

import ru.atott.combiq.service.bean.Question;

import java.util.List;
import java.util.stream.Stream;


public interface UserStarsService {

    public void like(String userId, String questionId);

    public void dislike(String userId, String questionId);

    public List<String> starsQuestions(String userId);
}
