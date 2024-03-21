package com.dp_ua.JogJourney.dba.element;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
public class SubscribeEntity extends DomainElement {
    private String chatId;
    private String groupId;
    private Status type;

    public enum Status {
        ACTIVE, PAUSED, DELETED
    }
}
