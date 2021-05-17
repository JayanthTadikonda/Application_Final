package com.jay.CWAdmin.repository;

import com.jay.CWAdmin.model.WasherLeaderboard;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WasherLeaderboardRepository extends MongoRepository<WasherLeaderboard, Integer> {

}
