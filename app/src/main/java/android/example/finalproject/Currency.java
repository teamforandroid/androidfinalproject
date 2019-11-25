package android.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
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


public class Currency extends AppCompatActivity{
    /**
     * List to save the Currency types which query from the given url. Will used by spinner.
     */
    List<String> list = new ArrayList<String>();
    /**
     * spinners to choose the original and target currency.
     */
    Spinner spinner1,spinner2;
    /**
     * EditText to input the amount and output the convert result.
     */
    EditText text1, text2;
    /**
     * ProgressBar to display current progress.
     */
    ProgressBar progressBar;
    /**
     * ToolBar to display help menu.
     */
    Toolbar tBar;

    String CURRENCY_PREF = "CUREENCYPREF";
    String CURRENCY_HISTORY = "CURRENCYHISTORY";

    SharedPreferences prefs, prefsForList;
    ArrayAdapter<String> theAdapter;
    ArrayList<String> source = new ArrayList<>();

    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final int EMPTY_ACTIVITY = 345;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        prefs = getSharedPreferences(CURRENCY_PREF, MODE_PRIVATE);
        prefsForList = getSharedPreferences(CURRENCY_HISTORY, MODE_PRIVATE);
        progressBar = findViewById(R.id.progressBar);
        int historyNumbers = prefsForList.getInt("COUNT",0);
        for (int i = 1;i <= historyNumbers;) {
            if (prefsForList.getString("From" + Integer.toString(i),null) == null)
                continue;
            String tmp = Integer.toString(i) + ":" + prefsForList.getString("FromAmount" + Integer.toString(i),null) + " "
                    + prefsForList.getString("From" + Integer.toString(i),null) + "  Convert to "
                    + prefsForList.getString("ToAmount" + Integer.toString(i),null) + " "
                    + prefsForList.getString("To" + Integer.toString(i),null);
            source.add(tmp);
            i++;
        }

        /**
         * Toast to display welcome.
         */
        Toast.makeText(getApplicationContext(),"Welcome to currency conversion page!",Toast.LENGTH_SHORT).show();

        /**
         * Initialize class variables.
         */
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        text1 = findViewById(R.id.firstCurrency);
        text2 = findViewById(R.id.secondCurrency);

        /**
         * Adding input EditText listener to update progress bar.
         */
        text1.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(50);
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

        /**
         * Query is another thread, to avoid it is empty add one empty item firstly.
         */
        list.add("");;

        /**
         * Query the currency list from the url. Sleep to wait the query to be finished.
         */
        doQuery("");
        try {
          Thread.sleep(1000);
        } catch (Exception e) {

        }

        /**
         * Setting the spinners by the url query result.
         */
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
        spinner1.setAdapter(dataAdapter);
        /**
         * spinner1.setSelected(false);
         */
        spinner1.setSelection(0,true);
        spinner2.setSelection(0,true);

        /**
         * Adding spinner listener to set the text color and update progress bar.
         */
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.RED);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(25);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                 //Another interface callback

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.BLUE);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(75);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });
        //spinner2.setOnItemSelectedListener(this);

        /**
         * Adding convert button listener to trigger query and show snackbar.
         */
        Button btn = (Button) findViewById(R.id.convert);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromCurrency = spinner1.getSelectedItem().toString();
                String toCurrency = spinner2.getSelectedItem().toString();

                //Generate the target URI for query use.
                String urlTarget = "base=" + fromCurrency + "&symbols=" + toCurrency;
                doQuery(urlTarget);

                Snackbar.make(view, "The currency converted finished. Thank you!", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(Currency.this);
                builder.setCancelable(true);
                builder.setTitle("Successful conversion");
                builder.setMessage("This is a message in the App");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //alertTextView.setVisibility(View.VISIBLE);
                    }
                });
                builder.show();
            }
        });

        tBar = (Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(tBar);
        text1.setText(prefs.getString("ORIGINALVALUE",""));
        text2.setText(prefs.getString("TARGETVALUE",""));

        ListView theList = (ListView)findViewById(R.id.theList);
        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

        theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, source);
        theList.setAdapter( theAdapter );
        theList.setOnItemClickListener( (list, item, position, id) -> {

            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, source.get(position) );
            dataToPass.putInt(ITEM_POSITION, position);
            dataToPass.putLong(ITEM_ID, id);

            if(isTablet)
            {
                Currency_DetailFragment dFragment = new Currency_DetailFragment(); //add a Currency_DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("AnyName") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(Currency.this, Currency_EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EMPTY_ACTIVITY)
        {
            if(resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long id = data.getLongExtra(ITEM_ID, 0);
                deleteMessageId((int)id);
            }
        }
    }

    public void deleteMessageId(int id)
    {
        Log.i("Delete this message:" , " id="+id);
        String index = source.get(id).substring(0,source.get(id).indexOf(":"));
        SharedPreferences.Editor editor1 = prefsForList.edit();
        editor1.putInt("COUNT",prefsForList.getInt("COUNT",0) - 1);
        editor1.remove("From" + index);
        editor1.remove("FromAmount" + index);
        editor1.remove("To" + index);
        editor1.remove("ToAmount" + index);
        editor1.commit();
        source.remove(id);
        theAdapter.notifyDataSetChanged();
    }
    /**
     * Initialize the menu option.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.currency_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * Handle item selection
         */
        switch (item.getItemId()) {
            /**
             * Back button
             */
            case R.id.currency_help:
                View viewDialog = getLayoutInflater().inflate(R.layout.currency_helpdialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("The Message")
                        .setView(viewDialog)
                        .setPositiveButton("Positive", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                builder.create().show();
                break;
        }
        return true;
    }
    /****************************************
    *Construct and call the Async query.
    *Parameter uri, for different original and target currency generate different uri.
    *               For the first time, get the list, using empty.
     ****************************************/
    private void doQuery(String uri) {
        rateQuery query = new rateQuery(uri);
        query.execute();
    }

    /**
     * Iner class to execute Async query.
     */
    private class rateQuery extends AsyncTask<String, Integer, String> {
        String queryContent;  //special query uri.
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
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            /*//Update GUI stuff only:
            try {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(values[0]);
                Thread.sleep(1000);
            }catch (Exception e) {}*/

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
                      //If the uri is empty, it means we are getting the list.
                      if (queryContent.isEmpty()) {
                          list.add(key);
                      } else if (key.equals(spinner2.getSelectedItem().toString())) {
                          //if the uri has value, and the result is match, calculator and update the result.
                          String value = rate.getString(key);
                          float rateValue = Float.valueOf(value);
                          float originalValue = Float.valueOf(text1.getText().toString());
                          float targetValue = rateValue * originalValue;
                          text2.setText(Float.toString(targetValue));
                          progressBar.setVisibility(View.VISIBLE);
                          progressBar.setProgress(100);
                          SharedPreferences.Editor editor = getSharedPreferences(CURRENCY_PREF, MODE_PRIVATE).edit();
                          editor.putString("ORIGINAL", spinner1.getSelectedItem().toString());
                          editor.putString("TARGET", spinner2.getSelectedItem().toString());
                          editor.putString("ORIGINALVALUE", text1.getText().toString());
                          editor.putString("TARGETVALUE", Float.toString(targetValue));

                          editor.commit();

                          SharedPreferences.Editor editor1 = prefsForList.edit();
                          int currentNum = prefsForList.getInt("COUNT",0);
                          String historyNumber = Integer.toString(prefsForList.getInt("COUNT",0) + 1);
                          for (int i = 1;i <= currentNum;i++)
                          {
                              if (prefsForList.getString("From" + Integer.toString(i),null) == null) {
                                  historyNumber = Integer.toString(i);
                                  break;
                              }
                          }
                          editor1.putInt("COUNT",prefsForList.getInt("COUNT",0) + 1);
                          editor1.putString("From" + historyNumber, spinner1.getSelectedItem().toString());
                          editor1.putString("FromAmount" + historyNumber, text1.getText().toString());
                          editor1.putString("To" + historyNumber, spinner2.getSelectedItem().toString());
                          editor1.putString("ToAmount" + historyNumber, Float.toString(targetValue));
                          String tmp = historyNumber + ":" + text1.getText().toString() + " "
                                  + spinner1.getSelectedItem().toString() + "  Convert to "
                                  + Float.toString(targetValue) + " "
                                  + spinner2.getSelectedItem().toString();
                          editor1.commit();
                          source.add(tmp);
                          theAdapter.notifyDataSetChanged();
                      }
                      //Object value = rate.get(key);
                    }
                if ((!prefs.getString("ORIGINAL","").isEmpty()) && list.indexOf(prefs.getString("ORIGINAL","")) != 0) {
                    spinner1.setSelection(list.indexOf(prefs.getString("ORIGINAL","")));
                }

                if ((!prefs.getString("TARGET","").isEmpty()) && list.indexOf(prefs.getString("TARGET","")) != 0) {
                    spinner2.setSelection(list.indexOf(prefs.getString("TARGET","")));
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
