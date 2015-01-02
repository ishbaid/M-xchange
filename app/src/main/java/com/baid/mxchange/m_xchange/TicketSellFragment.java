package com.baid.mxchange.m_xchange;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

/**
 * Created by Ish on 9/7/14.
 */
public class TicketSellFragment extends Fragment {

    EditText price, row, section;
    Button confirm;
    double priceVal = -1;
    int rVal, sVal = -1;
    Switch showNumber;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tick_sell, container, false);

        showNumber = (Switch) rootView.findViewById(R.id.show_number);
        price = (EditText) rootView.findViewById(R.id.price_edit);
        row = (EditText) rootView.findViewById(R.id.row_edit);
        section = (EditText) rootView.findViewById(R.id.section_edit);
        confirm = (Button) rootView.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {


                //hide keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);


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

                if(priceVal >= 0) {

                    MainActivity.ticketResults = new SearchTickets(MainActivity.searchInterface);
                    MainActivity.ticketResults.createTicketEntry(priceVal, rVal, sVal, showNumber.isChecked());

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
