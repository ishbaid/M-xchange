package com.baid.mxchange.m_xchange;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Ish on 9/6/14.
 */
public class Fragment2 extends Fragment implements View.OnClickListener{

    Button textbook, ticket;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_item, container, false);

        textbook = (Button) rootView.findViewById(R.id.textbook);
        ticket = (Button) rootView.findViewById(R.id.sportsTicket);

        textbook.setOnClickListener(this);
        ticket.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if(id == textbook.getId()){

            Log.d("Baid", "Textbook");
            MainActivity.book = new Boolean(true);

            //load select course fragment
            //start fragment2 regardless of what button was pressed
            SelectCourseFragment newFragment = new SelectCourseFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, newFragment);
            transaction.addToBackStack(null);
// Commit the transaction
            transaction.commit();

        }
        else if (id == ticket.getId()){

            Log.d("Baid", "Ticket");
            MainActivity.book = new Boolean(false);

            TicketsFragment newFragment = new TicketsFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, newFragment);
            transaction.addToBackStack(null);
// Commit the transaction
            transaction.commit();
        }



       /* if(MainActivity.buy != null){

            //start buy
            if(MainActivity.buy == true){

                //start fragment2 regardless of what button was pressed
                Fragment3 newFragment = new Fragment3();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, newFragment);
                transaction.addToBackStack(null);
// Commit the transaction
                transaction.commit();
            }
            //start sell
            else{

            }

        }
        else{

            Log.d("Baid", "Error. Buy not initialized");
        }*/
    }
}
