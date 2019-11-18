package android.example.finalproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Currency_toolbar extends AppCompatActivity {
    Toolbar tBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        tBar = (Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(tBar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.currency_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            //Back button
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
}
