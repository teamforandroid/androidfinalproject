/**
 * Assignment : final project - recipe
 * Author ：zheyuan li
 * date : Nov 29 2019
 */
package android.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecipeDetailActivity extends AppCompatActivity {

    private String title;
    private  String url;
    private Bitmap image;
    TextView titleTextView;
    ImageView imageView;
    TextView urlText;
    private Button goSavedButton;
 Button saveRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
//bind  title image url
        titleTextView=findViewById(R.id.recipeTitleValue);
        imageView=findViewById(R.id.recipeImage);
        urlText=findViewById(R.id.recipeLink);
//get value of title  url image
        title= getIntent().getStringExtra("title");
        titleTextView.setText(title);
        url=getIntent().getStringExtra("url");
        urlText.setText(url);
        urlText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipejumpUrl(url);
            }
        });
      /*
      //get image-
        byte[] byteArray =getIntent().getByteArrayExtra("image");
        image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        imageView.setImageBitmap(image);
        findViewById(R.id.button_longin).setOnClickListener(new View.OnClickListener() {
            @Override
         public void onClick(View view) {
         Intent intent = new Intent(RecipeDetailActivity.this, SaveFavoriteActivity.class);
          intent.putExtra("email",emailAddressSaved);
          startActivity(intent);
           }
       */
    }

    private void recipejumpUrl(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }


}
