package com.dp_ua.JogJourney.dba.element;

import com.dp_ua.JogJourney.strava.entity.StravaActivityResponse;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
public class StravaActivity extends DomainElement {
    private long athleteId;
    private long activityId;
    private String name;
    private double distance;
    private int movingTime;
    private int elapsedTime;
    private String type;
    private String sportType;
    private String startDate;

    public StravaActivity(StravaActivityResponse response) {
        this.athleteId = response.getAthlete().getStravaId();
        this.activityId = response.getId();
        this.name = response.getName();
        this.distance = response.getDistance();
        this.movingTime = response.getMovingTime();
        this.elapsedTime = response.getElapsedTime();
        this.type = response.getType();
        this.sportType = response.getSportType();
        this.startDate = response.getStartDate();
    }

    public void applyChanges(StravaActivity activity) {
        this.name = activity.getName();
        this.distance = activity.getDistance();
        this.movingTime = activity.getMovingTime();
        this.elapsedTime = activity.getElapsedTime();
        this.type = activity.getType();
        this.sportType = activity.getSportType();
        this.startDate = activity.getStartDate();
    }
}