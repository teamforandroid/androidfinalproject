package android.example.finalproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;


/**
 * Help class used for display App version and help information
 */
public class News_Help extends AppCompatActivity {
    ImageView github;
    private Button helpButton;
    private Button alertButton;
    private TextView alertTextView, tv1;
    private EditText et1;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity_help);

        github = findViewById(R.id.github);

        helpButton = findViewById(R.id.button_help);
        alertButton = (Button) findViewById(R.id.button_alert);

        alertTextView = (TextView) findViewById(R.id.textView_alert);
        tv1 = findViewById(R.id.textView_logo);
        et1 = findViewById(R.id.textView_message);


        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(new News_FragmentOne(), false, "one");
            }
        });

        et1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et1.setTextColor(Color.RED);
                String displayString = "You typed '" + et1.getText().toString() +
                        "'  Thanks for leaving a message!";

                Toast msg = Toast.makeText(getBaseContext(), displayString,
                        Toast.LENGTH_LONG);
                msg.show();
            }
        });

        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://github.com/jichengkevin";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "The LIke! Button was clicked. Thank you!", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        alertButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(News_Help.this);


                builder.setCancelable(true);

                builder.setTitle("App Bug Alert");

                builder.setMessage("This is a bug in the App");


                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();

                    }

                });


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialogInterface, int i) {

                        alertTextView.setVisibility(View.VISIBLE);

                    }

                });

                builder.show();

            }

        });

    }

    /**
     *
     * @param fragment
     * @param addToBackStack
     * @param tag
     */
    public void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.frame_container, fragment, tag);
        ft.commitAllowingStateLoss();
    }
}
