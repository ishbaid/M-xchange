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

                    final ParseObject ticket = new ParseObject("SportsTicket");
                    ticket.put("game", MainActivity.sportsGame);
                    ticket.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                Log.d("Baid", "Saved ticket!");
                                ParseObject itemBuy = new ParseObject("ItemBuy");
                                itemBuy.put("maxPay", priceVal);
                                itemBuy.put("sportsTicket", ticket);
                                itemBuy.put("user", ParseUser.getCurrentUser());

                                itemBuy.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e == null){

                                            Log.d("Baid", "Saved ItemBuy!");

                                            Log.d("Baid", "Ready to create parseObjects");
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

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ItemSell");
        //should contain correct textbook
        query.whereMatchesQuery("sportsTicket", innerQuery);
        //should be less than max pay
        query.whereLessThan("cost", priceVal);
        query.include("user");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){

                    Log.d("Baid", "Found " + objects.size() + " tickets");
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
    }
}
