package net.desandoval.apps.imhere.contacts;

/**
 * Created by Daniel on 13/12/2014.
 */
public class Contact {

    private String mName;
    private String mNumber;
    private String mID;

    public Contact(String mName, String mNumber, String mID) {
        this.mName = mName;
        this.mNumber = mNumber;
        this.mID = mID;
    }

    public String getName() {
        return mName;
    }

    public String getNumber() {
        return mNumber;
    }

    public String getID() {
        return mID;
    }
}
