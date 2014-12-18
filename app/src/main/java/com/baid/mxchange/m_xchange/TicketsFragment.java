package com.baid.mxchange.m_xchange;

import android.app.Fragment;
import android.app.FragmentTransaction;
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
 * Created by Ish on 9/7/14.
 */
public class TicketsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Spinner sports, games;
    Button next;

    List<ParseObject> sportResults;
    List<ParseObject> gameResults;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.tickets_fragment, container, false);

        sports = (Spinner) rootView.findViewById(R.id.spinner);
        games = (Spinner) rootView.findViewById(R.id.games);
        next = (Button) rootView.findViewById(R.id.next);

        sports.setOnItemSelectedListener(this);
        games.setOnItemSelectedListener(this);
        next.setOnClickListener(this);

        loadSports();

        return rootView;
    }

    //loads all sports into spinner
    private void loadSports(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Sport");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(e != null){

                    Log.d("Baid", "Error: " + e.getMessage());
                    return;
                }
                sportResults = objects;
                Log.d("Baid", "Found "  + objects.size() + " sports");

                List<String>allSports = new ArrayList<String>();
                for(int i = 0; i < objects.size(); i ++){

                    allSports.add(objects.get(i).getString("description"));

                }

                sports.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, allSports));

            }
        });

    }

    private void loadGames(ParseObject selectedSport){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Games");
        query.whereEqualTo("sport", selectedSport);

        query.findInBackground(new FindCallback<ParseObject>(){

            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

                if(e != null){

                    Log.d("Baid", "Error: " + e.getMessage());
                }

                gameResults = parseObjects;

                List<String> allGames = new ArrayList<String>();
                for(int i = 0; i < parseObjects.size(); i ++){

                    ParseObject game = parseObjects.get(i);
                    allGames.add(game.getString("opponent"));
                }

                games.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, allGames));

            }
        });

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if(id == next.getId()){


            if(MainActivity.buy != null){

                if(MainActivity.buy == true){

                    //buy--ticket fragment
                    TicketBuyFragment newFragment = new TicketBuyFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else{


                    //sell--ticket fragment
                    TicketSellFragment newFragment = new TicketSellFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId() == sports.getId()){

            ParseObject po = sportResults.get(position);
            loadGames(po);
        }
        if(parent.getId() == games.getId()){

            MainActivity.sportsGame = gameResults.get(position);
            Log.d("Baid", "Game: " + MainActivity.sportsGame.getString("opponent"));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
