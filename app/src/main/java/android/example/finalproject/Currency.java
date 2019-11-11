package android.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.android.material.snackbar.Snackbar;

import static android.widget.AdapterView.*;


public class Currency extends AppCompatActivity{
    List<String> list = new ArrayList<String>();
    Spinner spinner1,spinner2;
    EditText text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        Toast.makeText(getApplicationContext(),"Hello!",Toast.LENGTH_SHORT).show();
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        text1 = findViewById(R.id.firstCurrency);
        doQuery("");
        try {
          Thread.sleep(1000);
        } catch (Exception e) {

        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
        spinner1.setAdapter(dataAdapter);
        spinner1.setSelected(false);
        spinner1.setSelection(0,true);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });
        //spinner2.setOnItemSelectedListener(this);
        Button btn = (Button) findViewById(R.id.convert);

    }

    private void doQuery(String uri) {
        rateQuery query = new rateQuery(uri);
        query.execute();
    }

    private class rateQuery extends AsyncTask<String, String, String> {
        String queryContent;
        String URL="https://api.exchangeratesapi.io/latest";

        @Override
        protected String doInBackground(String... args){

            String ret = null;
            String rateURL = URL;
            if (!queryContent.isEmpty()) {
                rateURL = rateURL + "?" + queryContent;
            }
            try {       // Connect to the server:
                URL url = new URL(rateURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                JSONObject jObject = new JSONObject(result);
                ret = result;
            }
            catch(MalformedURLException mfe){ ret = "Malformed URL exception"; }
            catch(IOException ioe)          { ret = "IO Exception. Is the Wifi connected?";}
            catch(JSONException JSONeX){ret = "Json Exception. The Json is not properly formed";}
            //What is returned here will be passed as a parameter to onPostExecute:
            return ret;
        }

        @Override                       //Type 2
        protected void onProgressUpdate(String... values) {
        }

        @Override                   //Type 3
        protected void onPostExecute(String sentFromDoInBackground) {
            super.onPostExecute(sentFromDoInBackground);
            try {
                    JSONObject jObject = new JSONObject(sentFromDoInBackground);
                    JSONObject rate = jObject.getJSONObject("rates");
                    Iterator<String> iter = rate.keys();
                //List<String> list = new ArrayList<String>();
                while (iter.hasNext()) {
                      String key = iter.next();
                      list.add(key);
                      //Object value = rate.get(key);
                    }
                  }catch(Exception e) {
                }

        }

        public rateQuery(String queryURI)
        {
            queryContent = queryURI;
        }
    }
}
