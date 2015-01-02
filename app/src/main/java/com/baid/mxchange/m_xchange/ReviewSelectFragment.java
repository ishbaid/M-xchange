package com.baid.mxchange.m_xchange;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


/**
 */
public class ReviewSelectFragment extends Fragment implements AdapterView.OnItemSelectedListener  {


    Spinner school, department, course;
    List<ParseObject> allSchools, allDepartments, allCourse;

    Button readReviews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_review_select, container, false);



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

                ReadReviewFragment resultsFragment = new ReadReviewFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, resultsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        school.setOnItemSelectedListener(this);
        department.setOnItemSelectedListener(this);
        course.setOnItemSelectedListener(this);

        allSchools = new ArrayList<ParseObject>();
        allDepartments = new ArrayList<ParseObject>();
        allCourse = new ArrayList<ParseObject>();

        loadSchools();

        return rootView;
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

                    if(items == null)
                        Log.d("Baid", "Null items");
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
}
