package com.example.slabiak.appointmentscheduler.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class TimePeroid implements Comparable<TimePeroid> {
    @JsonDeserialize(using = LocalTimeArrayDeserializer.class)
    private LocalTime start;
    @JsonDeserialize(using = LocalTimeArrayDeserializer.class)
    private LocalTime end;

    public TimePeroid() {

    }

    public TimePeroid(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    @Override
    public int compareTo(TimePeroid o) {
        return this.getStart().compareTo(o.getStart());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimePeroid peroid = (TimePeroid) o;
        return this.start.equals(peroid.getStart()) &&

                this.end.equals(peroid.getEnd());
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "TimePeroid{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    public boolean overLaps(LocalDateTime start, LocalDateTime end){
        LocalDateTime thisStart = LocalDateTime.of(LocalDate.of(2000, 1, 1), this.start);
        LocalDateTime thisEnd = LocalDateTime.of(LocalDate.of(2000, 1, 1), this.end);

        LocalDateTime targetStart = LocalDateTime.of(LocalDate.of(2000, 1, 1), start.toLocalTime());
        LocalDateTime targetEnd = LocalDateTime.of(LocalDate.of(2000, 1, 1), end.toLocalTime());

        return targetStart.isBefore(thisEnd) && targetEnd.isAfter(thisStart);
    }
}
