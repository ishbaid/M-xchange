package com.baid.mxchange.m_xchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.facebook.AppEventsLogger;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

/*
* This activity is where user signs into their account. If user is already signed in,
* they will be redirected to dashboard
*
* */
public class Login extends FragmentActivity{

    private LoginFragment mainFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //eventually uncomment this -- this for some reason was causing a crash in the app
       //Parse.enableLocalDatastore(this);

        //real database
        //Parse.initialize(this, "Hv2s5UNlCaykyL5JxX5EIGYaxQrXAV6Ci2W6TikL", "nmVHe8v5c9pmDz9Wvh8o7zWQNKO88WVmtyKL56Hy");

        //TEST database
        Parse.initialize(this, "7ybxS3opTh2dYIeo0DLDguiqpvmtlanXoSCZzIdw", "xgenFXtatwsewpQ4YQiPXxC8V3qjPb9JbrFPls9n");
        super.onCreate(savedInstanceState);

        //launches login fragment
        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            mainFragment = new LoginFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, mainFragment)
                    .commit();
        } else {
            // Or set the fragment from restored state info
            mainFragment = (LoginFragment) getFragmentManager()
                    .findFragmentById(android.R.id.content);
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        //facebook metrics
        AppEventsLogger.activateApp(this, getString(R.string.facebook_app_id));

        //if user is already logged in, go to dashboard
        if(ParseUser.getCurrentUser() != null){

            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //facebook metrics
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
//        LoginFragment fragment = ((LoginFragment) getFragmentManager().findFragmentById(android.R.id.content));
//        fragment.onActivityResult(requestCode, resultCode,data);
    }


}
