package com.dp_ua.JogJourney.dba.service;

import com.dp_ua.JogJourney.dba.repo.StravaAthleteRepo;
import com.dp_ua.JogJourney.dba.element.StravaAthlete;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Comparator;
import java.util.Optional;

@Slf4j
@Component
public class StravaAthleteService {
    private final StravaAthleteRepo repo;

    @Autowired
    public StravaAthleteService(StravaAthleteRepo repo) {
        this.repo = repo;
    }

    public Optional<StravaAthlete> findByChatId(String chatId) {
        return repo.findByChatId(chatId).stream().max(Comparator.comparing(StravaAthlete::getUpdated));
    }

    public Optional<StravaAthlete> findByStravaId(long stravaId) {
        return repo.findByStravaId(stravaId).stream().max(Comparator.comparing(StravaAthlete::getUpdated));
    }

    public StravaAthlete save(StravaAthlete athlete) {
        log.debug("save athlete: stravaId={}", athlete.getStravaId());
        return findByStravaId(athlete.getStravaId()).map(dbAthlete -> {
            if (Instant.parse(dbAthlete.getStravaUpdatedAt()).isBefore(Instant.parse(athlete.getStravaUpdatedAt()))) {
                return repo.save(athlete);
            }
            return dbAthlete;
        }).orElseGet(() -> repo.save(athlete));
    }
}
