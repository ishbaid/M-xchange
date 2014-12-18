package com.baid.mxchange.m_xchange;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Created by Ish on 9/7/14.
 */
public class TicketSellFragment extends Fragment {

    EditText price, row, section;
    Button confirm;
    ListView results;
    double priceVal = -1;
    int rVal, sVal = -1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tick_sell, container, false);

        price = (EditText) rootView.findViewById(R.id.price);
        row = (EditText) rootView.findViewById(R.id.row);
        section = (EditText) rootView.findViewById(R.id.section);
        results = (ListView) rootView.findViewById(R.id.spinner3);
        confirm = (Button) rootView.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                //price is mandatory
                if(!price.getText().toString().equals(""))
                    priceVal = Double.parseDouble(price.getText().toString());
                else {

                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Alert ");
                    alertDialog.setMessage("Must enter price.");
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();
                }

                //only price is mandatory
                if(priceVal > -1){


                   MainActivity.ticketResults = new SearchTickets(MainActivity.searchInterface);
                    MainActivity.ticketResults.createTicketEntry(priceVal, rVal, sVal);
                }
            }
        });


        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        priceVal = -1;
        rVal = -1;
        sVal = -1;
    }
}
