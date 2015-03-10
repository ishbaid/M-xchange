package com.baid.mxchange.m_xchange;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class WriteReviewFragment extends Fragment implements View.OnClickListener{


    TextView courseName;
    RatingBar courseRating;
    EditText courseReview, profName;
    Spinner workloadSpinner;
    Switch anon;
    Button submit;



    public WriteReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_write_review, container, false);

        courseName = (TextView) rootView.findViewById(R.id.review_write_title);
        courseRating = (RatingBar) rootView.findViewById(R.id.write_rating);
        workloadSpinner = (Spinner) rootView.findViewById(R.id.workload_spinner);
        profName = (EditText) rootView.findViewById(R.id.professor_edit);
        courseReview = (EditText) rootView.findViewById(R.id.write_review);
        anon = (Switch) rootView.findViewById(R.id.anon);
        submit = (Button) rootView.findViewById(R.id.write_submit);

        submit.setOnClickListener(this);

        String[] values = {"Easy Breezy", "Moderate", "Heavy", "Insane"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workloadSpinner.setAdapter(adapter);

        String title = MainActivity.reviewCourse.getString("subject") + MainActivity.reviewCourse.getString("catalogNumber");
        courseName.setText(title);

        return rootView;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if(id == submit.getId()){

            double rating = courseRating.getRating();
            String professor = profName.getText().toString();
            int workloadValue = workloadSpinner.getSelectedItemPosition();
            ParseObject review = new ParseObject("Review");
            review.put("user", ParseUser.getCurrentUser());
            review.put("course", MainActivity.reviewCourse);


            review.put("anonymous", anon.isChecked());

            //submit non empty review
            String text = courseReview.getText().toString();
            if(!text.equals("")){

                review.put("text", text);
            }
            review.put("rating", rating);
            review.put("professor", professor);
            review.put("workload", workloadValue);

            review.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    if(e != null){

                        Log.d("Baid", "Error: " + e.getMessage());
                        return;
                    }
                    Log.d("Baid", "Saved Review");
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Review Submitted");
                    alertDialog.setMessage("Thanks for submitting the review! Your feedback is going to help Michigan students better select courses");
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();

                    //hide keyboard
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    //return user to dashboard
                    ProfileFragment resultsFragment = new ProfileFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, resultsFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });

        }
    }
}
