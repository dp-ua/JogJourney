package com.dp_ua.JogJourney.strava.entity;

import com.dp_ua.JogJourney.dba.element.DomainElement;
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
public class StravaLog extends DomainElement {
    private String chatId;
    private StravaLogType type;
    private String details;

    public enum StravaLogType {
        ACTIVITIES
    }
}
