package com.baid.mxchange.m_xchange;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;


public class ResultsFragment extends Fragment implements AdapterView.OnItemClickListener {

    TextView title;
    TextView subtitle;
    ListView queryResults;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_results, container, false);

        title = (TextView) rootView.findViewById(R.id.title);
        subtitle = (TextView) rootView.findViewById(R.id.subtitle);
        queryResults = (ListView) rootView.findViewById(R.id.result_list);


        //set title
        title.setText(MainActivity.course);

        double price = MainActivity.searchResults.getPriceVal();
        if(price != -1){

                subtitle.setText("$" + price);
        }
        else
            subtitle.setText("");

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), MainActivity.searchResults.getData(),
                android.R.layout.simple_list_item_2,
                new String[] {"title", "price"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});
        queryResults.setAdapter(adapter);
        //adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, MainActivity.searchResults.getSearchLabels());



        queryResults.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ContactDialog dialog = new ContactDialog(getActivity());
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);

        retrieveTextbook(dialog, position);



    }

    private void retrieveTextbook(ContactDialog dialog, int position){

        ParseObject book = MainActivity.searchResults.getSearchResults().get(position);
        ParseUser user = book.getParseUser("user");

        String condition = book.getString("condition");
        String description = book.getString("description");
        String edition = book.getString("edition");
        double price = book.getDouble("price");
        boolean showNumber = book.getBoolean("showPhoneNumber");
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
        dialog.setData(condition, description, edition, MainActivity.course, price, showNumber, title, name, numberToDisplay, email);


    }


}
