package com.baid.mxchange.m_xchange;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * Created by Ish on 9/6/14.
 */
public class SelectCourseFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Spinner school, department, course;
    List<ParseObject> allSchools, allDepartments, allCourse;
    Button next, writeReview, browse;

    final static String TAG = "SelectCourseFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.select_course, container, false);

        school = (Spinner) rootView.findViewById(R.id.school);
        school.setOnItemSelectedListener(this);

        department = (Spinner) rootView.findViewById(R.id.department);
        department.setOnItemSelectedListener(this);

        course = (Spinner) rootView.findViewById(R.id.course);
        course.setOnItemSelectedListener(this);

        next = (Button) rootView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

            Log.d(TAG, "Course selected: " + MainActivity.course);
            NewPostFragment resultsFragment = new NewPostFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, resultsFragment);
            transaction.addToBackStack(null);
            transaction.commit();


                }

        });

        browse = (Button) rootView.findViewById(R.id.browse_button);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = v.getId();
                //just show textbooks
                if(MainActivity.course != null && id == browse.getId()){

                    Log.d("Baid", "Browse pressed");
                    MainActivity.searchResults = new SearchTextbooks(MainActivity.searchInterface);

                }
            }
        });

        writeReview = (Button) rootView.findViewById(R.id.write_review);
        writeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.reviewCourse = MainActivity.courseObject;
                if(MainActivity.reviewCourse == null)
                    return;

                WriteReviewFragment resultsFragment = new WriteReviewFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, resultsFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        allSchools = new ArrayList<ParseObject>();
        allDepartments = new ArrayList<ParseObject>();
        allCourse = new ArrayList<ParseObject>();


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

                    if(getActivity() == null){

                        return;

                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    school.setAdapter(adapter);


                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
        return rootView;

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

        //college selected
        if(adapterView.getId() == school.getId()){

            Log.d("Baid", "College selected");
            if(allSchools != null){

                ParseObject college = allSchools.get(position);
                String code = college.getString("code");
                loadDepartments(code);

            }

        }
        //department selected
        else if(adapterView.getId() == department.getId()){

            Log.d("Baid", "Department selected");
            if(allDepartments != null){

                ParseObject dep = allDepartments.get(position);
                loadCourses(dep.getString("code"));

            }
        }
        //class selected
        else if(adapterView.getId() == course.getId()){


            Log.d("Baid", "Course selected");
            if(course.getAdapter().getCount() > 0) {

                String cc = (String) course.getSelectedItem();
                MainActivity.course = cc;
                MainActivity.courseObject = allCourse.get(position);


            }
            else if(course.getAdapter().getCount() == 0){

                String dep = (String) department.getSelectedItem();
                MainActivity.course = dep;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
