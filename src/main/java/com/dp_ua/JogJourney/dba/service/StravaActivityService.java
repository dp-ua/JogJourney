package com.dp_ua.JogJourney.dba.service;

import com.dp_ua.JogJourney.dba.repo.StravaActivityRepo;
import com.dp_ua.JogJourney.strava.entity.StravaActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StravaActivityService {
    private final StravaActivityRepo repo;

    @Autowired
    public StravaActivityService(StravaActivityRepo repo) {
        this.repo = repo;
    }

    public StravaActivity save(StravaActivity activity) {
        log.debug("Save activity: " + activity);
        return repo.save(activity);
    }

    public void saveOnlyNew(StravaActivity activity) {
        log.debug("Save activity: " + activity);
        if (repo.findByActivityId(activity.getActivityId()).isEmpty()) {
            repo.save(activity);
        }
    }
}
