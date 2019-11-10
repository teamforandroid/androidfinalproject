package android.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.menu_charger:
                startActivity(new Intent(MainActivity.this, Charger_stations.class));
                break;
            case R.id.menu_recipe:
                startActivity(new Intent(MainActivity.this, Recipe.class));
                break;
            case R.id.menu_currency:
                startActivity(new Intent(MainActivity.this, Currency.class));
                break;
            case R.id.menu_news:
                startActivity(new Intent(MainActivity.this, News.class));
                break;
            default:
                break;
        }
        return true;
    }



    public void Charger(View view) {
        Intent int1 = new Intent(this, Charger_stations.class);
        startActivity(int1);

    }
    public void Recipe(View view) {
        Intent int2 = new Intent(this, Recipe.class);
        startActivity(int2);
    }


    public void Currency(View view) {
        Intent int3 = new Intent(this, Currency.class);
        startActivity(int3);

    }


    public void News(View view) {
        Intent int4 = new Intent(this, News.class);
        startActivity(int4);

    }


}
