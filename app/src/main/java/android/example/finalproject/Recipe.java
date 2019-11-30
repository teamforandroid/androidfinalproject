/**
 * Assignment : final project - recipe
 * Author ：zheyuan li
 * date : Nov 29 2019
 */
package android.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.example.finalproject.model.RecipePojo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 *creat recipe class
 */
public class Recipe extends AppCompatActivity {

    public static final String ACTIVITY_NAME = "RecipeSearch";



     Button searchItemButton;
     EditText recipeItemText;
     ListView recipeList;
     ProgressBar recipeProgress;
     String recipekeyword;
     ArrayList<RecipePojo>  foodList;
     BaseAdapter recipemyAdapter;
     Toolbar recipetoolbar;

    private String search;

    private Object Intent;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        foodList= new ArrayList<>();
        // find  recipeItemText searchItemButton recipeList
        recipeItemText = (EditText) findViewById(R.id.recipeItemText);
        searchItemButton = (Button) findViewById(R.id.searchItemButton);
        recipeList = (ListView) findViewById(R.id.recipeList);

        recipetoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(recipetoolbar);

        // To populate the recipeList with data
        recipeList.setAdapter(recipemyAdapter = new MyListAdapter());
        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //go next page
           @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               goToDetailPage(foodList.get(position));
            }
        });
       //to set the progressBar’s visibility
        recipeProgress = (ProgressBar) findViewById(R.id.recipe_progress);
        recipeProgress.setVisibility(View.INVISIBLE);


        //To remind to entry search item
        searchItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View view) {
                recipeProgress.setVisibility(View.VISIBLE);

                    new RecipeQuery().execute();


            }
        });

    }

    /**
     *
     * @param obj
     */
    private void goToDetailPage(RecipePojo obj){
        Intent intent = new Intent(Recipe.this, RecipeDetailActivity.class);
        intent.putExtra("title",obj.getTitle());
        intent.putExtra("url",obj.getUrl());

        //try to get image
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        obj.getImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//        intent.putExtra("image",byteArray);
        startActivity(intent);

    }
      //adapter interface for foodList view
        class MyListAdapter extends BaseAdapter {
        //returns the size of foodList (POJO object)
        public int getCount() {
            return foodList.size();
        }

       //returns the foodList(POJO object) at row position in the list
        public RecipePojo getItem(int position) {
            return foodList.get(position);
        }

        // for return database id of the at position
        public long getItemId(int p) {
            return p;
        }
        //specifies how the homepage list view looks
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            RecipePojo obj = getItem(i);
            LayoutInflater inflater = getLayoutInflater();
            View thisFoodListRow = inflater.inflate(R.layout.recipe_hompage_listview_layout, null);
                TextView itemField = thisFoodListRow.findViewById(R.id.recipe_homepage_listview_Textview);
                itemField.setText(obj.getTitle());

            return thisFoodListRow;
        }

    }
        /*
        *To query data from the internet
        */
    private class RecipeQuery extends AsyncTask<String, String, String> {

        //Background thread
        @Override
        protected String doInBackground(String... strings) {
            foodList= new ArrayList<>();
            String title =null;
            String urlImage=null;
            String url=null;
            Bitmap foodImage=null;

        // String queryURL = "https://api.edamam.com/search?q="+keyword+"&r=http%3A%2F%2Fwww.edamam.com%2Fontologies%2Fedamam.owl%23recipe_9b5945e03f05acbf9d69625138385408&app_key=518555839126313535ab5bfb9f7da8ad&app_id=9fb9e712";
            String queryURL = "http://torunski.ca/FinalProjectChickenBreast.json";
            try {
                ////To create object from json
                JSONObject jo =new JSONObject(getJSON(queryURL,10000));
                title=jo.getJSONArray("recipes").getJSONObject(0).getString("title");
                Log.d("aaaaaaaaaa","title="+title);
                //get title image url from JSONArray
                JSONArray array =jo.getJSONArray("recipes");
                for(int n = 0; n < array.length(); n++)
                {
                    //To get tilte urlimage image
                    title=array.getJSONObject(n).getString("title");
                    urlImage=array.getJSONObject(n).getString("image_url");
                    url=array.getJSONObject(n).getString("source_url");
                    foodImage=getBitmapFromURL(urlImage);
                    Log.d("imgurl",urlImage);
                    boolean a=foodImage==null;
                    Log.d("dddddddddddddddd",""+a);

                    RecipePojo rp = new RecipePojo(title,foodImage,url);
                    foodList.add(rp);

                }

                 } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public Bitmap getBitmapFromURL(String src) {
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        /*
         * get json string by url  from  https://stackoverflow.com/questions/10500775/parse-json-from-httpurlconnection-object
         */
        public String getJSON(String url, int timeout) {
            HttpURLConnection c = null;
            try {
                URL u = new URL(url);
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                c.setRequestProperty("Content-length", "0");
                c.setUseCaches(false);
                c.setAllowUserInteraction(false);
                c.setConnectTimeout(timeout);
                c.setReadTimeout(timeout);
                c.connect();
                int status = c.getResponseCode();

                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        br.close();
                        return sb.toString();
                }

            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            return null;
        }

          //invoked on the UI thread after the background computation finishes
        @Override
        protected void onPostExecute(String args){
            notifyAdapter();
        }

    }
      // To update data
        void notifyAdapter(){

        recipemyAdapter.notifyDataSetChanged();
            recipeProgress.setVisibility(View.INVISIBLE);
        }



     /*
      *to create toolbar
      */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_menu, menu);


        return true;
    }
    /*
     *toolbar   help toast snackbar
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.recipe_help_ChoiceToast:

                Toast.makeText(getApplicationContext(), R.string.recipe_toast_string,
                        Toast.LENGTH_SHORT).show();

                break;
            case R.id.recipe_ChoiceGoHome:

                Snackbar.make(findViewById(R.id.toolbar),  R.string.recipe_gohome_string, Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        }).show();

                break;

            case R.id.recipe_help:

                new AlertDialog.Builder(this)
                        .setTitle("Help")
                        .setMessage(R.string.recipe_help_string)
                        .show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
