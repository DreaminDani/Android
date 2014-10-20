package demo.android.ait.hu.placestovisit.data;

import java.io.Serializable;
import java.util.Date;

import demo.android.ait.hu.placestovisit.R;

/**
 * Created by Peter on 2014.10.08..
 */
public class Place implements Serializable {

    public enum PlaceType {
        LANDSCAPE(0, R.drawable.landscape),
        CITY(1, R.drawable.city),
        BUILDING(2, R.drawable.building);

        private int value;
        private int iconId;

        private PlaceType(int value, int iconId) {
            this.value = value;
            this.iconId = iconId;
        }

        public static PlaceType fromInt(int value) {
            for (PlaceType p : PlaceType.values()) {
                if (p.value == value) {
                    return p;
                }
            }
            return LANDSCAPE;
        }

        public int getIconId() {
            return iconId;
        }
    }

    private PlaceType placeType;
    private String placeName;
    private String description;
    private Date pickUpDate;

    public Place(PlaceType placeType, String placeName, String description, Date pickUpDate) {
        this.placeType = placeType;
        this.placeName = placeName;
        this.description = description;
        this.pickUpDate = pickUpDate;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getDescription() {
        return description;
    }

    public Date getPickUpDate() {
        return pickUpDate;
    }

    public PlaceType getPlaceType() {
        return placeType;
    }
}
