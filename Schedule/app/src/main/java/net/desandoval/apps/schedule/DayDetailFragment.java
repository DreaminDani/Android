package net.desandoval.apps.schedule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.desandoval.apps.schedule.adapter.EventListAdapter;
import net.desandoval.apps.schedule.data.DayEvent;
import net.desandoval.apps.schedule.dummy.DayContent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a single Day detail screen.
 * This fragment is either contained in a {@link DayListActivity}
 * in two-pane mode (on tablets) or a {@link DayDetailActivity}
 * on handsets.
 */
public class DayDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final int CRATE_DIALOG_REQUEST_CODE = 101;
    List<DayEvent> eventList = null;

    /**
     * The dummy content this fragment is presenting.
     */
    private DayContent.DayContentItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DayDetailFragment() {
    }

    @Override
    public void onStart(){
        super.onStart();

        buildList();

        if (eventList != null) {
            EventListAdapter adapter = new EventListAdapter(eventList);
            DayDetailActivity.mAdapter = adapter;
            ListView listView = (ListView) getActivity().findViewById(R.id.eventList);
            listView.setAdapter(adapter);
        }

    }

    /*
     * Generates new itemsList from stored Items in the ORM database
     */
    public void buildList() {
        List<DayEvent> tempList = DayEvent.find(DayEvent.class, "title = " + "'" + mItem.content + "'");

        if (tempList != null) {
            eventList = tempList;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DayContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_day_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.day_detail)).setText(mItem.content);
            populateList();
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_day, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_create) {
            DialogCreateDayItem dialog = new DialogCreateDayItem();
            Bundle args = new Bundle();
            args.putString("day", mItem.content);
            dialog.setArguments(args);
            dialog.setTargetFragment(this,
                    CRATE_DIALOG_REQUEST_CODE);
            //Bundle b = new Bundle();
            //b.putString(MessageFragment.KEY_MSG, msg);
            //dialog.setArguments(b);
            dialog.show(getFragmentManager(), "CREATE_TAG");
        }

        return true;
    }

    public void populateList() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CRATE_DIALOG_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(
                        getActivity(), "Created...", Toast.LENGTH_LONG).show();
            }
        }
    }
}
