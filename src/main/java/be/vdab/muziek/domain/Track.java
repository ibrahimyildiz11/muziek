package be.vdab.muziek.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.time.LocalTime;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class Track {
    private String naam;
    private LocalTime tijd;

    public String getNaam() {
        return naam;
    }

    public LocalTime getTijd() {
        return tijd;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Track track && naam.equalsIgnoreCase(track.naam);
    }

    @Override
    public int hashCode() {
        return naam.toUpperCase().hashCode();
    }
}
