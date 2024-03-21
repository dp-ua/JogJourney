package com.dp_ua.JogJourney.dba.repo;

import com.dp_ua.JogJourney.dba.element.StravaToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StravaTokenRepo extends CrudRepository<StravaToken, Long> {
    Optional<StravaToken> findByStravaId(long stravaId);
}
