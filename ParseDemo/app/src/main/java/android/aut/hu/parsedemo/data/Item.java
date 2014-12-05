package android.aut.hu.parsedemo.data;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by Peter on 2014.12.01..
 */
@ParseClassName("Item")
public class Item extends ParseObject {

    public Item(){
    }

    public String getItemName() {
        return getString("itemname");
    }

    public void setItemName(String itemName) {
        put("itemname", itemName);
    }

    public double getPrice() {
        return getDouble("price");
    }

    public void setPrice(double price) {
        put("price",price);
    }

    public ParseFile getPhotoFile() {
        return getParseFile("item_photo");
    }

    public void setPhotoFile(ParseFile file) {
        put("item_photo", file);
    }

}
