package net.desandoval.apps.imhere.contacts;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import net.desandoval.apps.imhere.R;

import java.util.ArrayList;
import java.util.List;

/**
 * EditContacts activity for editing the user's "ImHere" group
 */
public final class EditContacts extends Activity {
    final String TAG = CustomContactsAdapter.class.getSimpleName();

    private List<Contact> mContacts;
    List<Contact> storedContacts;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private SearchView mSearchView;
    private CustomContactsAdapter mAdapter;
    private TextView tvListInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        setupSearchView();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerList);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        populateList();
    }

    /*
     * Generates contact list from previously stored contacts
     */
    private void populateList() {
        // get contacts from Parse
        storedContacts = new ArrayList<>();
        getContactsFromParse();
        mContacts = storedContacts;
        if (mContacts == null) {
            mContacts = new ArrayList<>();
        }

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(EditContacts.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        removeContact(position);
                        mAdapter.notifyDataSetChanged();
                    }
                })
        );
    }

    /*
     * Removes contact from list and Parse
     */
    private void removeContact(int position) {
        Contact thisContact = mContacts.get(position);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CustomContacts");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.whereEqualTo("id", thisContact.getID());
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    ParseObject.deleteAllInBackground(parseObjects);
                } else {
                    Log.d("Parse Error", "Could not retrieve stored contact for deletion - contact not deleted from parse");
                }
            }
        });
        mContacts.remove(position);
    }

    /*
     * Generates SearchView from xml
     */
    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) findViewById(R.id.searchView);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);
        mSearchView = searchView;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (ContactsContract.Intents.SEARCH_SUGGESTION_CLICKED.equals(intent.getAction())) {
            Contact newContact = getContact(intent);
            addContactToParse(newContact);
            mAdapter.notifyDataSetChanged();
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    /*
     * Gets DisplayName from SearchView result (no longer used)
     */
//    private String getDisplayNameForContact(Intent intent) {
//        Cursor phoneCursor = getContentResolver().query(intent.getData(), null, null, null, null);
//        phoneCursor.moveToFirst();
//        int idDisplayName = phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//        String name = phoneCursor.getString(idDisplayName);
//        phoneCursor.close();
//        return name;
//    }

    /*
     * Gets Contact info from SearchView result (name, number, id)
     */
    private Contact getContact(Intent intent) {
        Cursor c = getContentResolver().query(intent.getData(), null, null, null, null);
        c.moveToFirst();

        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

        if (hasPhone.equalsIgnoreCase("1"))
        {
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
            phones.moveToFirst();
            String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.d("Phone Number", "Retrieved Phone Number for id:" + id + " - " + cNumber);

            String nameContact = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

            if (nameContact == null) {
                Toast.makeText(getApplicationContext(), "Contact does not have a valid name. Please try adding the contact again", Toast.LENGTH_LONG).show();
                return null;
            }

            c.close();
            return new Contact(nameContact, cNumber, id);
        }else {
            Toast.makeText(getApplicationContext(), "Contact does not have a phone number, please select a contact with a phone", Toast.LENGTH_LONG).show();
            return null;
        }

    }

    /*
     * Adds given contact to parse database
     */
    private void addContactToParse(Contact contact) {
        tvListInstructions.setVisibility(View.INVISIBLE);
        try {
            ParseObject newContact = new ParseObject("CustomContacts");
            newContact.put("name", contact.getName());
            newContact.put("number", contact.getNumber());
            newContact.put("id", contact.getID());
            newContact.put("user", ParseUser.getCurrentUser());
            newContact.saveInBackground();
            mContacts.add(contact);
        } catch (Exception e) {
            Log.e(TAG, "Cannot add contact to parse. Reason: " + e.getMessage());
            Log.e("Parse Error", e.getStackTrace().toString());
        }

    }

    private void getContactsFromParse() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CustomContacts");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    if (parseObjects.isEmpty()) {
                        tvListInstructions = (TextView) findViewById(R.id.tvListInstructions);
                        tvListInstructions.setVisibility(View.VISIBLE);
                    }else {
                        for (int i = 0; i < parseObjects.size(); i++) {
                            ParseObject obj = parseObjects.get(i);
                            Contact storedContact = new Contact(obj.getString("name"), obj.getString("number"), obj.getString("id"));
                            storedContacts.add(storedContact);
                        }
                    }
                } else {
                    Log.d("Parse Error", "Could not retrieve contacts from parse");
                }
                mAdapter = new CustomContactsAdapter(mContacts, EditContacts.this);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }
}

