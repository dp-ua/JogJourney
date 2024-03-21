package com.dp_ua.JogJourney.dba.repo;

import com.dp_ua.JogJourney.dba.element.StravaActivity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StravaActivityRepo extends CrudRepository<StravaActivity, Long> {

    Optional<StravaActivity> findByActivityId(long activityId);
}
