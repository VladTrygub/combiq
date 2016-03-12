package ru.atott.combiq.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import ru.atott.combiq.dao.entity.QuestionEntity;
import ru.atott.combiq.dao.entity.UserEntity;
import ru.atott.combiq.dao.repository.QuestionRepository;
import ru.atott.combiq.dao.repository.UserRepository;
import ru.atott.combiq.service.bean.Question;
import ru.atott.combiq.service.mapper.QuestionMapper;
import ru.atott.combiq.service.user.UserStars;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Леонид on 11.03.2016.
 */
public class UserStarsImp implements UserStars {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionMapper maper;

    @Override
    public void like(String userId, String questionId) {
        UserEntity user=userRepository.findOne(userId);
        List<String> questionslist=user.getQuestions();
        if(questionslist==null){questionslist=new ArrayList<String>();}
        if (!questionslist.contains(questionId)){
            questionslist.add(questionId);
            QuestionEntity questionEntity=questionRepository.findOne(questionId);
            questionEntity.setStars(questionEntity.getStars()+1);
            questionRepository.save(questionEntity);
        }
    }

    @Override
    public void dislike(String userId, String questionId) {
        UserEntity user=userRepository.findOne(userId);
        List<String> questionslist=user.getQuestions();
        if(questionslist==null){questionslist=new ArrayList<String>();}
        if (questionslist.contains(questionId)){
            questionslist.remove(questionId);
            QuestionEntity questionEntity=questionRepository.findOne(questionId);
            questionEntity.setStars(questionEntity.getStars()-1);
            questionRepository.save(questionEntity);
        }
    }

    @Override
    public List<Question> getAll(String userId) {
        List<Question> questionslist=new LinkedList<Question>();
        UserEntity user=userRepository.findOne(userId);
        if(user!=null && user.getQuestions()!=null){
            questionRepository.findAll(user.getQuestions()).forEach(x->questionslist.add(maper.map(x)));
        }
        return questionslist;
    }
}
