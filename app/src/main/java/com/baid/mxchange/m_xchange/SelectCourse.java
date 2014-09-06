package com.baid.mxchange.m_xchange;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ish on 9/6/14.
 */
public class SelectCourse extends Activity implements AdapterView.OnItemSelectedListener{

    Spinner school, department, course;
    List<ParseObject> allSchools, allDepartments, allCourse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //initialize parse
        Parse.initialize(this, "AQ2Vfb0vhbBq3N6t2Aeu4fpLaZ5Xp8HI42P1fOxr", "mkjVzwYH47zFQD6xOMNvwMmRHNxg0QAnDnS7AHUI");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_course);

        school = (Spinner) findViewById(R.id.school);
        school.setOnItemSelectedListener(this);
        department = (Spinner) findViewById(R.id.department);
        department.setOnItemSelectedListener(this);
        course = (Spinner) findViewById(R.id.course);
        course.setOnItemSelectedListener(this);



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

                    school.setAdapter(new ArrayAdapter<String>(SelectCourse.this, android.R.layout.simple_spinner_item, items));


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
                    department.setAdapter(new ArrayAdapter<String>(SelectCourse.this, android.R.layout.simple_spinner_item, items));
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

                    course.setAdapter(new ArrayAdapter<String>(SelectCourse.this, android.R.layout.simple_spinner_item, items));
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
        else if(id == course.getId()){


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
