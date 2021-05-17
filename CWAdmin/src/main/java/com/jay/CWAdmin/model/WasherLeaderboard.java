package com.jay.CWAdmin.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "washerleaderboard")
public class WasherLeaderboard {

    private String washerName;

    private double waterSavedInLiters;
}
