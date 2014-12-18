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
 * Created by Ish on 12/16/14.
 */

/*TODO: If user already has a textbook request, update it.
TODO: If user is a result, remove them
TODO: Don't show duplicate users
    */
public class SearchTextbooks {

    //used to tell fragment when query has finished
    SearchInterface sInterface;

    List <ParseObject> searchResults;
    ArrayList<String> searchLabels;
    ArrayList<String> priceLabels;

    public SearchTextbooks(SearchInterface si){

        sInterface = si;

        searchResults = new ArrayList<ParseObject>();
        searchLabels = new ArrayList<String>();
        priceLabels = new ArrayList<String>();

        search();
    }

    private void search(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Textbook");
        query.whereEqualTo("class", MainActivity.course);
        query.whereEqualTo("selling", MainActivity.buy);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {


                if(e != null){

                    Log.d("Baid", "Error: " + e.getMessage());
                }

                searchResults = parseObjects;

                if(parseObjects.size() == 0)
                    searchLabels.add("No results at this time.");

                for(int i = 0; i < parseObjects.size(); i ++){

                    ParseObject book = parseObjects.get(i);
                    String title = book.getString("title");
                    double price = book.getDouble("price");

                    searchLabels.add(title);
                    priceLabels.add(price + "");

                    //labels.add(className + " " + title);

                    ParseUser user = book.getParseUser("user");

                    //if user's information is needed, we will fetch is at that time
                    try{

                        if(user != null)
                            user.fetchIfNeeded();
                    }catch (ParseException e2){

                        Log.d("Baid", e.getMessage());
                    }

                    if(user != null){

                        Log.d("Baid", "found user");


                    }
                    else{

                        Log.d("Baid", "NOT FOUND");
                    }
                }

                //callback: Query is complete and results can be retieved
                sInterface.onSearchComplete();

            }
        });
    }

    public void createTextbookEntry(String condition, String desc, String edition, double priceValue, final String title){

        ParseObject textbook = new ParseObject("Textbook");
        textbook.put("class", MainActivity.course);
        textbook.put("condition", condition);
        textbook.put("description", desc);
        textbook.put("edition", edition);
        textbook.put("price", priceValue);
        textbook.put("selling", !MainActivity.buy);
        textbook.put("showPhoneNumber", false);
        textbook.put("title", title);
        textbook.put("user", ParseUser.getCurrentUser());



        textbook.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e != null) {

                    Log.d("Baid", "Error: " + e.getMessage());
                    return;
                }

                Log.d("Baid", "Textbook: " + title + " saved!");

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
