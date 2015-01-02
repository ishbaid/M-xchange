package com.baid.mxchange.m_xchange;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReadReviewFragment extends Fragment implements View.OnClickListener{


    TextView courseName, ratingLabel, writeReviewLabel;

    ListView reviewList;

    public ReadReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_read_review, container, false);

        courseName = (TextView) rootView.findViewById(R.id.review_course_name);
        ratingLabel = (TextView) rootView.findViewById(R.id.review_avg);
        writeReviewLabel = (TextView) rootView.findViewById(R.id.write_label);
        reviewList = (ListView) rootView.findViewById(R.id.review_list);

        writeReviewLabel.setOnClickListener(this);

        retrieveReviews();
        return rootView;
    }

    private void retrieveReviews(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Review");
        query.whereEqualTo("course", MainActivity.reviewCourse);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

                if(e != null){

                    Log.d("Baid", "Error: " + e.getMessage());
                    return;
                }

                ArrayList<String> allReview = new ArrayList<String>();
                double rating = 0;
                for(int i = 0; i < parseObjects.size(); i ++){

                    ParseObject review = parseObjects.get(i);

                    rating += review.getDouble("rating");
                    String text = review.getString("text");
                    Boolean anon = review.getBoolean("anonymous");

                    //anonymous by default
                    if(anon == null)
                        anon = new Boolean(true);

                    allReview.add(text);

                }

                if(parseObjects.size() > 0){

                    rating /= parseObjects.size();
                    ratingLabel.setText("Rating: " + rating);
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, allReview);

                // Assign adapter to ListView
                reviewList.setAdapter(adapter);


            }
        });
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if(id == writeReviewLabel.getId()){

            WriteReviewFragment resultsFragment = new WriteReviewFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, resultsFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }
    }
}
