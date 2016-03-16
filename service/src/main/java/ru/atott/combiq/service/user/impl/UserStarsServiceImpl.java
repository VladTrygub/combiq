package ru.atott.combiq.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.atott.combiq.dao.entity.QuestionEntity;
import ru.atott.combiq.dao.entity.UserEntity;
import ru.atott.combiq.dao.repository.QuestionRepository;
import ru.atott.combiq.dao.repository.UserRepository;
import ru.atott.combiq.service.mapper.QuestionMapper;
import ru.atott.combiq.service.user.UserStarsService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class UserStarsServiceImpl implements UserStarsService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;


    private QuestionMapper maper = new QuestionMapper();

    @Override
    public void like(String userId, String questionId) {
        UserEntity user=userRepository.findOne(userId);
        List<String> questionslist = user.getFavoriteQuestions();
        if(questionslist == null){
            questionslist = new ArrayList<String>();
        }
        if (!questionslist.contains(questionId)){
            questionslist.add(questionId);
            user.setFavoriteQuestions(questionslist);
            userRepository.save(user);
            QuestionEntity questionEntity = questionRepository.findOne(questionId);
            questionEntity.setStars(questionEntity.getStars() + 1);
            questionRepository.save(questionEntity);
        }
    }

    @Override
    public void dislike(String userId, String questionId) {
        UserEntity user=userRepository.findOne(userId);
        List<String> questionslist = user.getFavoriteQuestions();
        if(questionslist == null){
            questionslist = new ArrayList<String>();
        }
        if (questionslist.contains(questionId)){
            questionslist.remove(questionId);
            userRepository.save(user);
            QuestionEntity questionEntity = questionRepository.findOne(questionId);
            questionEntity.setStars(questionEntity.getStars() - 1);
            questionRepository.save(questionEntity);
        }
    }

    @Override
    public List<String> starsQuestions(String userId) {
        List<String> starsQuestion = userRepository.findOne(userId).getFavoriteQuestions();
        starsQuestion = starsQuestion == null ? new LinkedList<>() : starsQuestion;
        return starsQuestion;
    }
}
