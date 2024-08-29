package com.example.AssessmentService.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setid")
    private Long setid;

    @Column(name = "set_name", unique = true)
    private String setName;

    private String domain;

    private String createdby;

    private String approvedby;

    private LocalDateTime createddate;

    private SetStatus status;

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL)
    private List<SetQuestionMap> setQuestionMaps;

    // Method to get questions
    @Transient
    public List<Question> getQuestions() {
        return setQuestionMaps.stream()
                .map(SetQuestionMap::getQuestion)
                .toList();
    }
}