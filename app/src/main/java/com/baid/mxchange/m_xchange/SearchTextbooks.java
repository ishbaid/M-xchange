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
 * Created by Ish on 12/16/14.
 */

/*TODO: If user already has a textbook request, update it.
TODO: If user is a result, remove them
TODO: Don't show duplicate users
    */
public class SearchTextbooks {

    //used to tell fragment when query has finished
    SearchInterface sInterface;

    List<Map<String, String>> data;

    List <ParseObject> searchResults;
    ArrayList<String> searchLabels;
    ArrayList<String> priceLabels;
    double priceVal = -1;

    public SearchTextbooks(SearchInterface si){


        sInterface = si;
        sInterface.onStart();
        data = new ArrayList<Map<String, String>>();
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

                    Map<String, String> datum = new HashMap<String, String>(2);

                    ParseObject book = parseObjects.get(i);
                    String title = book.getString("title");
                    Double price = book.getDouble("price");

                    //title
                    if(title != null)
                        datum.put("title", title);
                    else
                        datum.put("title", "Unknown");

                    //price
                    if(price != null &&  price > 0)
                        datum.put("price", price + "");
                    else
                        datum.put("price", "Unknown");
                    data.add(datum);
                    if(title != null)
                        searchLabels.add(title);
                    if(price != null)
                        priceLabels.add(price + "");

                    //labels.add(className + " " + title);

                    ParseUser user = book.getParseUser("user");

                    //if user's information is needed, we will fetch is at that time
                    try{

                        if(user != null)
                            user.fetchIfNeeded();
                    }catch (ParseException e2){

                        Log.d("Baid", e2.getMessage());
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

    public void createTextbookEntry(String condition, String desc, String edition, double priceValue, final String title, boolean showNumber){

        ParseObject textbook = new ParseObject("Textbook");
        textbook.put("class", MainActivity.course);
        textbook.put("course", MainActivity.courseObject);
        textbook.put("condition", condition);
        textbook.put("description", desc);
        textbook.put("edition", edition);
        textbook.put("price", priceValue);
        textbook.put("selling", !MainActivity.buy);
        textbook.put("showPhoneNumber", showNumber);
        textbook.put("title", title);
        textbook.put("user", ParseUser.getCurrentUser());

        priceVal = priceValue;

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
