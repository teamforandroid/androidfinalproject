package android.example.finalproject;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is the main activity class
 */
public class News_MainActivity extends AppCompatActivity {

    ListView listNews;
    ProgressBar loader;
    SharedPreferences prefs;
    SearchManager searchManager;
    SearchView search;
    int positionClicked = 0;
    BaseAdapter myAdapter;
    String KEYWORD = null;
    Toolbar toolbar;
    static final String MY_NAME = null;
    //String API_KEY = "a65a65ef7a2f4c4c89a76a64790c4af9";

    //static String NEWS_SOURCE = "abc-news";
    //static String weburl = "https://newsapi.org/v2/top-headlines?sources=" + NEWS_SOURCE + "&apiKey=" + API_KEY;
    //String weburl = "https://newsapi.org/v2/everything?q=" + KEYWORD + "&from=2019-10-01&sortBy=publishedAt&apiKey=" + API_KEY;

    //News api url
    String weburl = "https://newsapi.org/v2/everything?q=" + KEYWORD + "&apiKey=a65a65ef7a2f4c4c89a76a64790c4af9";

    ArrayList<HashMap<String, String>> dataList = new ArrayList<>();

    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_URL = "url";
    static final String KEY_URLTOIMAGE = "urlToImage";
    static final String KEY_PUBLISHEDAT = "publishedAt";
    static final String KEY_AUTHOR = "author";

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity_main);

        listNews = findViewById(R.id.listNews);
        loader = findViewById(R.id.loader);
        listNews.setEmptyView(loader);
        prefs = getSharedPreferences(MY_NAME, MODE_PRIVATE);
        KEYWORD = prefs.getString("KEYWORD", null);
        weburl = "https://newsapi.org/v2/everything?q=" + KEYWORD + "&apiKey=a65a65ef7a2f4c4c89a76a64790c4af9";

        toolbar = (Toolbar)findViewById(R.id.toolbar_main_news);
        setSupportActionBar(toolbar);

        if (Downloader.isNetworkConnected(getApplicationContext())) {
            DownloadNews newsTask = new DownloadNews();
            newsTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * DownloadNews class used for downloading news form the URL
     */
    class DownloadNews extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * @param args
         * @return
         */
        protected String doInBackground(String... args) {

            String xml = Downloader.excuteGet(weburl);
            return xml;
        }

        /**
         * @param xml
         */
        @Override
        protected void onPostExecute(String xml) {

            if (xml.length() > 10) {

                try {
                    JSONObject jsonResponse = new JSONObject(xml);
                    JSONArray jsonArray = jsonResponse.optJSONArray("articles");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<>();

                        map.put(KEY_TITLE, jsonObject.optString(KEY_TITLE));
                        map.put(KEY_DESCRIPTION, jsonObject.optString(KEY_DESCRIPTION));
                        map.put(KEY_URL, jsonObject.optString(KEY_URL));
                        map.put(KEY_URLTOIMAGE, jsonObject.optString(KEY_URLTOIMAGE));
                        //map.put(KEY_PUBLISHEDAT, jsonObject.optString(KEY_PUBLISHEDAT));
                        //map.put(KEY_AUTHOR, jsonObject.optString(KEY_AUTHOR));
                        dataList.add(map);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                }


                myAdapter = new MyOwnAdapter();
                listNews.setAdapter(myAdapter);

                listNews.setOnItemClickListener((parent, view, position, id) -> {
                    Log.e("you clicked on :", "item " + position);
                    //save the position in case this object gets deleted or updated

                    positionClicked = position;

                    Intent i = new Intent(News_MainActivity.this, News_ArticleActivity.class);
                    i.putExtra("url", dataList.get(position).get(KEY_URL));
                    i.putExtra("title", dataList.get(position).get(KEY_TITLE));
                    i.putExtra("description", dataList.get(position).get(KEY_DESCRIPTION));
                    i.putExtra("urlToImage", dataList.get(position).get(KEY_URLTOIMAGE));
                    startActivity(i);

                });
            } else {
                Toast.makeText(getApplicationContext(), "No news found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * MyOwnAdapter class used for displaying news
     */
    public class MyOwnAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList.size();
        }

        public Object getItem(int position) {
            return dataList.indexOf(position);
        }

        public long getItemId(int position) {
            return position;
        }

        /**
         * @param position
         * @param view
         * @param parent
         * @return
         */
        public View getView(int position, View view, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();

            View newView = inflater.inflate(R.layout.news_article_row, parent, false);

            TextView rowTitle = (TextView) newView.findViewById(R.id.article_title);
            TextView rowDes = (TextView) newView.findViewById(R.id.article_des);
            ImageView rowImg = (ImageView) newView.findViewById(R.id.article_img);

            HashMap<String, String> data = new HashMap<String, String>();
            data = dataList.get(position);

            rowTitle.setText(data.get(News_MainActivity.KEY_TITLE));
            rowDes.setText(data.get(News_MainActivity.KEY_DESCRIPTION));

            if (data.get(News_MainActivity.KEY_URLTOIMAGE).toString().length() < 10) {
                rowImg.setVisibility(View.GONE);
            } else {
                Picasso.get()
                        .load(data.get(News_MainActivity.KEY_URLTOIMAGE))
                        .resize(200, 150)
                        .centerCrop()
                        .into(rowImg);
            }

            Button button = newView.findViewById(R.id.article_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(News_MainActivity.this, News_ArticleActivity.class);
                    i.putExtra("url", dataList.get(position).get(KEY_URL));
                    i.putExtra("title", dataList.get(position).get(KEY_TITLE));
                    i.putExtra("description", dataList.get(position).get(KEY_DESCRIPTION));
                    i.putExtra("urlToImage", dataList.get(position).get(KEY_URLTOIMAGE));
                    startActivity(i);
                }
            });

            //return the row:
            return newView;

        }

    }


    /**
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_menu_main, menu);

        searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);

        search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        search.setSubmitButtonEnabled(true);

        return super.onCreateOptionsMenu(menu);
        //return true;
    }

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_search:
                search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        //KEYWORD=query;
                        Intent searchIntent = new Intent(News_MainActivity.this, News_SearchActivity.class);

                        //getPreferences(MODE_PRIVATE).edit().putString("KEYWORD",query).apply();
                        SharedPreferences.Editor editor = getSharedPreferences(MY_NAME, MODE_PRIVATE).edit();
                        editor.putString("KEYWORD", query);
                        editor.commit();

                        searchIntent.putExtra("query", query);
                        startActivity(searchIntent);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String query) {
                        //KEYWORD=query;
                        return false;
                    }

                });

                return true;

            case R.id.action_saved:
                Intent saveIntent = new Intent(this, News_SavedActivity.class);
                this.startActivity(saveIntent);
                return true;
            case R.id.action_help:
                //Intent myIntent = new Intent(this, News_Help.class);
                //this.startActivity(myIntent);
                alertExample();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * This display Dialog box for help
     */
    public void alertExample()
    {
        View middle = getLayoutInflater().inflate(R.layout.news_extra_stuff, null);
        EditText et = (EditText)middle.findViewById(R.id.view_edit_text);
        TextView tv = middle.findViewById(R.id.help_show_text1);
        //btn.setOnClickListener( clk -> et.setText("You clicked my button!"));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("")
                .setPositiveButton("Positive", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String message = et.getText().toString();

                    }
                })
                .setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).setView(middle);

        builder.create().show();
    }
}





