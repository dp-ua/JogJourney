package com.dp_ua.JogJourney.dba.element;

import com.dp_ua.JogJourney.dba.element.DomainElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class StravaAthlete extends DomainElement {
    @JsonProperty("id")
    private long stravaId;
    private String chatId;
    private String username;
    private String firstname;
    private String lastname;
    private String sex;
    private String city;
    private String country;
    private String profile;
    @JsonProperty("created_at")
    private String stravaCreatedAt;
    @JsonProperty("updated_at")
    private String stravaUpdatedAt;

    @Override
    public String toString() {
        return "StravaAthlete{" +
                "stravaId=" + stravaId +
                ", chatId='" + chatId + '\'' +
                ", username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", sex='" + sex + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", profile='" + profile + '\'' +
                ", stravaCreatedAt='" + stravaCreatedAt + '\'' +
                ", stravaUpdatedAt='" + stravaUpdatedAt + '\'' +
                ", id=" + id +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
