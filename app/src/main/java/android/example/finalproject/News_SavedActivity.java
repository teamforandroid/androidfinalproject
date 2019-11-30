package android.example.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used for save News in my favorite
 */
public class News_SavedActivity extends AppCompatActivity {
    News_MyDatabaseOpenHelper mydb;
    BaseAdapter myAdapter;
    Toolbar toolbar;
    ListView listNews;
    int positionClicked = 0;

    //save all news in articleList
    ArrayList<HashMap<String, String>> articleList = new ArrayList<>();

    ArrayList<String> titleList = new ArrayList<>();

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity_saved);

        listNews = findViewById(R.id.listNews_saved);
        toolbar = (Toolbar)findViewById(R.id.toolbar_main_saved);
        setSupportActionBar(toolbar);

        //get a database:
        mydb = new News_MyDatabaseOpenHelper(this);
        SQLiteDatabase db = mydb.getWritableDatabase();

        //query all the results from the database:
        String[] columns = {News_MyDatabaseOpenHelper.COL_ID, News_MyDatabaseOpenHelper.COL_TITLE, News_MyDatabaseOpenHelper.COL_DESCRIPTION, News_MyDatabaseOpenHelper.COL_URL};
        Cursor results = db.query(false, News_MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        //find the column indices:
        int titleColIndex = results.getColumnIndex(News_MyDatabaseOpenHelper.COL_TITLE);
        int descriptionColIndex = results.getColumnIndex(News_MyDatabaseOpenHelper.COL_DESCRIPTION);
        int idColIndex = results.getColumnIndex(News_MyDatabaseOpenHelper.COL_ID);
        int urlColIndex = results.getColumnIndex(News_MyDatabaseOpenHelper.COL_URL);

        //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {
            String title = results.getString(titleColIndex);
            String description = results.getString(descriptionColIndex);
            String url = results.getString(urlColIndex);
            HashMap<String, String> map = new HashMap<>();
            //map.put(KEY_AUTHOR, jsonObject.optString(KEY_AUTHOR));
            map.put(News_MainActivity.KEY_TITLE, title);
            map.put(News_MainActivity.KEY_DESCRIPTION, description);
            map.put(News_MainActivity.KEY_URL, url);
            //String email = results.getString(emailColumnIndex);
            //long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            articleList.add(map);
        }

        //create an adapter object and send it to the listVIew
        myAdapter = new ArticleAdapter();
        listNews.setAdapter(myAdapter);

        listNews.setOnItemClickListener((parent, view, position, id) -> {
            Log.e("you clicked on :", "item " + position);
            //save the position in case this object gets deleted or updated
            positionClicked = position;
            Intent i = new Intent(this, News_ArticleActivity.class);
            i.putExtra("url", articleList.get(position).get(News_MainActivity.KEY_URL));
            i.putExtra("title", articleList.get(position).get(News_MainActivity.KEY_TITLE));
            i.putExtra("description", articleList.get(position).get(News_MainActivity.KEY_DESCRIPTION));
            startActivity(i);

        });
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_menu_saved, menu);
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

            case R.id.action_deleteAll:
                News_MyDatabaseOpenHelper dbOpener = new News_MyDatabaseOpenHelper(this);
                SQLiteDatabase db = dbOpener.getWritableDatabase();
                db.execSQL("delete from " + News_MyDatabaseOpenHelper.TABLE_NAME);
                Toast.makeText(getApplicationContext(), "All Articles Deleted", Toast.LENGTH_LONG).show();
                myAdapter.notifyDataSetChanged();
                return true;

            case R.id.action_help2:
                Intent myIntent = new Intent(this, News_Help.class);
                this.startActivity(myIntent);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     *
     */
    public class ArticleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return articleList.size();
        }

        public Object getItem(int position) {
            return articleList.indexOf(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int i, View view, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();

            View newView = inflater.inflate(R.layout.news_title_row, parent, false);

            TextView rowTitle = (TextView) newView.findViewById(R.id.title);
            TextView rowDes = (TextView) newView.findViewById(R.id.description);

            HashMap<String, String> data = new HashMap<String, String>();
            data = articleList.get(i);

            rowTitle.setText(data.get(News_MainActivity.KEY_TITLE));
            rowDes.setText(data.get(News_MainActivity.KEY_DESCRIPTION));

            //return the row:
            return newView;


        }

    }
}