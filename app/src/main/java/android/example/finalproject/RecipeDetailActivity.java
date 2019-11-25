package android.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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


//get image---can not get image
//        byte[] byteArray =getIntent().getByteArrayExtra("image");
//        image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//        imageView.setImageBitmap(image);


    }


}
