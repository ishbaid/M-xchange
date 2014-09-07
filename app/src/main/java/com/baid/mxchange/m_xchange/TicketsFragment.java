package com.baid.mxchange.m_xchange;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by Ish on 9/7/14.
 */
public class TicketsFragment extends Fragment {

    Spinner games;
    Button next;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.tickets_fragment, container, false);

        games = (Spinner) rootView.findViewById(R.id.spinner);
        next = (Button) rootView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                String tic = (String) games.getSelectedItem();
                MainActivity.sportsGame = tic;

                if(MainActivity.buy != null){

                    if(MainActivity.buy == true){

                        //buy--ticket fragment
                        TicketBuyFragment newFragment = new TicketBuyFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame, newFragment);
                        transaction.addToBackStack(null);
// Commit the transaction
                        transaction.commit();
                    }else{


                        //sell--ticket fragment
                        TicketSellFragment newFragment = new TicketSellFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame, newFragment);
                        transaction.addToBackStack(null);
// Commit the transaction
                        transaction.commit();
                    }
                }

            }
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("FootballGames");
        query.whereExists("game");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){

                    Log.d("Baid", "Found "  + objects.size() + " games");
                    List<String>allGames = new ArrayList<String>();
                    for(int i = 0; i < objects.size(); i ++){

                        allGames.add(objects.get(i).getString("game"));

                    }

                    games.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, allGames));
                }
            }
        });

        return rootView;
    }
}
