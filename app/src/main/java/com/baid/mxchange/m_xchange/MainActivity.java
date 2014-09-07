package com.baid.mxchange.m_xchange;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.parse.Parse;

/**
 * Created by Ish on 9/6/14.
 */
public class MainActivity extends Activity {

    public static Boolean buy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Parse.initialize(this, "AQ2Vfb0vhbBq3N6t2Aeu4fpLaZ5Xp8HI42P1fOxr", "mkjVzwYH47zFQD6xOMNvwMmRHNxg0QAnDnS7AHUI");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //for now we will load fragment 1, but I would like some sort of dashboard here later

        Fragment1 newFragment = new Fragment1();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.frame, newFragment);
        transaction.commit();



    }
}
