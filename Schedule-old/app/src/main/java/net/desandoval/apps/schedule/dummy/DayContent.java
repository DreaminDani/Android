package net.desandoval.apps.schedule.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DayContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<DayContentItem> ITEMS = new ArrayList<DayContentItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, DayContentItem> ITEM_MAP = new HashMap<String, DayContentItem>();

    static {
        // Add days of week.
        addItem(new DayContentItem("1", "Monday"));
        addItem(new DayContentItem("2", "Tuesday"));
        addItem(new DayContentItem("3", "Wednesday"));
        addItem(new DayContentItem("4", "Thursday"));
        addItem(new DayContentItem("5", "Friday"));
        addItem(new DayContentItem("6", "Saturday"));
        addItem(new DayContentItem("7", "Sunday"));
    }

    private static void addItem(DayContentItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DayContentItem {
        public String id;
        public String content;

        public DayContentItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
