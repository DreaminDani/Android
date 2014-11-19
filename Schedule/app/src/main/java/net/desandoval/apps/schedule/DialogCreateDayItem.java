package net.desandoval.apps.schedule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import net.desandoval.apps.schedule.adapter.EventListAdapter;
import net.desandoval.apps.schedule.data.DayEvent;

import java.util.List;

/**
 * Created by Peter on 2014.11.13..
 */
public class DialogCreateDayItem extends DialogFragment {

    public static final String TAG = "MessageFragment";
    public static final String KEY_MSG = "KEY_MSG";
    private String day;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //String msg = getArguments().getString(KEY_MSG);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Add to Today's Schedule");
        builder.setView(
                getActivity().getLayoutInflater().inflate(
                        R.layout.fragment_create_day_item, null));

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle mArgs = getArguments();
        this.day = mArgs.getString("day");

        final EditText etEventName  = (EditText) getDialog().findViewById(R.id.etEventName);
        final EditText etEventLocation  = (EditText) getDialog().findViewById(R.id.etEventLocation);
        final TimePicker etStartTime = (TimePicker) getDialog().findViewById(R.id.etStartTime);
        Button btn = (Button) getDialog().findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SAVE new dayItem here via SugarORM
                DayEvent dayEvent = new DayEvent(day, etEventName.getText().toString(), etStartTime.getCurrentHour(),
                                                    etStartTime.getCurrentMinute(), etEventLocation.getText().toString());
                dayEvent.save();

                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),
                        Activity.RESULT_OK,
                        getActivity().getIntent());



                dismiss();
            }
        });
    }
}

