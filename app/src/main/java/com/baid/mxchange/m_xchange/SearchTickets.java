package com.baid.mxchange.m_xchange;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ish on 12/17/14.
 */
public class SearchTickets {

    SearchInterface searchInterface;

    List <ParseObject> searchResults;
    ArrayList<String> searchLabels;

    public SearchTickets(SearchInterface si){

        searchInterface = si;
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

                    ParseObject po = parseObjects.get(i);
                    ParseObject pGame = po.getParseObject("game");

                    try {

                        if(pGame != null) {

                            pGame.fetchIfNeeded();
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

    public void createTicketEntry(double price, int row, int section){

        ParseObject ticketEntry = new ParseObject("Ticket");
        ticketEntry.put("game", MainActivity.sportsGame);
        ticketEntry.put("price", price);
        ticketEntry.put("row", row);
        ticketEntry.put("section", section);
        ticketEntry.put("selling", MainActivity.buy);
        ticketEntry.put("showPhoneNumber", false);
        ticketEntry.put("user", ParseUser.getCurrentUser());

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

    public ArrayList<String> getSearchLabels(){

        return searchLabels;
    }

   public List<ParseObject> getSearchResults(){

       return searchResults;
   }


}
