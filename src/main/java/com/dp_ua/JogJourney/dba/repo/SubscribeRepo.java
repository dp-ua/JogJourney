package com.dp_ua.JogJourney.dba.repo;

import com.dp_ua.JogJourney.dba.element.SubscribeEntity;
import com.dp_ua.JogJourney.dba.element.SubscribeEntity.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SubscribeRepo extends CrudRepository<SubscribeEntity, Long> {
    Optional<SubscribeEntity> findByChatId(String chatId);

    List<SubscribeEntity> findAllByGroupIdAndType(String groupId, Status status);
}
