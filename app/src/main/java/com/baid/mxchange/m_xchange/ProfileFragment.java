package com.baid.mxchange.m_xchange;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

/**
 * Created by Ish on 9/14/14.
 */
public class ProfileFragment  extends Fragment implements View.OnClickListener{

    Button exchange, myTextbooks, blueReview, logout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
        exchange = (Button) rootView.findViewById(R.id.exchange);
        myTextbooks = (Button) rootView.findViewById(R.id.my_textbooks);
        blueReview = (Button) rootView.findViewById(R.id.blue_review);
        logout = (Button) rootView.findViewById(R.id.logout_button);

        exchange.setOnClickListener(this);
        myTextbooks.setOnClickListener(this);
        blueReview.setOnClickListener(this);
        logout.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if(id == exchange.getId()){

            //start Fragment 1
            SelectCourseFragment newFragment = new SelectCourseFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(id == myTextbooks.getId()){



            UserPostsFragment newFragment = new UserPostsFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }

        else if(id == blueReview.getId()){


            ReviewViewerFragment newFragment = new ReviewViewerFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(id == logout.getId()){


            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            //logout user
                            logout();

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        }

    }

    //logs user out
    private void logout(){

        //logout facebook user
        if(ParseFacebookUtils.getSession() != null && ParseFacebookUtils.getSession().isOpened())
            ParseFacebookUtils.getSession().closeAndClearTokenInformation();

        //logout parse user
        ParseUser.logOut();

        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
    }
}
