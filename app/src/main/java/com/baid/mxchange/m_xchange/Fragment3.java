package com.baid.mxchange.m_xchange;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ish on 9/6/14.
 */
public class Fragment3 extends Fragment {

    EditText price, name;
    Button confirm;
    ListView results;
    double priceValue = -1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_buy_book, container, false);

        price = (EditText) rootView.findViewById(R.id.price);
        name = (EditText) rootView.findViewById(R.id.last);
        results = (ListView) rootView.findViewById(R.id.spinner3);

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
                                            search();
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

    private void search(){

        ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery("Textbook");
        innerQuery.whereEqualTo("class", MainActivity.course);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ItemSell");
        //should contain correct textbook
        query.whereMatchesQuery("textbook", innerQuery);
        //should be less than max pay
        query.whereLessThan("cost", priceValue);
        query.include("user");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){

                    Log.d("Baid", "Found " + objects.size() + " matches");
                    //creates list of matches
                    final List<ParseUser> matches = new ArrayList<ParseUser>();
                    final List<String> names = new ArrayList<String>();
                    final List<String> label = new ArrayList<String>();
                    final List<Double> payments = new ArrayList<Double>();
                    for(int i = 0; i < objects.size(); i ++){

                        ParseUser match = objects.get(i).getParseUser("user");
                        matches.add(match);


                        String name = match.getString("firstName");
                        name += " " + match.getString("lastName");
                        names.add(name);

                        Double payment = objects.get(i).getDouble("cost");
                        payments.add(payment);
                        label.add(name + "\t$" + payment);

                    }

                    results.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, label));
                    results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                            Log.d("Baid", "Position " + position + " pressed");
                            ParseUser person = matches.get(position);
                            String name = names.get(position);
                            String email = person.getString("email");
                            String phone = person.getString("phoneNumber");
                            Double payment = payments.get(position);

                            Log.d("Baid", name + ", " + email + ", " + phone);

                            Intent intent = new Intent(getActivity(), ViewProfileActivity.class);
                            intent.putExtra("name", name);
                            intent.putExtra("email", email);
                            intent.putExtra("number", phone);
                            intent.putExtra("pay", payment);
                            startActivity(intent);

                        }
                    });
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        priceValue = -1;
    }
}
