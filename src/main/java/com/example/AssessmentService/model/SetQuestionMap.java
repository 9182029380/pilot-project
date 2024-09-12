package com.example.AssessmentService.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "set_question_map")
public class SetQuestionMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "set_id")
    @JsonBackReference
    private Assessment assessment;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonBackReference
    private Question question;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    // You can add a pre-persist method to automatically set the created date
    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }
}
