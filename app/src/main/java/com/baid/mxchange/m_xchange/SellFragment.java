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
public class SellFragment extends Fragment implements View.OnClickListener{

    EditText price, edition, name, condition, description;
    Button confirm;
    double priceVal = -1;
    String nameVal = null;
    String editionVal = null;
    Switch showNumber;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_sell_, container, false);

        price = (EditText) rootView.findViewById(R.id.price_edit);
        edition = (EditText) rootView.findViewById(R.id.edition_edit);
        name = (EditText) rootView.findViewById(R.id.title_edit);
        condition = (EditText) rootView.findViewById(R.id.condition_edit);
        description = (EditText) rootView.findViewById(R.id.description_edit);

        confirm = (Button) rootView.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);

        showNumber = (Switch) rootView.findViewById(R.id.show_number);


        return rootView;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if(id == confirm.getId()){


            //hide keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

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

            if(priceVal >= 0 && nameVal != null){

                Log.d("Baid", "Ready to submit textbook");



                String conditionText = condition.getText().toString();
                String descriptionText = description.getText().toString();
                String editionText = edition.getText().toString();

                MainActivity.searchResults = new SearchTextbooks(MainActivity.searchInterface);

                MainActivity.searchResults.createTextbookEntry(conditionText, descriptionText, editionText, priceVal, nameVal, showNumber.isChecked());

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
