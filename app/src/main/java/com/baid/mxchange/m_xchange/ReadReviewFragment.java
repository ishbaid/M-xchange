package com.baid.mxchange.m_xchange;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReadReviewFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{


    TextView courseName, ratingLabel, writeReviewLabel;

    ListView reviewList;

    List<ParseObject> allReviews;

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

        String title = MainActivity.reviewCourse.getString("subject") + MainActivity.reviewCourse.getString("catalogNumber");
        courseName.setText(title);

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

                allReviews = parseObjects;

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
                    DecimalFormat df = new DecimalFormat("#.##");
                    ratingLabel.setText("Average Rating: " + df.format(rating));
                }

                if(allReview == null){

                    Log.d("Baid", "allreview is null");
                    return;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, allReview);

                // Assign adapter to ListView
                reviewList.setAdapter(adapter);
                reviewList.setOnItemClickListener(ReadReviewFragment.this);


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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(allReviews == null)
            return;
        ParseObject userReview = allReviews.get(position);
        if(userReview == null)
            return;

        String text = userReview.getString("text");
        ParseUser user = userReview.getParseUser("user");
        String userName = "Anonymous";
        Double rating = userReview.getDouble("rating");
        Boolean anon = userReview.getBoolean("anonymous");

        //anonymous by default
        if(anon == null)
            anon = new Boolean(true);

        try{

            user.fetchIfNeeded();
            userName = user.getString("firstName") + " " + user.getString("lastName");


        }catch (Exception e){

            Log.d("Baid", e.getMessage());
        }

        View dialogView = View.inflate(getActivity(), R.layout.review_dialog, null);

        RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.ratingBar);
        ratingBar.setIsIndicator(true);
        TextView messageLabel = (TextView) dialogView.findViewById(R.id.textView);
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setView(dialogView);

        if(rating != null) {

            Log.d("Baid", "Num stars is " + ratingBar.getNumStars());
            Log.d("Baid", "Step stars is " + ratingBar.getStepSize());
            Log.d("Baid", "Rating is " + rating.floatValue());
            ratingBar.setRating(rating.floatValue());
        }

        //displays review content
        if(anon)
            alert.setTitle("Anonymous");
        else
            alert.setTitle(userName);
        if(text != null)
            messageLabel.setText(text);
        else
            messageLabel.setText("");

        //creates dismiss button
        alert.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {



            }
        });

        alert.show();

    }
}
