package com.baid.mxchange.m_xchange;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewViewerFragment extends Fragment{


    private ViewPager pager = null;
    private MainPagerAdapter pagerAdapter = null;
    final static int QUERY_LIMIT = 5;
    final static String TAG = "ReviewViewFragment";

    public ReviewViewerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_review_viewer, container, false);

        pagerAdapter = new MainPagerAdapter();
        pager = (ViewPager) rootView.findViewById(R.id.view_pager);
        pager.setAdapter (pagerAdapter);

//        for (int i = 0; i < QUERY_LIMIT; i ++) {
//
//            LayoutInflater ifl = getActivity().getLayoutInflater();
//            LinearLayout v0 = (LinearLayout) ifl.inflate(R.layout.review_layout, null);
//            pagerAdapter.addView(v0, 0);
//        }
//        pagerAdapter.notifyDataSetChanged();

        loadReviews();
        return rootView;
    }

    private void loadReviews(){


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Review");
        query.setLimit(QUERY_LIMIT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e != null){

                    Log.d(TAG, "Error!");
                    return;
                }

               for(int i = 0; i < parseObjects.size(); i ++) {

                    LayoutInflater ifl = getActivity().getLayoutInflater();
                    LinearLayout v0 = (LinearLayout) ifl.inflate(R.layout.review_layout, null);

                    ParseObject review = parseObjects.get(i);

                    TextView course, professor, workload, text;
                    RatingBar score;
                    Button left, right;

                    course = (TextView) v0.findViewById(R.id.review_title);
                    professor = (TextView) v0.findViewById(R.id.professor);
                    workload = (TextView) v0.findViewById(R.id.workload);
                    text = (TextView) v0.findViewById(R.id.review_text);

                    score = (RatingBar) v0.findViewById(R.id.rating);
                   left = (Button) v0.findViewById(R.id.left_button);
                   right = (Button) v0.findViewById(R.id.right_button);

                   left.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {

                           int pos = pagerAdapter.getItemPosition(getCurrentPage());

                           if(pos > 0)
                            pager.setCurrentItem(pos - 1);
                       }
                   });

                   right.setOnClickListener(new View.OnClickListener(){

                       @Override
                       public void onClick(View v) {
                           int pos = pagerAdapter.getItemPosition(getCurrentPage());

                           if(pos < pagerAdapter.getCount() - 1);
                               pager.setCurrentItem(pos + 1);

                       }
                   });

                    ParseUser user = review.getParseUser("user");
                    ParseObject courseObject = review.getParseObject("course");

                    //if user's information is needed, we will fetch is at that time
                    try {

                        if (user != null)
                            user.fetchIfNeeded();
                        if (courseObject != null)
                            courseObject.fetchIfNeeded();

                    } catch (ParseException e2) {

                        Log.d("Baid", e2.getMessage());

                    }

                   // String name = user.getString("firstName") + " " + user
                    String className = courseObject.getString("subject") + courseObject.getString("catalogNumber");
                    String reviewText = review.getString("text");
                    String prof = review.getString("professor");
                    Number rating = review.getNumber("rating");

                    course.setText(className);
                    text.setText(reviewText);
                    professor.setText(prof);
                    workload.setText("EASY BREEZE!");
                    score.setRating(rating.floatValue());

                    addView(v0);
                    //pagerAdapter.notifyDataSetChanged();


               }

            }
        });

    }



    //-----------------------------------------------------------------------------
    // Here's what the app should do to add a view to the ViewPager.
    public void addView (View newPage)
    {
        int pageIndex = pagerAdapter.addView (newPage);
        // You might want to make "newPage" the currently displayed page:
        pagerAdapter.notifyDataSetChanged();
        //pager.setCurrentItem (pageIndex, true);

    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to remove a view from the ViewPager.
    public void removeView (View defunctPage)
    {
        int pageIndex = pagerAdapter.removeView (pager, defunctPage);
        // You might want to choose what page to display, if the current page was "defunctPage".
        if (pageIndex == pagerAdapter.getCount())
            pageIndex--;
        pager.setCurrentItem (pageIndex);
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to get the currently displayed page.
    public View getCurrentPage ()
    {
        return pagerAdapter.getView (pager.getCurrentItem());
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to set the currently displayed page.  "pageToShow" must
    // currently be in the adapter, or this will crash.
    public void setCurrentPage (View pageToShow)
    {
        pager.setCurrentItem (pagerAdapter.getItemPosition (pageToShow), true);
    }


}
