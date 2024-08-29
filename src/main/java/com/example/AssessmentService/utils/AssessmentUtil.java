package com.example.AssessmentService.utils;

import com.example.AssessmentService.dto.AssessmentDTO;
import com.example.AssessmentService.model.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Data
public class AssessmentUtil {
    private LocalDateTime date = LocalDateTime.now();
    private SetStatus initialStatus = SetStatus.PENDING;

    public Assessment MapToAssessment(AssessmentDTO assessmentDto) {
        Assessment assessment = new Assessment();
        assessment.setSetName(assessmentDto.getSetName());
        assessment.setDomain(assessmentDto.getDomain());
        assessment.setCreatedby(assessmentDto.getCreatedby());
        assessment.setApprovedby(assessmentDto.getApprovedby());
        assessment.setCreateddate(date);
        assessment.setStatus(initialStatus);

        List<SetQuestionMap> setQuestionMaps = assessmentDto.getQuestions().stream()
                .map(questionRequest -> {
                    Question question = new Question();
                    question.setDescription(questionRequest.getDescription());

                    if(questionRequest.getAnswers() != null) {
                        List<Answer> answerList = questionRequest.getAnswers().stream()
                                .map(answerDTO -> {
                                    Answer answer = new Answer();
                                    answer.setQuestion(question);
                                    answer.setValue(answerDTO.getValue());
                                    answer.setSuggestion(answerDTO.getSuggestion());
                                    return answer;
                                })
                                .collect(Collectors.toList());
                        question.setAnswers(answerList);
                    } else {
                        question.setAnswers(new ArrayList<>());
                    }

                    SetQuestionMap setQuestionMap = new SetQuestionMap();
                    setQuestionMap.setAssessment(assessment);
                    setQuestionMap.setQuestion(question);
                    setQuestionMap.setCreatedDate(LocalDateTime.now());

                    return setQuestionMap;
                })
                .collect(Collectors.toList());

        assessment.setSetQuestionMaps(setQuestionMaps);
        return assessment;
    }

    public AssessmentDTO mapToAssessmentDTO(Assessment assessment) {
        AssessmentDTO assessmentDTO = new AssessmentDTO();
        assessmentDTO.setSetName(assessment.getSetName());
        assessmentDTO.setDomain(assessment.getDomain());
        assessmentDTO.setCreatedby(assessment.getCreatedby());
        assessmentDTO.setApprovedby(assessment.getApprovedby());
        // You might want to add more fields here, such as questions
        return assessmentDTO;
    }
}