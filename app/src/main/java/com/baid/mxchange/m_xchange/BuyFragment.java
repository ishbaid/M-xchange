package com.baid.mxchange.m_xchange;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

/**
 * Created by Ish on 9/6/14.
 */
public class BuyFragment extends Fragment implements View.OnClickListener {

    EditText price, name;
    Button confirm;
    double priceValue = -1;
    String nameValue = null;

    Switch showNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_buy_book, container, false);

        price = (EditText) rootView.findViewById(R.id.price_edit);
        name = (EditText) rootView.findViewById(R.id.last);

        confirm = (Button) rootView.findViewById(R.id.confirm);

        confirm.setOnClickListener(this);


        showNumber = (Switch) rootView.findViewById(R.id.show_number);


        return rootView;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        //search textbooks
        //display results
        //create new testbook entry
        if(id == confirm.getId()){


            //hide keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            //make sure price isn't empty
            if(!price.getText().toString().equals(""))
                priceValue = Double.parseDouble(price.getText().toString());
            //we require price
            else{

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Alert ");
                alertDialog.setMessage("Must enter price.");
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();

            }


           nameValue = null;
            if(!name.getText().toString().equals("")){

                nameValue = name.getText().toString();
            }
            //textbook name is required
            else{

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Alert ");
                alertDialog.setMessage("Must enter textbook title.");
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
            }

            //if everything was entered correctly, we can create parse objects
            if(priceValue >= 0 && nameValue != null){


                //Ready to start searching
                Log.d("Baid", "Searching for books");


                String conditionText = "";
                String editionText = "";
                String descriptionText = "";

                MainActivity.searchResults = new SearchTextbooks(MainActivity.searchInterface);
                MainActivity.searchResults.createTextbookEntry(conditionText, descriptionText, editionText, priceValue, nameValue, showNumber.isChecked());

            }

        }

    }


    @Override
    public void onResume() {
        super.onResume();
        priceValue = -1;
        nameValue = null;
    }
}
