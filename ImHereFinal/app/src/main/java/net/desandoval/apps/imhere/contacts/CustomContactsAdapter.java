package net.desandoval.apps.imhere.contacts;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.desandoval.apps.imhere.R;
import net.desandoval.apps.imhere.main.SendLocation;

import java.util.List;

/**
 * Created by Daniel on 13/12/2014.
 */
public class CustomContactsAdapter extends RecyclerView.Adapter<CustomContactsAdapter.ViewHolder> {
    final String TAG = CustomContactsAdapter.class.getSimpleName();

    private List<Contact> mContacts;
    private Activity mActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView mName;
        public TextView mNumber;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            mName = (TextView) v.findViewById(R.id.tvName);
            mNumber = (TextView) v.findViewById(R.id.tvNumber);
        }
    }

    public CustomContactsAdapter(List<Contact> contacts, EditContacts context) {
        mContacts = contacts;
        mActivity = context;
    }

    public CustomContactsAdapter(List<Contact> contacts, SendLocation context) {
        mContacts = contacts;
        mActivity = context;
    }

    @Override
    public CustomContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_row, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Contact thisContact = mContacts.get(position);
        if (thisContact != null) {
            viewHolder.mName.setText(thisContact.getName());
            viewHolder.mNumber.setText(thisContact.getNumber());
        }else {
            Log.e(TAG, "contact is null in viewHolder at position:" + position);
        }
    }


    @Override
    public int getItemCount() {
        return mContacts.size();
    }
}
