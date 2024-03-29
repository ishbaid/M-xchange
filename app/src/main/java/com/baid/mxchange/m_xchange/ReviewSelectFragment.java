package com.baid.mxchange.m_xchange;

import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 */
public class ReviewSelectFragment extends Fragment implements AdapterView.OnItemSelectedListener  {

    //globals
    final static String TAG = "ReviewSelectFragment";

    //bottom half
    private ViewPager pager = null;
    private MainPagerAdapter pagerAdapter = null;
    final static int QUERY_LIMIT = 5;
    int numRatings = 0;
    float averageRating = 0;
    double averageWorkload = 0;

    //top halfs
    Spinner school, department, course;
    List<ParseObject> allSchools, allDepartments, allCourse;

    Button readReviews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_review_select, container, false);

        initializeTop(rootView);
        initializeBottom(rootView);


        return rootView;
    }

    private void initializeBottom(View rootView){

        pagerAdapter = new MainPagerAdapter();
        pager = (ViewPager) rootView.findViewById(R.id.view_pager);
        pager.setAdapter (pagerAdapter);

        loadReviews();
//        LayoutInflater ifl = getActivity().getLayoutInflater();
//        LinearLayout v0 = (LinearLayout) ifl.inflate(R.layout.review_layout, null);
//        pagerAdapter.addView(v0, 0);
//        LinearLayout v1 = (LinearLayout) ifl.inflate(R.layout.review_layout, null);
//        pagerAdapter.addView(v1, 1);
//        pagerAdapter.notifyDataSetChanged();
    }

    private void initializeTop(View rootView){

        school = (Spinner) rootView.findViewById(R.id.r_college);
        department = (Spinner) rootView.findViewById(R.id.r_department);
        course = (Spinner) rootView.findViewById(R.id.r_course);

        readReviews = (Button) rootView.findViewById(R.id.read_button);
        readReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MainActivity.reviewCourse != null){

                    String courseName = MainActivity.reviewCourse.getString("subject") + MainActivity.reviewCourse.getString("catalogNumber");
                    Log.d("Baid", "Reviews of " + courseName);
                }
                else{

                    Log.d("Baid", "No course has been selected");
                    return;
                }

                //remove all current reviews
                while(pagerAdapter.getCount() != 0) {
                    pagerAdapter.removeView(pager, 0);
                    pagerAdapter.notifyDataSetChanged();

                }
                findReviews();
            }
        });

        school.setOnItemSelectedListener(this);
        department.setOnItemSelectedListener(this);
        course.setOnItemSelectedListener(this);

        allSchools = new ArrayList<ParseObject>();
        allDepartments = new ArrayList<ParseObject>();
        allCourse = new ArrayList<ParseObject>();

        loadSchools();

    }

    private void loadSchools(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Schools");
        query.whereExists("code");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {

                    allSchools = objects;
                    Log.d("Baid", "Retrieved " + objects.size() + " objects");
                    List<String>items = new ArrayList<String>();

                    for(int i = 0; i < objects.size(); i ++){

                        items.add(objects.get(i).getString("description"));

                    }
                    Log.d("Baid", "Item size: " + items.size());

                    if(getActivity() == null)
                        return;
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    school.setAdapter(adapter);


                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    private void loadDepartments(String code){

        Log.d("Baid", "Loading Departments");
        String schoolSelection = (String) school.getSelectedItem();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Subjects");
        query.whereEqualTo("school", code);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){

                    allDepartments = objects;
                    Log.d("Baid", "Number of departments found: " + objects.size());
                    List<String> items = new ArrayList<String>();
                    for(int i = 0; i < objects.size(); i ++){


                        items.add(objects.get(i).getString("description"));
                    }

                    if(getActivity() == null)
                        return;
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    department.setAdapter(adapter);
                }
            }
        });



    }

    private void loadCourses(String code){

        new AsyncTask<Void, Void, Object>(){

            @Override
            protected Object doInBackground(Void... voids) {


                return null;
            }
        }.execute();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Classes");
        query.whereEqualTo("subject", code);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(e == null){

                    allCourse = objects;
                    Log.d("Baid", "Classes found: " + objects.size());
                    List<String>items = new ArrayList<String>();
                    for(int i = 0; i < objects.size(); i ++){


                        ParseObject po = objects.get(i);

                        String subject = po.getString("subject");
                        String catNum = po.getString("catalogNumber");

                        Log.d("Baid", subject + ":" + catNum);
                        if(subject != null && catNum != null)
                            items.add(subject + catNum);

                    }

                    if(getActivity() == null) {

                        return;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    course.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        Log.d("Baid", "Items selected");
        if(adapterView.getId() == school.getId()){

            Log.d("Baid", "College selected");
            if(allSchools != null){

                ParseObject college = allSchools.get(position);
                String code = college.getString("code");
                loadDepartments(code);

            }

        }
        else if(adapterView.getId() == department.getId()){

            Log.d("Baid", "Department selected");
            if(allDepartments != null){

                ParseObject dep = allDepartments.get(position);
                loadCourses(dep.getString("code"));

            }
        }
        else if(adapterView.getId() == course.getId()){


            Log.d("Baid", "Course selected");
            if(course.getAdapter().getCount() > 0) {

                MainActivity.reviewCourse = allCourse.get(position);

            }
            else if(course.getAdapter().getCount() == 0){

                MainActivity.reviewCourse = null;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //find reviews for selected class
    private void findReviews(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Review");
        query.whereEqualTo("course", MainActivity.reviewCourse);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

                if(e != null){

                    Log.d(TAG, e.getMessage());
                    return;
                }

                addReviewCards(parseObjects);

                //course overview card
                LayoutInflater ifl = getActivity().getLayoutInflater();
                LinearLayout view = (LinearLayout) ifl.inflate(R.layout.course_overview, null);

                TextView name, workload, numberOfRatings;
                RatingBar rating;

                name = (TextView) view.findViewById(R.id.review_title);
                workload = (TextView) view.findViewById(R.id.workload);
                numberOfRatings = (TextView) view.findViewById(R.id.num_ratings);

                rating = (RatingBar) view.findViewById(R.id.rating);

                name.setText(MainActivity.reviewCourse.getString("subject") + MainActivity.reviewCourse.getString("catalogNumber"));
                rating.setRating(averageRating);

                //set workload text
                String workText = "Unknown";
                if(averageWorkload < .5) {
                    workText = "Easy Breezy";
                    workload.setTextColor(Color.GREEN);
                }
                else if (averageWorkload < 1.5) {
                    workText = "Moderate";
                    workload.setTextColor(Color.YELLOW);
                }
                else if (averageWorkload < 2.5) {
                    workText = "Heavy";
                    workload.setTextColor(Color.MAGENTA);
                }
                else if (averageWorkload <= 3) {
                    workText = "INSANE!";
                    workload.setTextColor(Color.RED);
                }



                workload.setText(workText);
                numberOfRatings.setText(numRatings + "");

                pagerAdapter.addView(view, 0);
                pagerAdapter.notifyDataSetChanged();
                pager.setCurrentItem(0);
            }
        });
    }

    //finds recent reviews
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

                addReviewCards(parseObjects);



            }
        });

    }

    private void addReviewCards(List<ParseObject> parseObjects){

        float total = 0;
        int workTotal = 0;
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
            Number workRating = review.getNumber("workload");

            if(rating != null)
                total += rating.floatValue();
            if(workRating != null)
                workTotal += workRating.intValue();

            course.setText(className);
            text.setText(reviewText);
            professor.setText(prof);

            String workText = "Unknown";
            if(workRating != null) {
                //set workload
                switch (workRating.intValue()) {

                    case 0:
                        workText = "Easy Breezy!";
                        workload.setTextColor(Color.GREEN);
                        break;
                    case 1:
                        workText = "Moderate";
                        workload.setTextColor(Color.YELLOW);
                        break;
                    case 2:
                        workText = "Heavy";
                        workload.setTextColor(Color.MAGENTA);
                        break;
                    case 3:
                        workText = "INSANE!";
                        workload.setTextColor(Color.RED);
                        break;
                }
            }

            workload.setText(workText);
            score.setRating(rating.floatValue());
            score.setIsIndicator(true);

            addView(v0);
            //pagerAdapter.notifyDataSetChanged();


        }

        numRatings = parseObjects.size();
        averageRating = total / numRatings;
        averageWorkload = (double) workTotal / numRatings;

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
