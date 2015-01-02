package com.baid.mxchange.m_xchange;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ish on 12/17/14.
 */
public class SearchTickets {

    SearchInterface searchInterface;

    List<Map<String, String>> data;
    List <ParseObject> searchResults;
    ArrayList<String> searchLabels;
    double priceVal = -1;

    public SearchTickets(SearchInterface si){

        searchInterface = si;
        searchInterface.onStart();

        data = new ArrayList<Map<String, String>>();
        searchResults = new ArrayList<ParseObject>();
        searchLabels = new ArrayList<String>();

        search();
    }

    private void search(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ticket");
        query.whereEqualTo("game", MainActivity.sportsGame);
        query.whereEqualTo("selling", MainActivity.buy);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

                if(e != null){

                    Log.d("Baid", "Error: " + e.getMessage());
                    return;
                }

                searchResults = parseObjects;
                Log.d("Baid", "Found " + parseObjects.size() + " matches");

                for(int i = 0; i < parseObjects.size(); i ++){

                    Map<String, String> datum = new HashMap<String, String>(2);

                    ParseObject po = parseObjects.get(i);
                    ParseObject pGame = po.getParseObject("game");

                    try {

                        if(pGame != null) {

                            pGame.fetchIfNeeded();

                            String opponent = pGame.getString("opponent");
                            Double price = po.getDouble("price");

                            //game
                            if(opponent != null)
                                datum.put("game", opponent);
                            else
                                datum.put("game", "Unknown");

                            //price
                            if(price != null)
                                datum.put("price", price + "");
                            else
                                datum.put("price", "Unknown");

                            data.add(datum);

                            searchLabels.add(pGame.getString("opponent"));

                        }

                    }catch(Exception e2){

                        Log.d("Baid", e.getMessage());
                    }


                }

               searchInterface.onSearchComplete();

            }
        });

    }

    public void createTicketEntry(double price, int row, int section, boolean showNumber){

        ParseObject ticketEntry = new ParseObject("Ticket");
        ticketEntry.put("game", MainActivity.sportsGame);
        ticketEntry.put("price", price);
        ticketEntry.put("row", row);
        ticketEntry.put("section", section);
        ticketEntry.put("selling", !MainActivity.buy);
        ticketEntry.put("showPhoneNumber", showNumber);
        ticketEntry.put("user", ParseUser.getCurrentUser());

        priceVal = price;

        ticketEntry.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e != null){

                    Log.d("Baid", e.getMessage());
                    return;
                }

                Log.d("Baid", "Ticket has been saved!");
            }
        });

    }

    public List<Map<String, String>> getData(){

        return data;
    }

    public double getPriceVal(){

        return priceVal;
    }

    public ArrayList<String> getSearchLabels(){

        return searchLabels;
    }

   public List<ParseObject> getSearchResults(){

       return searchResults;
   }


}
