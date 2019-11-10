package android.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Charger extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap googleMap;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Object Bitmap;

    private String weburl = "https://api.openchargemap.io/v3/poi/?output=json&countrycode=CA&latitude=45.347571&longitude=-75.756140&maxresults=10";

    ArrayList<HashMap<String, String>> chargerList = new ArrayList<>();

    static final String KEY_TITLE = "Title";
    static final String KEY_CONTACTTELEPHONE1 = "ContactTelephone1";
    static final String KEY_LATITUDE = "Latitude";
    static final String KEY_LONGITUDE = "Longitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charger);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }


        Download task = new Download();
        task.execute();
    }

    class Download extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            String xml = Downloader.excuteGet(weburl);
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {

            if (xml.length() > 2) {

                try {
                    //JSONObject jsonResponse = new JSONObject(xml);
                    //JSONArray jsonArray = jsonResponse.optJSONArray("AddressInfo");
                    //JSONObject json = new JSONObject(xml);
                    JSONArray jsonArray = new JSONArray(xml);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject json2 = jsonObject.getJSONObject("AddressInfo");
                        HashMap<String, String> map = new HashMap<>();

                        map.put(KEY_TITLE, json2.optString(KEY_TITLE));
                        map.put(KEY_CONTACTTELEPHONE1, json2.optString(KEY_CONTACTTELEPHONE1));
                        map.put(KEY_LATITUDE, json2.optString(KEY_LATITUDE));
                        map.put(KEY_LONGITUDE, json2.optString(KEY_LONGITUDE));


                        Log.v("Title: ", KEY_TITLE);
                        Log.v("Latitude: ", KEY_LATITUDE);
                        Log.v("Longitude: ", KEY_LONGITUDE);

                        LatLng position = new LatLng(Double.parseDouble(KEY_LATITUDE), Double.parseDouble(KEY_LONGITUDE));
                        googleMap.addMarker(new MarkerOptions().position(position).title(KEY_TITLE).snippet("snippet").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_charging)));

                        chargerList.add(map);

                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        this.googleMap = googleMap;

        LatLng Ottawa = new LatLng(45.4215, -75.6972);
        googleMap.addMarker(new MarkerOptions().position(Ottawa).title("Ottawa"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Ottawa, 15));


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }

        }
    }


}
