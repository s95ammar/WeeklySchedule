package com.s95ammar.weeklyschedule.models;


import com.google.gson.annotations.Expose;
import org.joda.time.LocalTime;
import java.io.Serializable;

public class Event implements Serializable/*, Comparable<Event>*/ {
    @Expose
    private String name;
    @Expose
    private Category category;
    private Day day;
    @Expose
    private LocalTime startTime;
    @Expose
    private LocalTime endTime;
    public static final LocalTime DEFAULT_TIME = new LocalTime(12, 00);

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

    public static boolean isTimeValid(LocalTime startTime, LocalTime endTime) {
        return endTime.isAfter(startTime);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Event) {
            Event other = (Event) obj;
            return name.equals(other.name) &&
                    category.equals(other.category) &&
                    day.equals(other.day) &&
                    startTime.equals(other.startTime) &&
                    endTime.equals(other.endTime);
        }
        return false;
    }


    public boolean overlapsWith(Event other) {
        return (startTime.isEqual(other.startTime)) ||
                (startTime.isBefore(other.startTime) && endTime.isAfter(other.startTime)) ||
                (startTime.isAfter(other.startTime) && startTime.isBefore(other.endTime)) ||
                (endTime.isAfter(other.startTime) && endTime.isBefore(other.endTime));
    }

    @Override
    public String toString() {
        return '{' +
                "name='" + name + '\'' +
                ", category=" + category.getName() +
                ", day=" + day.getDayOfWeek() +
                ", startTime=" + startTime.toString("HH:mm") +
                ", endTime=" + endTime.toString("HH:mm") +
                '}';
    }

}
/*
TODO: implement comparators
    public static class EventNameComparator implements Comparator<Event>, Serializable {
        @Override
        public int compare(Event o1, Event o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }

    public static class EventTimeComparator implements Comparator<Event>, Serializable {
        @Override
        public int compare(Event o1, Event o2) {
            return o1.getStartTime().compareTo(o2.getStartTime());
        }
    }
*/

