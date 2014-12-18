package com.baid.mxchange.m_xchange;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Created by Ish on 9/6/14.
 */
public class SellFragment extends Fragment implements View.OnClickListener{

    EditText price, edition, name;
    Button confirm;
    ListView results;

    double priceVal = -1;
    String nameVal = null;
    String editionVal = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_sell_, container, false);

        price = (EditText) rootView.findViewById(R.id.price);
        edition = (EditText) rootView.findViewById(R.id.row);
        name = (EditText) rootView.findViewById(R.id.section);

        results = (ListView) rootView.findViewById(R.id.spinner3);

        confirm = (Button) rootView.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if(id == confirm.getId()){

            //must enter price
            if(!price.getText().toString().equals("")){

                priceVal = Double.parseDouble(price.getText().toString());
            }
            else{

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Alert ");
                alertDialog.setMessage("Must enter price.");
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
            }

            //edition is optional
            if(!edition.getText().toString().equals("")){

                editionVal = edition.getText().toString();
            }


            //title is required
            if(!name.getText().toString().equals("")){

                nameVal = name.getText().toString();
            }
            else{

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Alert ");
                alertDialog.setMessage("Must enter textbook title.");
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
            }

            if(priceVal != -1 && nameVal != null){

                Log.d("Baid", "Ready to submit textbook");

                MainActivity.searchResults = new SearchTextbooks(MainActivity.searchInterface);
                MainActivity.searchResults.createTextbookEntry("Condition", "Description", "Edition", priceVal, nameVal);

            }
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        priceVal = -1;
        editionVal = null;
        nameVal = null;
    }
}
