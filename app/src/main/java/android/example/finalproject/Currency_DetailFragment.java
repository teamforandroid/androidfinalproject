package android.example.finalproject;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class Currency_DetailFragment extends Fragment {

    private boolean isTablet;
    private Bundle dataFromActivity;
    private long id;

    public void setTablet(boolean tablet) { isTablet = tablet; }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(Currency.ITEM_ID );

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.currency_detail_fragment, container, false);

        //From
        TextView from = (TextView)result.findViewById(R.id.From);
        String content = dataFromActivity.getString(Currency.ITEM_SELECTED);
        String[] contentDetail = content.split("\\s");
        from.setText(contentDetail[1]);

        //From Amount
        TextView fromAmount = (TextView)result.findViewById(R.id.FromAmount);
        fromAmount.setText(contentDetail[0].substring(2));

        TextView to = (TextView)result.findViewById(R.id.To);
        to.setText(contentDetail[6]);

        TextView toAmount = (TextView)result.findViewById(R.id.ToAmount);
        toAmount.setText(contentDetail[5]);


        // get the delete button, and add a click listener:
        Button deleteButton = (Button)result.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener( clk -> {

            if(isTablet) { //both the list and details are on the screen:
                Currency parent = (Currency)getActivity();
                parent.deleteMessageId((int)id); //this deletes the item and updates the list


                //now remove the fragment since you deleted it from the database:
                // this is the object to be removed, so remove(this):
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                Currency_EmptyActivity parent = (Currency_EmptyActivity) getActivity();
                Intent backToFragmentExample = new Intent();
                backToFragmentExample.putExtra(Currency.ITEM_ID, dataFromActivity.getLong(Currency.ITEM_ID ));

                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });
        return result;
    }
}
