package android.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is the main class for Charger
 */
public class Charger_stations extends AppCompatActivity {
    ListView listview;
    Toolbar toolbar;
    BaseAdapter myAdapter;
    SharedPreferences prefs;
    SearchManager searchManager;
    SearchView search;
    ProgressBar progressBar;
    String KEYWORD = null;
    String[] latlong;
    double latitude;
    double longitude ;
    private String weburl;
    static final String MY_CHARGER = null;
    //private String weburl = "https://api.openchargemap.io/v3/poi/?output=json&countrycode=CA&latitude=45.347571&longitude=-75.756140&maxresults=10";

    ArrayList<HashMap<String, String>> chargerList = new ArrayList<>();

    static final String KEY_TITLE = "Title";
    static final String KEY_CONTACTTELEPHONE1 = "ContactTelephone1";
    static final String KEY_LATITUDE = "Latitude";
    static final String KEY_LONGITUDE = "Longitude";

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charger_stations);
        prefs = getSharedPreferences(MY_CHARGER, MODE_PRIVATE);

        //check search result, if it is not null, display it
        if (prefs != null ) {

        KEYWORD = prefs.getString("KEYWORD", "45,-75");
        latlong =  KEYWORD.split(",");
        latitude= Double.parseDouble(latlong[0]);
        longitude = Double.parseDouble(latlong[1]);
        weburl = "https://api.openchargemap.io/v3/poi/?output=json&countrycode=CA&latitude=" + latitude+"&longitude="+longitude+"&maxresults=10";
        }

        //if search result is null, use default location
        else
            weburl = "https://api.openchargemap.io/v3/poi/?output=json&countrycode=CA&latitude=45.347571&longitude=-75.756140&maxresults=10";

        listview = findViewById(R.id.chargerstations_listview);
        progressBar = findViewById(R.id.charger_progressbar);
        listview.setEmptyView(progressBar);

        toolbar = findViewById(R.id.toolbar_chargerstations);
        setSupportActionBar(toolbar);

        Download task = new Download();
        task.execute();


    }

    /**
     * @class:use this class to download information from the website
     */
    class Download extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            String xml = Downloader.excuteGet(weburl);
            return xml;
        }

        /**
         *
         * @param xml
         * use JSON to extra the information of required attributes
         */
        @Override
        protected void onPostExecute(String xml) {

            if (xml.length() > 2) {

                try {

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

                        chargerList.add(map);

                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                }

                myAdapter = new MyListAdapter();
                listview.setAdapter(myAdapter);

                listview.setOnItemClickListener((parent, view, position, id) -> {

                    Intent i = new Intent(Charger_stations.this, ChargerStationDetail.class);
                    i.putExtra("title", chargerList.get(position).get(KEY_TITLE));
                    i.putExtra("latitude", chargerList.get(position).get(KEY_LATITUDE));
                    i.putExtra("longitude", chargerList.get(position).get(KEY_LONGITUDE));
                    i.putExtra("telephone", chargerList.get(position).get(KEY_CONTACTTELEPHONE1));
                    startActivity(i);

                });

            } else {
                Toast.makeText(getApplicationContext(), "No news found", Toast.LENGTH_SHORT).show();
            }
            }

    }

    /**
     * use Adapter to populate the attributes into ListView
     */
    //Need to add 4 functions here:
    class MyListAdapter extends BaseAdapter {

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

        public View getView(int position, View recycled, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            View newView = inflater.inflate(R.layout.charger_row_layout, parent, false);

            TextView rowTitle = (TextView) newView.findViewById(R.id.titleField);
            //String n = "<b>bold</b> <medium>mediem</medium>";
            //rowTitle.setText(Html.fromHtml(n));
            TextView rowNumber = (TextView) newView.findViewById(R.id.numberField);
            //ImageView rowImg = (ImageView) newView.findViewById(R.id.article_img);

            HashMap<String, String> data = new HashMap<String, String>();
            data = chargerList.get(position);

            rowTitle.setText(data.get(KEY_TITLE));
            rowNumber.setText(data.get(KEY_CONTACTTELEPHONE1));

            return newView;
        }
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.charger_menu_main, menu);
        searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        search = (SearchView) menu.findItem(R.id.charger_search).getActionView();
        search.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        search.setSubmitButtonEnabled(true);
        return super.onCreateOptionsMenu(menu);

    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.charger_search:
                search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        Intent searchIntent = new Intent(Charger_stations.this, Charger_SearchActivity.class);

                        //getPreferences(MODE_PRIVATE).edit().putString("KEYWORD",query).apply();
                        SharedPreferences.Editor editor = getSharedPreferences(MY_CHARGER, MODE_PRIVATE).edit();
                        editor.putString("KEYWORD", query);
                        editor.commit();

                        searchIntent.putExtra("query", query);
                        startActivity(searchIntent);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String query) {

                        return false;
                    }

                });

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
