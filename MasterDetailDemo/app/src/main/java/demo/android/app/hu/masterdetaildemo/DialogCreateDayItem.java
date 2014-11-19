package demo.android.app.hu.masterdetaildemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Peter on 2014.11.13..
 */
public class DialogCreateDayItem  extends DialogFragment {

    public static final String TAG = "MessageFragment";
    public static final String KEY_MSG = "KEY_MSG";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //String msg = getArguments().getString(KEY_MSG);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.app_name);
        builder.setView(
                getActivity().getLayoutInflater().inflate(
                        R.layout.fragment_create_day_item, null));

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        Button btn = (Button) getDialog().findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SAVE new dayItem here via SugarORM

                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),
                        Activity.RESULT_OK,
                        getActivity().getIntent());



                dismiss();
            }
        });
    }
}

