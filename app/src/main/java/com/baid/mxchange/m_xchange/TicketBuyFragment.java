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
public class TicketBuyFragment extends Fragment {

    ListView results;
    EditText price;
    Button confirm;

    double priceVal = -1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tick_buy, container, false);

        results = (ListView) rootView.findViewById(R.id.spinner3);
        price = (EditText) rootView.findViewById(R.id.price);
        confirm = (Button) rootView.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                if(!price.getText().toString().equals(""))
                    priceVal = Double.parseDouble(price.getText().toString());
                else{

                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Alert ");
                    alertDialog.setMessage("Must enter price.");
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();
                }

                if(priceVal > -1){

                    MainActivity.ticketResults = new SearchTickets(MainActivity.searchInterface);
                    MainActivity.ticketResults.createTicketEntry(priceVal, -1, -1);
                }
            }
        });

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        priceVal = -1;
    }
}
