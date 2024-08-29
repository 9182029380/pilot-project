package com.example.AssessmentService.service;

import com.example.AssessmentService.dto.*;
import com.example.AssessmentService.exception.ResourceNotFoundException;
import com.example.AssessmentService.model.*;
import com.example.AssessmentService.repo.*;
import com.example.AssessmentService.utils.AssessmentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SetQuestionMapRepository setQuestionMapRepository;

    @Autowired
    AssessmentUtil assessmentUtil;

    @Autowired
    private AnswerRepository answerRepository;

    private final String setNameIsInvalid = "Set name is invalid";
    private final String questionIdIsInvalid = "Question id is invalid";

    @Transactional
    public Assessment createAssessment(AssessmentDTO assessmentRequest) {
        Assessment assessment = assessmentUtil.MapToAssessment(assessmentRequest);
        assessment = assessmentRepository.save(assessment);

        for (SetQuestionMap sqm : assessment.getSetQuestionMaps()) {
            Question question = questionRepository.save(sqm.getQuestion());
            sqm.setQuestion(question);
            setQuestionMapRepository.save(sqm);
        }

        return assessment;
    }

    public List<Assessment> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    @Transactional
    public String updateQuestion(Long setId, Long questionId, List<AnswerDTO> answerDtos) {
        Assessment assessment = assessmentRepository.findById(setId)
                .orElseThrow(() -> new ResourceNotFoundException(setNameIsInvalid));

        SetQuestionMap setQuestionMapToUpdate = assessment.getSetQuestionMaps().stream()
                .filter(sqm -> sqm.getQuestion().getQuestionId() == questionId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(questionIdIsInvalid));

        Question questionToUpdate = setQuestionMapToUpdate.getQuestion();

        if (answerDtos != null && !answerDtos.isEmpty()) {
            List<Answer> answers = answerDtos.stream()
                    .map(answerDto -> {
                        Answer answer = new Answer();
                        answer.setValue(answerDto.getValue());
                        answer.setSuggestion(answerDto.getSuggestion());
                        answer.setQuestion(questionToUpdate);
                        return answer;
                    })
                    .collect(Collectors.toList());

            List<Answer> existingAnswers = questionToUpdate.getAnswers();
            if (existingAnswers == null) {
                existingAnswers = new ArrayList<>();
            }

            existingAnswers.addAll(answers);
            questionToUpdate.setAnswers(existingAnswers);
        }

        questionRepository.save(questionToUpdate);
        setQuestionMapRepository.save(setQuestionMapToUpdate);

        return "Question updated successfully";
    }

    @Transactional
    public Map<String, String> deleteQuestion(long setId, Long questionId) {
        Map<String, String> response = new HashMap<>();
        Assessment assessment = assessmentRepository.findById(setId)
                .orElseThrow(() -> new ResourceNotFoundException(setNameIsInvalid));

        SetQuestionMap setQuestionMapToDelete = assessment.getSetQuestionMaps().stream()
                .filter(sqm -> sqm.getQuestion().getQuestionId() == questionId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Question is not found"));

        assessment.getSetQuestionMaps().remove(setQuestionMapToDelete);
        setQuestionMapRepository.delete(setQuestionMapToDelete);
        questionRepository.deleteById(questionId);
        assessmentRepository.save(assessment);

        response.put("message", "Question deleted successfully");
        return response;
    }

    public List<Question> getQuestionsSetName(String setName) {
        Assessment assessment = assessmentRepository.findBySetName(setName)
                .orElseThrow(() -> new ResourceNotFoundException("Set name is invalid"));
        return assessment.getSetQuestionMaps().stream()
                .map(SetQuestionMap::getQuestion)
                .collect(Collectors.toList());
    }

    public Optional<Question> fetchQuestion(Long qid) {
        return questionRepository.findById(qid);
    }

    public List<Question> getQuestionsSetId(long setId) {
        Assessment assessment = assessmentRepository.findById(setId)
                .orElseThrow(() -> new ResourceNotFoundException("Set id is invalid"));
        return assessment.getSetQuestionMaps().stream()
                .map(SetQuestionMap::getQuestion)
                .collect(Collectors.toList());
    }
}