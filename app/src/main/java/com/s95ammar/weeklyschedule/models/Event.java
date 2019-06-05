package com.s95ammar.weeklyschedule.models;


import org.joda.time.LocalTime;

import java.io.Serializable;
import java.util.Comparator;

public class Event implements Serializable, Comparable<Event> {
    private String name;
    private Category category;
    private Day day;
    private LocalTime startTime;
    private LocalTime endTime;

    public Event(String name, Category category, Day day, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.category = category;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public boolean isNameValid() {
        return !name.isEmpty();
    }

    public boolean isTimeValid() {
        return endTime.isAfter(startTime);
    }

    @Override
    public int compareTo(Event that) {
        return name.compareTo(that.getName());
    }

    public boolean overlapsWith(Event other) {
        return (startTime.isEqual(other.startTime)) ||
                (startTime.isBefore(other.startTime) && endTime.isAfter(other.startTime)) ||
                (startTime.isAfter(other.startTime) && startTime.isBefore(other.endTime)) ||
                (endTime.isAfter(other.startTime) && endTime.isBefore(other.endTime));
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", category=" + category.getName() +
                ", day=" + day.getDayOfWeek() +
                ", startTime=" + startTime.toString("HH:mm") +
                ", endTime=" + endTime.toString("HH:mm") +
                '}';
    }

    public static class EventNameComparator implements Comparator<Event> {
        @Override
        public int compare(Event o1, Event o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }

    public static class EventTimeComparator implements Comparator<Event> {
        @Override
        public int compare(Event o1, Event o2) {
            return o1.getStartTime().compareTo(o2.getStartTime());
        }
    }

}
