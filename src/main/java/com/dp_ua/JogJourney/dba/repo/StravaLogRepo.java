package com.dp_ua.JogJourney.dba.repo;

import com.dp_ua.JogJourney.strava.entity.StravaLog;
import org.springframework.data.repository.CrudRepository;

public interface StravaLogRepo extends CrudRepository<StravaLog, Long> {
}
