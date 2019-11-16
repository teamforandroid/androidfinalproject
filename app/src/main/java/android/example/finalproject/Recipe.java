package android.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

public class Recipe extends AppCompatActivity {

    public static final String ACTIVITY_NAME = "RecipeSearch";

      //declaration of search button
     // search entry of editText view
     // ListView  of search result
     //progressBar to show process

     Button searchItemButton;
     EditText recipeItemText;
     ListView recipeList;
     ProgressBar recipeProgress;

    private String search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        recipeItemText = (EditText) findViewById(R.id.recipeItemText);
        searchItemButton = (Button) findViewById(R.id.searchItemButton);
        recipeList = (ListView) findViewById(R.id.recipeList);

        //to set the progressBar’s visibility
        recipeProgress = (ProgressBar)findViewById(R.id.progress);
        recipeProgress.setVisibility(View.VISIBLE);

//recipe ??
        //RecipeSearch query = new RecipeSearch();
        //query.execute();
               //search event handlers

 //request recipe url
        searchItemButton.setOnClickListener(click-> {
                    ///get input from recipeitemtext /
                    search = recipeItemText.getText().toString();

                    /// should   call http request


                    recipeList.setText("");
                });







        //innerclass that extends AsyncTask
        private class RecipeQuery extends AsyncTask<String, String, String> {
            private Double recipeTitle;
            private Double recipeURL;
            private Bitmap bitmap;

        }



    }


 /*
    // overridden methods with built-in logging.

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
        //??
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
*/
}
