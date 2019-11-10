package android.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

public class ChargerStationDetail extends AppCompatActivity {
    Toolbar toolbar;
    TextView textview_title, textview_lat, textview_lon, textview_phone;
    Button button;
    Intent intent;
    String title, latitude, longitude, telephone;
    SearchManager searchManager;
    SearchView search;
    Charger_MyDatabaseOpenHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charger_station_detail);
        textview_title = findViewById(R.id.textView_detail1);
        textview_lat = findViewById(R.id.textView_detail2);
        textview_lon = findViewById(R.id.textView_detail3);
        textview_phone = findViewById(R.id.textView_detail4);

        toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        mydb = new Charger_MyDatabaseOpenHelper(this);

        button = findViewById(R.id.button_detail);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        telephone = intent.getStringExtra("telephone");

        textview_title.setText("Charger Station Title: " + title);
        textview_lat.setText("Charger Station latitude: "+latitude);
        textview_lon.setText("Charger Station longitude: "+longitude);
        textview_phone.setText("Charger Station telephone number: "+telephone);


        Toast.makeText(getApplicationContext(), "Above are the charger station information in details", Toast.LENGTH_LONG).show();

        Snackbar.make(findViewById(R.id.myRelativeLayout), R.string.textView_charge,
                Snackbar.LENGTH_SHORT)
                .show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.charger_menu_detail, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.charger_save:
                addChargerData(title, latitude, longitude, telephone);
                return true;
            case R.id.charger_saved:
                Intent saveIntent = new Intent(this, Charger_savedActivity.class);
                this.startActivity(saveIntent);
                return true;

                case R.id.charger_help:
                //Intent myIntent = new Intent(this, News_Help.class);
                //this.startActivity(myIntent);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void addChargerData(String title, String latitude, String longitude, String telephone) {
        boolean insertData = mydb.addChargerData(title, latitude, longitude, telephone);
        if (insertData) {

            Toast.makeText(getApplicationContext(), "Charger Station Data saved", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Wrong, not saved", Toast.LENGTH_LONG).show();
        }
    }
}
