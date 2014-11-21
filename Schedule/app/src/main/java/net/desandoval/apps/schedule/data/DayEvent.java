package net.desandoval.apps.schedule.data;

import com.orm.SugarRecord;

/**
 * Created by Daniel Sandoval on 2014.10.21
 * Modified from Peter's PlaceToVisit Example in class on 2014.10.08..
 */
public class DayEvent extends SugarRecord<DayEvent> implements Comparable<DayEvent> {

    private String title;
    private int startHour;
    private int startMinute;
    private String location;
    private String day;

    public DayEvent(){

    }

    public DayEvent(String day, String title, int startHour, int startMinute, String location) {
        this.title = title;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.location = location;
        this.day = day;
    }



    public String getTitle() {
        return title;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getStartMinute() { return startMinute; }

    public String getLocation() {
        return location;
    }

    public String getDay() { return day; }

    @Override
    public int compareTo(DayEvent another) {
        int compareHour = this.getStartHour() - another.getStartHour();
        if (compareHour != 0) {
            return compareHour;
        } else {
            return this.getStartMinute() - another.getStartMinute();
        }
    }
}
