package android.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

public class ChargerStationDetail extends AppCompatActivity {
    Toolbar toolbar;
    TextView textview_title, textview_lat, textview_lon, textview_phone, textview_alert;
    Button button_googlemap, button_alert;
    Intent intent;
    String title, latitude, longitude, telephone;
    SearchManager searchManager;
    SearchView search;
    Charger_MyDatabaseOpenHelper mydb;


    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charger_station_detail);
        textview_title = findViewById(R.id.textView_detail1);
        textview_lat = findViewById(R.id.textView_detail2);
        textview_lon = findViewById(R.id.textView_detail3);
        textview_phone = findViewById(R.id.textView_detail4);

        textview_alert = findViewById(R.id.charger_alert);

        toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        mydb = new Charger_MyDatabaseOpenHelper(this);

        button_googlemap = findViewById(R.id.button_googlemap);
        button_alert = findViewById(R.id.button_like);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        telephone = intent.getStringExtra("telephone");

        textview_title.setText("Charger Station Title: " + title);
        textview_lat.setText("Charger Station latitude: "+latitude);
        textview_lon.setText("Charger Station longitude: "+longitude);
        textview_phone.setText("Charger Station telephone number: "+telephone);

        button_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChargerStationDetail.this);
                builder.setCancelable(true);
                builder.setTitle("I Like It Alert");
                builder.setMessage("I like this charge station!");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        textview_alert.setVisibility(View.VISIBLE);

                    }

                });

                builder.show();

            }

        });

                button_googlemap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:latitude,longitude");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });



                Toast.makeText(getApplicationContext(), "Above are the charger station information in details", Toast.LENGTH_LONG).show();

                Snackbar.make(findViewById(R.id.myRelativeLayout), R.string.textView_charge,
                        Snackbar.LENGTH_SHORT)
                        .show();


            }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.charger_menu_detail, menu);
        return super.onCreateOptionsMenu(menu);

    }

    /**
     *
     * @param item
     * @return
     */
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
                    alertExample();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     *
     * @param title
     * @param latitude
     * @param longitude
     * @param telephone
     */
    public void addChargerData(String title, String latitude, String longitude, String telephone) {
        boolean insertData = mydb.addChargerData(title, latitude, longitude, telephone);
        if (insertData) {

            Toast.makeText(getApplicationContext(), "Charger Station Data saved", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Wrong, not saved", Toast.LENGTH_LONG).show();
        }
    }


    public void alertExample()
    {

        View middle = getLayoutInflater().inflate(R.layout.charger_viewextrastuff, null);
        ImageView imageView = (ImageView) findViewById(R.id.charger_fish);
        //EditText et = (EditText)middle.findViewById(R.id.view_edit_text);
        TextView textView1 = (TextView)findViewById(R.id.charger_help_author);
        TextView textView2 = (TextView)findViewById(R.id.charger_help_version);
        TextView textView3 = (TextView)findViewById(R.id.charger_help_instruction);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("")
                .setPositiveButton("Positive", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                }).setView(middle);

        builder.create().show();
    }


}
