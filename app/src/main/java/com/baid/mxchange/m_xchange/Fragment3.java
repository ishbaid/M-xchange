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

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by Ish on 9/6/14.
 */
public class Fragment3 extends Fragment {

    EditText price, name;
    Button confirm;
    double priceValue = -1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_buy_book, container, false);

        price = (EditText) rootView.findViewById(R.id.first);
        name = (EditText) rootView.findViewById(R.id.last);


        confirm = (Button) rootView.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {


                //make sure price isn't empty
                if(!price.getText().toString().equals(""))
                    priceValue = Double.parseDouble(price.getText().toString());
                //enforces requirement of price
                else{

                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Alert ");
                    alertDialog.setMessage("Must enter price.");
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();
                }

                String nameValue = null;
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
                if(priceValue > -1  && nameValue != null){

                    Log.d("Baid", "Ready to create parseobjects");
                    final ParseObject textbook = new ParseObject("Textbook");
                    textbook.put("name", nameValue);
                    textbook.put("class", MainActivity.course);
                    textbook.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){

                                Log.d("Baid", "Saved book!");

                                ParseObject itemBuy = new ParseObject("ItemBuy");
                                //passes in current user
                                itemBuy.put("user", ParseUser.getCurrentUser());
                                itemBuy.put("maxPay", priceValue);
                                itemBuy.put("textbook", textbook);

                                itemBuy.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null){

                                            Log.d("Baid", "Saved item buy!");
                                        }
                                    }
                                });

                            }
                        }
                    });


                }

            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        priceValue = -1;
    }
}
