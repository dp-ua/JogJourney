package com.dp_ua.JogJourney.dba.service;

import com.dp_ua.JogJourney.dba.element.SubscribeEntity;
import com.dp_ua.JogJourney.dba.repo.SubscribeRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

import static com.dp_ua.JogJourney.dba.element.SubscribeEntity.Status.ACTIVE;

@Component
@Slf4j
public class SubscribeService {
    private final SubscribeRepo repo;

    @Autowired
    public SubscribeService(SubscribeRepo repo) {
        this.repo = repo;
    }

    public void save(SubscribeEntity info) {
        log.debug("Save subscribe: " + info);
        repo.save(info);
    }

    public List<String> getUniqueSubscribersChatIds() {
        Iterable<SubscribeEntity> all = repo.findAll();
        return StreamSupport.stream(all.spliterator(), false)
                .filter(sub -> sub.getType() == ACTIVE)
                .map(SubscribeEntity::getChatId)
                .distinct()
                .toList();
    }

    public List<SubscribeEntity> getAllSubscribersForGroup(String groupId) {
        return repo.findAllByGroupIdAndType(groupId, ACTIVE);
    }
}
