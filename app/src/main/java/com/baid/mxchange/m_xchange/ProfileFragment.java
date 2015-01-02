package com.baid.mxchange.m_xchange;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Ish on 9/14/14.
 */
public class ProfileFragment  extends Fragment implements View.OnClickListener{

    Button exchange, myTextbooks, myTickets, blueReview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
        exchange = (Button) rootView.findViewById(R.id.exchange);
        myTextbooks = (Button) rootView.findViewById(R.id.my_textbooks);
        myTickets = (Button) rootView.findViewById(R.id.my_tickets);
        blueReview = (Button) rootView.findViewById(R.id.blue_review);

        exchange.setOnClickListener(this);
        myTextbooks.setOnClickListener(this);
        myTickets.setOnClickListener(this);
        blueReview.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if(id == exchange.getId()){

            //start Fragment 1
            Fragment1 newFragment = new Fragment1();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(id == myTextbooks.getId()){

            MainActivity.book = new Boolean(true);

            UserPostsFragment newFragment = new UserPostsFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }
        else if(id == myTickets.getId()){

            MainActivity.book = new Boolean(false);

            UserPostsFragment newFragment = new UserPostsFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }
        else if(id == blueReview.getId()){


            ReviewSelectFragment newFragment = new ReviewSelectFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
