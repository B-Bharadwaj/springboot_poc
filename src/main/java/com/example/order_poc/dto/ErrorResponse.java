package com.example.order_poc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

// DTO: error JSON returned for every failed request.
// Leaves out null fields, so fieldErrors appears only when set.
@JsonInclude(JsonInclude.Include.NON_NULL)
// Lombok: generates getters.
@Getter
// Lombok: constructor with all fields, in field order.
@AllArgsConstructor
public class ErrorResponse {

    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldErrorDetail> fieldErrors;
}
