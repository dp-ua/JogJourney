package com.dp_ua.JogJourney.dba.repo;

import com.dp_ua.JogJourney.dba.element.UpdateStatusEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpdateStatusRepo extends CrudRepository<UpdateStatusEntity, Long> {
    List<UpdateStatusEntity> findAllByCompetitionIdAndStatus(long competitionId, String name);

    void flush();
}
