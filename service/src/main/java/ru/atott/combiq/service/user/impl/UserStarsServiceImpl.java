package ru.atott.combiq.service.user.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.atott.combiq.dao.entity.QuestionEntity;
import ru.atott.combiq.dao.entity.UserEntity;
import ru.atott.combiq.dao.repository.QuestionRepository;
import ru.atott.combiq.dao.repository.UserRepository;
import ru.atott.combiq.service.site.UserContext;
import ru.atott.combiq.service.user.UserStarsService;

import java.util.*;

@Service
public class UserStarsServiceImpl implements UserStarsService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void like(UserContext uc, String questionId) {
        Validate.isTrue(!uc.isAnonimous());

        UserEntity user = userRepository.findOne(uc.getUserId());
        Set<String> questionsSet = user.getFavoriteQuestions();
        if (questionsSet == null) {
            questionsSet = new HashSet<>();
        }
        questionsSet = new HashSet<>(questionsSet);
        if (!questionsSet.contains(questionId)) {
            questionsSet.add(questionId);
            user.setFavoriteQuestions(questionsSet);
            userRepository.save(user);
            QuestionEntity questionEntity = questionRepository.findOne(questionId);
            questionEntity.setStars(questionEntity.getStars() + 1);
            questionRepository.save(questionEntity);
        }
    }

    @Override
    public void dislike(UserContext uc, String questionId) {
        Validate.isTrue(!uc.isAnonimous());

        UserEntity user = userRepository.findOne(uc.getUserId());
        Set<String> questionsSet = user.getFavoriteQuestions();
        if (questionsSet == null) {
            questionsSet = new HashSet<>();
        }
        questionsSet = new HashSet<>(questionsSet);
        if (questionsSet.contains(questionId)) {
            questionsSet.remove(questionId);
            user.setFavoriteQuestions(questionsSet);
            userRepository.save(user);
            QuestionEntity questionEntity = questionRepository.findOne(questionId);
            questionEntity.setStars(Math.max(0, questionEntity.getStars() - 1));
            questionRepository.save(questionEntity);
        }
    }

    @Override
    public Set<String> getFavoriteQuestions(UserContext uc) {
        if (uc.isAnonimous()) {
            return Collections.emptySet();
        }

        Set<String> starsQuestion = userRepository.findOne(uc.getUserId()).getFavoriteQuestions();
        starsQuestion = starsQuestion == null ? new HashSet<>() : starsQuestion;
        return starsQuestion;
    }

    @Override
    public boolean isFavoriteQuestion(UserContext uc, String questionId) {
        if (uc.isAnonimous()) {
            return false;
        }

        UserEntity user = userRepository.findOne(uc.getUserId());
        Set<String> questionsSet = user.getFavoriteQuestions();

        if (CollectionUtils.isEmpty(questionsSet)) {
            return false;
        }

        return questionsSet.contains(questionId);
    }

    @Override
    public void addAskedCount(UserContext uc, String questionId) {
        Validate.isTrue(!uc.isAnonimous());

        UserEntity user = userRepository.findOne(uc.getUserId());
        Set<String> questionsAskedSet = user.getAskedQuestions();
        if (questionsAskedSet == null) {
            questionsAskedSet = new HashSet<>();
        }
        questionsAskedSet = new HashSet<>(questionsAskedSet);
        if (!questionsAskedSet.contains(questionId)) {
            questionsAskedSet.add(questionId);
            user.setAskedQuestions(questionsAskedSet);
            userRepository.save(user);
            QuestionEntity questionEntity = questionRepository.findOne(questionId);
            questionEntity.setAskedToday(questionEntity.getAskedToday() + 1);
            questionRepository.save(questionEntity);
        }
    }

    @Override
    public boolean isAskedQuestion(UserContext uc, String questionId) {
        if (uc.isAnonimous()) {
            return false;
        }

        UserEntity user = userRepository.findOne(uc.getUserId());
        Set<String> questionsAskedSet = user.getAskedQuestions();

        if (CollectionUtils.isEmpty(questionsAskedSet)) {
            return false;
        }

        return questionsAskedSet.contains(questionId);
    }

    @Override
    public void recount(){
        HashMap<String, Integer> likeCounter = new HashMap<>();
        questionRepository.findAll().forEach(x->likeCounter.put(x.getId(), 0));
        HashMap<String, Integer> starCounter = (HashMap<String, Integer>) likeCounter.clone();
        userRepository.findAll().iterator().forEachRemaining(x-> {
            x.getAskedQuestions().iterator()
                    .forEachRemaining(y-> likeCounter.put(y, likeCounter.get(y) + 1));
            x.getFavoriteQuestions().iterator()
                    .forEachRemaining(y-> starCounter.put(y, likeCounter.get(y) + 1));
        });
        likeCounter.keySet().forEach(x-> {
            QuestionEntity question = questionRepository.findOne(x);
            question.setAskedCount(likeCounter.get(x));
            question.setStars(starCounter.get(x));
            question.setAskedToday(0);
            questionRepository.save(question);
        });


    }
}
