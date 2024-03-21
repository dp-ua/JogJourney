package com.dp_ua.JogJourney.dba.service;

import com.dp_ua.JogJourney.dba.repo.StravaTokenRepo;
import com.dp_ua.JogJourney.dba.element.StravaToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class StravaTokenService {
    private final StravaTokenRepo repo;

    @Autowired
    public StravaTokenService(StravaTokenRepo repo) {
        this.repo = repo;
    }

    public Optional<StravaToken> findByStravaId(long stravaId) {
        return repo.findByStravaId(stravaId).stream().reduce((first, second) -> second);
    }

    public void saveOrUpdate(StravaToken token) {
        log.debug("save token: stravaId={}", token.getStravaId());
        findByStravaId(token.getStravaId()).ifPresent(repo::delete);
        repo.save(token);
    }
}
