package com.dp_ua.JogJourney.dba.service;

import com.dp_ua.JogJourney.dba.element.DayEntity;
import com.dp_ua.JogJourney.dba.repo.DayRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class DayService {
    private final DayRepo repo;

    @Autowired
    public DayService(DayRepo repo) {
        this.repo = repo;
    }

    public DayEntity save(DayEntity day) {
        return repo.save(day);
    }
}
