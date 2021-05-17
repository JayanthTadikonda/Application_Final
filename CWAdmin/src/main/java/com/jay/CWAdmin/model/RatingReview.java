package com.jay.CWAdmin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingReview {

    @NotEmpty(message = "please review the washer, review can't be empty!")
    private String review;
    private int rating;
}
