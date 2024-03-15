package com.dp_ua.JogJourney.dba.service;

import com.dp_ua.JogJourney.dba.repo.StravaLogRepo;
import com.dp_ua.JogJourney.strava.entity.StravaLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StravaLogService {
    private final StravaLogRepo repo;

    @Autowired
    public StravaLogService(StravaLogRepo repo) {
        this.repo = repo;
    }

    public void save(StravaLog info) {
        log.debug("Save log: " + info);
        repo.save(info);
    }
}
