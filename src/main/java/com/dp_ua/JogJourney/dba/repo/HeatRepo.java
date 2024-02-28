package com.dp_ua.JogJourney.dba.repo;

import com.dp_ua.JogJourney.dba.element.HeatEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeatRepo extends CrudRepository<HeatEntity, Long> {
}
