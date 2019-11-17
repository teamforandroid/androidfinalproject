package android.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class RecipeDetailActivity extends AppCompatActivity {

    private String title;
    private  String url;
    private Bitmap image;
    TextView titleTextView;
    ImageView imageView;
    private Button goSavedButton;
 Button saveRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        initView();

//insert  setonclicklistener for goto next page

   //     goSavedButton=finndViewById(R.id.savedmyfavourite);
    //    if (goSavedButton != null) {
    //        goSavedButton.setOnClickListener(v -> {
    //            Intent goToPage3 = new Intent(RecipeDetailActivity.this, SaveFavoriteActivity.class);
     //           startActivity(goToPage3);
     //       });
    }
//To bind title image url
    private void initView(){

         Intent intent = getIntent();
         title=intent.getStringExtra("title");
         url=intent.getStringExtra("url");
         String imageUrl=intent.getStringExtra("imageUrl");
         try {
       ////load image
            URL u = new URL(imageUrl);
            image = BitmapFactory.decodeStream(u.openConnection().getInputStream());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /// link to recipe item by url to web

///


    ////save faveriat
     saveRecipe.setOnClickListener(click -> {
        TextView titlefave = findViewById(R.id.title);
////should change to string??
        String favetitle=titlefave.getText().toString();
        //add to the database and get the new ID
        ContentValues newfaverecipe = new ContentValues();
        //put string name in the NAME column:
        newfaverecipe.put(SaveFavoriteActivity.COL_MASSAGE,favetitle );

        //insert in the database:
        long newId = db.insert(SveFavouriteActivity.TABLE_NAME, null, newfaverecipe);
        //Message messageObj = new Message(message, false,newId);
       // objects.add(messageObj);
      //  msgText.setText("");
        //myAdapter.notifyDataSetChanged();//update yourself
    });

    ///To next page  SaveFavoriteActivity
/
}
