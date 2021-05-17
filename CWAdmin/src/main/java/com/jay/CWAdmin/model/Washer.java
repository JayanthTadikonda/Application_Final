package com.jay.CWAdmin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Washer {

    private int washerId;
    private String name;
    private String password;
    private List<String> address;
    private List<RatingReview> ratingReviewList;
    private String role;

}
