package demo.android.app.hu.sqldemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

import demo.android.app.hu.sqldemo.data.DatabaseHelper;
import demo.android.app.hu.sqldemo.data.Person;


public class MyActivity extends Activity {

    private DatabaseHelper databaseHelper = null;
    private Dao<Person, Integer> peopleDAO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseHelper = new DatabaseHelper(this);

        /*createPerson(new Person("John",new Date(System.currentTimeMillis())));
        createPerson(new Person("Alex",new Date(System.currentTimeMillis())));*/

        try {
            StringBuilder sb = new StringBuilder();

            for (Person p : queryAllPerson()) {
                sb.append(p.toString());
            }

            Toast.makeText(getApplicationContext(),
                    sb.toString(),
                    Toast.LENGTH_LONG).show();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    public void createPerson(Person p) {
        try {
            getPeopleDAO().create(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Person> queryAllPerson() throws SQLException{
        return (ArrayList<Person>) getPeopleDAO().queryForAll();
    }




    public Dao<Person, Integer> getPeopleDAO() throws SQLException {
        if (peopleDAO == null) {
            peopleDAO = databaseHelper.getDao(Person.class);
        }

        return peopleDAO;
    }
}
