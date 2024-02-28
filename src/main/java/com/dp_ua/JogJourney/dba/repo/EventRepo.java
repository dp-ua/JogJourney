package com.dp_ua.JogJourney.dba.repo;

import com.dp_ua.JogJourney.dba.element.EventEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepo extends CrudRepository<EventEntity, Long> {
    List<EventEntity> findAll();
}
