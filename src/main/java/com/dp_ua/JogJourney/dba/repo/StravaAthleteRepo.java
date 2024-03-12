package com.dp_ua.JogJourney.dba.repo;

import com.dp_ua.JogJourney.strava.entity.StravaAthlete;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StravaAthleteRepo extends CrudRepository<StravaAthlete, Long> {
    Optional<StravaAthlete> findByStravaId(long stravaId);

    Optional<StravaAthlete> findByChatId(String chatId);
}
