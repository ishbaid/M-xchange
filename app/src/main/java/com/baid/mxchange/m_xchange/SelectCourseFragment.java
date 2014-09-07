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
    Button next;
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

                Log.d("Baid", "Course selected: " + MainActivity.course);

                if(MainActivity.buy != null){

                    //start buy
                    if(MainActivity.buy == true){


                        Fragment3 newFragment = new Fragment3();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame, newFragment);
                        transaction.addToBackStack(null);
                        // Commit the transaction
                        transaction.commit();
                    }
                    //start sell
                    else{

                        Fragment4 newFragment = new Fragment4();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame, newFragment);
                        transaction.addToBackStack(null);
                        // Commit the transaction
                        transaction.commit();
                    }

                    }
                else{

                    Log.d("Baid", "Error. Buy not initialized");
                }

                }

        });

        allSchools = new ArrayList<ParseObject>();
        allDepartments = new ArrayList<ParseObject>();
        allCourse = new ArrayList<ParseObject>();


        ParseQuery<ParseObject> query = ParseQuery.getQuery("School");
        query.whereExists("SchoolCode");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {

                    allSchools = objects;
                    Log.d("Baid", "Retrieved " + objects.size() + " objects");
                    List<String>items = new ArrayList<String>();

                    for(int i = 0; i < objects.size(); i ++){

                        items.add(objects.get(i).getString("SchoolDescr"));

                    }
                    Log.d("Baid", "Item size: " + items.size());

                    school.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items));


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
        query.whereEqualTo("SchoolCode", code);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){

                    allDepartments = objects;
                    Log.d("Baid", "Number of departments found: " + objects.size());
                    List<String> items = new ArrayList<String>();
                    for(int i = 0; i < objects.size(); i ++){


                        items.add(objects.get(i).getString("SubjectDescr"));
                    }
                    department.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items));
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
        query.whereEqualTo("SubjectCode", code);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(e == null){

                    Log.d("Baid", "Classes found: " + objects.size());
                    List<String>items = new ArrayList<String>();
                    for(int i = 0; i < objects.size(); i ++){


                        ParseObject po = objects.get(i);



                        Log.d("Baid", po.getString("SubjectCode") + ":" + po.getInt("CatalogNbr"));
                        if(po.getString("SubjectCode") != null && po.getInt("CatalogNbr") > 0)
                            items.add(po.getString("SubjectCode") + po.getInt("CatalogNbr"));

                    }

                    course.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items));
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
                String code = college.getString("SchoolCode");

                loadDepartments(code);

            }

        }
        else if(adapterView.getId() == department.getId()){

            Log.d("Baid", "Department selected");
            if(allDepartments != null){

                ParseObject dep = allDepartments.get(position);
                loadCourses(dep.getString("SubjectCode"));

            }
        }
        else if(adapterView.getId() == course.getId()){


            Log.d("Baid", "Course selected");
            if(course.getAdapter().getCount() > 0) {

                String cc = (String) course.getSelectedItem();
                MainActivity.course = cc;

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
