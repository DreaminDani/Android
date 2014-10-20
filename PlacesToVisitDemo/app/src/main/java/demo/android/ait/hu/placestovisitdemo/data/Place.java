package demo.android.ait.hu.placestovisitdemo.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Peter on 2014.10.09..
 */
public class Place implements Serializable {
    public enum PlaceCategory {
        LANDSCAPE, CITY, BUILDING
    }

    private PlaceCategory category;
    private String name;
    private String description;
    private Date date;

    public Place(PlaceCategory category, String name,
                 String description, Date date) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public PlaceCategory getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }
}
