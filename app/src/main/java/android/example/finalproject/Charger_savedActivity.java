package android.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class Charger_savedActivity extends AppCompatActivity {

    Charger_MyDatabaseOpenHelper mydb;
    BaseAdapter myAdapter;
    Toolbar toolbar;
    ListView listStations;
    int positionClicked = 0;
    Button deleteButton, buttonshowmap;
    ArrayList<HashMap<String, String>> chargerList = new ArrayList<>();
    HashMap<String, String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charger_saved);

        listStations = findViewById(R.id.listStations_saved);
        toolbar = (Toolbar) findViewById(R.id.toolbar_charger_saved);
        setSupportActionBar(toolbar);

        //get a database:
        mydb = new Charger_MyDatabaseOpenHelper(this);
        SQLiteDatabase db = mydb.getWritableDatabase();

        buttonshowmap = findViewById(R.id.button_showmap);

        //query all the results from the database:
        String[] columns = {Charger_MyDatabaseOpenHelper.COL_ID, Charger_MyDatabaseOpenHelper.COL_TITLE,
                Charger_MyDatabaseOpenHelper.COL_LATITUDE, Charger_MyDatabaseOpenHelper.COL_LONGITUDE, Charger_MyDatabaseOpenHelper.COL_TELEPHONE};
        Cursor results = db.query(false, Charger_MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        //find the column indices:
        int titleColIndex = results.getColumnIndex(Charger_MyDatabaseOpenHelper.COL_TITLE);
        int latitudeColIndex = results.getColumnIndex(Charger_MyDatabaseOpenHelper.COL_LATITUDE);
        int longitudeColIndex = results.getColumnIndex(Charger_MyDatabaseOpenHelper.COL_LONGITUDE);
        int telephoneColIndex = results.getColumnIndex(Charger_MyDatabaseOpenHelper.COL_TELEPHONE);

        //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {
            String title = results.getString(titleColIndex);
            String latitude = results.getString(latitudeColIndex );
            String longitude = results.getString(longitudeColIndex);
            String telephone = results.getString(telephoneColIndex );

            HashMap<String, String> map = new HashMap<>();
            map.put(Charger_stations.KEY_TITLE, title);
            map.put(Charger_stations.KEY_LATITUDE, latitude);
            map.put(Charger_stations.KEY_LONGITUDE, longitude);
            map.put(Charger_stations.KEY_CONTACTTELEPHONE1, telephone);
            chargerList.add(map);
        }

        //create an adapter object and send it to the listVIew
        myAdapter = new ChargerAdapter();
        listStations.setAdapter(myAdapter);
        //listStations.invalidateViews();
        //listStations.refreshDrawableState();
    }

    public class ChargerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return chargerList.size();
        }

        public Object getItem(int position) {
            return chargerList.indexOf(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int i, View view, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();

            View newView = inflater.inflate(R.layout.charger_savedrow_layout, parent, false);

            TextView rowTitle = (TextView) newView.findViewById(R.id.chargertitle);
            TextView rowLatitude = (TextView) newView.findViewById(R.id.latitudeField);
            TextView rowLongitude = (TextView) newView.findViewById(R.id.longitudeField);
            TextView rowTelephone = (TextView) newView.findViewById(R.id.stationphone);
            deleteButton = newView.findViewById(R.id.button_stationdelete);



            data = new HashMap<String, String>();
            data = chargerList.get(i);

            rowTitle.setText(data.get(Charger_stations.KEY_TITLE));
            rowLatitude.setText(data.get(Charger_stations.KEY_LATITUDE));
            rowLongitude.setText(data.get(Charger_stations.KEY_LONGITUDE));
            rowTelephone.setText(data.get(Charger_stations.KEY_CONTACTTELEPHONE1));

          /*  buttonshowmap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri gmmIntentUri = Uri.parse("geo:data.get(Charger_stations.KEY_LATITUDE),data.get(Charger_stations.KEY_LONGITUDE)");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);

                }
            });*/

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteChargeStation(data.get(Charger_stations.KEY_TITLE));
                    myAdapter.notifyDataSetChanged();

                }
            });

            return newView;

        }

    }

    public void deleteChargeStation(String title) {
        boolean deleteData = mydb.deleteData(title);
        if (deleteData) {
            Toast.makeText(getApplicationContext(), "Data deleted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }
}
