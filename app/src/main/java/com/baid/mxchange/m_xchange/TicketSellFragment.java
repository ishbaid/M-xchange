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


                    if(!row.getText().toString().equals("")){

                        rVal = Integer.parseInt(row.getText().toString());
                    }
                    if(!section.getText().toString().equals("")){

                        sVal = Integer.parseInt(section.getText().toString());
                    }

                    final ParseObject ticket = new ParseObject("SportsTicket");
                    ticket.put("game", MainActivity.sportsGame);
                    ticket.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null){

                                Log.d("Baid", "Ticket saved!!");
                                ParseObject itemSell = new ParseObject("ItemSell");
                                itemSell.put("cost", priceVal);
                                itemSell.put("user", ParseUser.getCurrentUser());
                                itemSell.put("sportsTicket", ticket);
                                itemSell.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {

                                        if(e == null){

                                            Log.d("Baid", "Saved ItemSell!");
                                            Log.d("Baid", "Ready to make parseObjects");
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

        ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery("SportsTicket");
        innerQuery.whereEqualTo("game", MainActivity.sportsGame);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ItemBuy");
        //should contain correct textbook
        query.whereMatchesQuery("sportsTicket", innerQuery);
        //should be willing to pay cost requested
        query.whereGreaterThan("maxPay", priceVal);
        //we are going to need to access the user object, so we must include the user field in our query so we can access it later
        query.include("user");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){

                    if(objects.size() == 0){

                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Message");
                        alertDialog.setMessage("Unfortunately, we have found no matches at this time. We will contact you when a match has been found.");
                        alertDialog.setCanceledOnTouchOutside(true);
                        alertDialog.show();
                    }

                    Log.d("Baid", "Found " + objects.size() + " tickets");
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

                        Double payment = objects.get(i).getDouble("maxPay");
                        payments.add(payment);
                        label.add(name + "\t$" + payment);



                    }

                    results.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, label));
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
        priceVal = -1;
        rVal = -1;
        sVal = -1;
    }
}
