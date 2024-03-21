package com.dp_ua.JogJourney.dba.service;

import com.dp_ua.JogJourney.dba.element.StravaActivity;
import com.dp_ua.JogJourney.dba.repo.StravaActivityRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

    public void saveOrUpdate(StravaActivity activity) {
        log.debug("Save activity: " + activity);
        Optional<StravaActivity> byActivityId = repo.findByActivityId(activity.getActivityId());
        if (byActivityId.isPresent()) {
            StravaActivity dbActivity = byActivityId.get();
            dbActivity.applyChanges(activity);
            repo.save(dbActivity);
        }
        if (byActivityId.isEmpty()) {
            repo.save(activity);
        }
    }
}
