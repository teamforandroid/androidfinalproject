package android.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charger_station_detail);
        textview_title = findViewById(R.id.textView_detail1);
        textview_lat = findViewById(R.id.textView_detail2);
        textview_lon = findViewById(R.id.textView_detail3);
        textview_phone = findViewById(R.id.textView_detail4);

        toolbar = findViewById(R.id.toolbar_chargerstations);
        setSupportActionBar(toolbar);

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
}
