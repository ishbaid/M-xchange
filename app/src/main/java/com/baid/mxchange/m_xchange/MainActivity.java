package com.baid.mxchange.m_xchange;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Ish on 9/6/14.
 * This activity is where all fragments will lay on top of
 */
public class MainActivity extends Activity {

    public static Boolean buy;
    public static Boolean book;
    public static String course;
    public static ParseObject sportsGame;


    public static SearchTextbooks searchResults;
    public static SearchTickets ticketResults;
    public static SearchInterface searchInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Parse.initialize(this, "7ybxS3opTh2dYIeo0DLDguiqpvmtlanXoSCZzIdw", "xgenFXtatwsewpQ4YQiPXxC8V3qjPb9JbrFPls9n");super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //for now we will load fragment 1, but I would like some sort of dashboard here later

        ProfileFragment newFragment = new ProfileFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.frame, newFragment);
        transaction.commit();

        searchInterface = new SearchInterface() {
            @Override
            public void onSearchComplete() {

                //launches results fragment
                ResultsFragment resultsFragment = new ResultsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, resultsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }


        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.logout) {

            ParseUser.logOut();
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
