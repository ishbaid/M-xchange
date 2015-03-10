package com.baid.mxchange.m_xchange;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {


    ListView results;
    List<ParseObject> textbookResults;
    Button reviews, textbooks;
    public DashboardFragment() {
        // Required empty public constructor
    }

    List<Map<String, String>> data;
    final static int QUERY_LIMIT = 5;
    final static String TAG = "LauncherFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        results = (ListView) rootView.findViewById(R.id.results_listview);
        results.setOnItemClickListener(this);
        data = new ArrayList<Map<String, String>>();

        reviews = (Button) rootView.findViewById(R.id.read_reviews);
        textbooks = (Button) rootView.findViewById(R.id.find_textbooks);

        reviews.setOnClickListener(this);
        textbooks.setOnClickListener(this);



        return rootView;
    }

    private void search(){

        new AsyncTask<Void, Void, Object>(){

            ProgressDialog dialog;

            @Override
            protected Object doInBackground(Void... params) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Textbook");
                query.whereEqualTo("selling", true);
                query.setLimit(QUERY_LIMIT);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {

                        if(e != null){

                            Log.d(TAG, "Error!");
                            return;
                        }

                        textbookResults = parseObjects;
                        if(parseObjects.size() == 0)
                            Log.d(TAG, "No results");

                        for(int i = 0; i < parseObjects.size(); i ++){

                            Map<String, String> datum = new HashMap<String, String>(2);

                            ParseObject book = parseObjects.get(i);
                            String title = book.getString("title");
                            Double price = book.getDouble("price");
                            DecimalFormat df = new DecimalFormat("#.00");
                            String className = book.getString("class");

                            //title
                            if(title != null) {

                                if(!title.equals(""))
                                    datum.put("title", title);
                                else
                                    datum.put("title", "Unknown");
                            }
                            else
                                datum.put("title", "Unknown");

                            //price
                            if(price != null &&  price > 0)
                                datum.put("price", "$" + df.format(price) + "| " + className);
                            else
                                datum.put("price", "Unknown| " + className);
                            data.add(datum);

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
                    }

                });
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(getActivity(), "",
                        "Loading. Please wait...", true);
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                dialog.dismiss();

                SimpleAdapter adapter = new SimpleAdapter(getActivity(), data,
                        android.R.layout.simple_list_item_2,
                        new String[] {"title", "price"},
                        new int[] {android.R.id.text1,
                                android.R.id.text2});
                results.setAdapter(adapter);
            }
        }.execute();


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ContactDialog dialog = new ContactDialog(getActivity());
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);

        retrieveTextbook(dialog, position);

    }

    private void retrieveTextbook(ContactDialog dialog, int position){

        ParseObject book = textbookResults.get(position);
        ParseUser user = book.getParseUser("user");

        String condition = book.getString("condition");
        String description = book.getString("description");
        String edition = book.getString("edition");
        double price = book.getDouble("price");
        String title = book.getString("title");
        String name = null;
        Number number = 0;
        String email = null;

        try {

            if(user != null) {

                user.fetchIfNeeded();
                name = user.getString("firstName") + " " + user.getString("lastName");
                number = user.getNumber("phone");
                email = user.getEmail();
                Log.d("Baid", "User number is " + number);
            }

        }catch (Exception e){

            Log.d("Baid", "Error: " + e.getMessage());
        }

        double numberToDisplay = -1;

        if(number != null) {

            numberToDisplay = number.doubleValue();
            Log.d("Baid", "String number is " + number.toString());

        }

        //TODO: shownumber argument is undefined
        dialog.setData(condition, description, edition, MainActivity.course, price, false, title, name, numberToDisplay, email);

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        //reviews
        if(id == reviews.getId()){

            ReviewSelectFragment resultsFragment = new ReviewSelectFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, resultsFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }
        //textbooks
        else if(id == textbooks.getId()){

            SelectCourseFragment resultsFragment = new SelectCourseFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, resultsFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Fragment onResume running");
        MainActivity.buy = true;
        search();
    }
}
