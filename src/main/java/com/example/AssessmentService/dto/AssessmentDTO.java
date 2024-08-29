package com.example.AssessmentService.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AssessmentDTO {
    private String setName;
    private String domain;
    private String createdby;
    private String approvedby;
    private List<QuestionRequest> questions;
}
