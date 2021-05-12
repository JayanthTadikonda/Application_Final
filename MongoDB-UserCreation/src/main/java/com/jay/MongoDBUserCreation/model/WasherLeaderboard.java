package com.jay.MongoDBUserCreation.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WasherLeaderboard {

    private String washerName;
    private double waterSavedInLiters;
}
