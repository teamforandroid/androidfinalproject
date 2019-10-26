package android.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void Charger(View view) {
        Intent int1 = new Intent(this, Charger.class);
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
