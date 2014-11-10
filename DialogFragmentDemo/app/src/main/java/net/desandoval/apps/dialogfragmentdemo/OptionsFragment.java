package net.desandoval.apps.dialogfragmentdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Daniel on 3/11/2014.
 */
public class OptionsFragment extends DialogFragment implements
        DialogInterface.OnClickListener {

    public interface FruitSelectionInterface {
        public void fruitSelected(String fruit);
    }

    public static final String TAG = "OptionsFragment";

    private String[] options = {"Apple", "Orange", "Lemon"};

    private FruitSelectionInterface fruitSelectionInterface = null;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        try{
            fruitSelectionInterface = (FruitSelectionInterface) activity;
        } catch(ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + "must implement FruitSelectionInterface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select a fruit!");
        builder.setItems(options, this);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        fruitSelectionInterface.fruitSelected(
                options[which]
        );
    }
}