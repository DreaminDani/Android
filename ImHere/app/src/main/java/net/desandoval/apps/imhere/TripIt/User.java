package net.desandoval.apps.imhere.TripIt;

import org.springframework.social.tripit.api.Trip;
import org.springframework.social.tripit.api.TripItProfile;

import java.util.List;

/**
 * Created by Daniel on 5/12/2014.
 */
public class User {

    public TripItProfile profile;
    public List<Trip> trips;

    public User(TripItProfile profile, List<Trip> trips) {
        this.profile = profile;
        this.trips = trips;
    }
}
