package com.baid.mxchange.m_xchange;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

/*
* This activity is where user signs into their account. If user is already signed in,
* they will be redirected to dashboard
*
* */
public class Login extends ActionBarActivity implements  View.OnClickListener{

    LinearLayout background;
    EditText email, password;
    Button login;
    TextView signup;

    final static String UM_DOMAIN = "@umich.edu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        //TEST database
        Parse.initialize(this, "7ybxS3opTh2dYIeo0DLDguiqpvmtlanXoSCZzIdw", "xgenFXtatwsewpQ4YQiPXxC8V3qjPb9JbrFPls9n");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        email = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(this);
        signup = (TextView) findViewById(R.id.signup);
        signup.setOnClickListener(this);


        background = (LinearLayout) findViewById(R.id.background);


    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if(id == signup.getId()){

            Intent intent = new Intent(Login.this, Signup.class);
            startActivity(intent);
        }
        else if (id == login.getId()){

           String empty = "";


            String un = email.getText().toString() + UM_DOMAIN;
            String pw = password.getText().toString();
            ParseUser.logInInBackground(un, pw, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if(parseUser != null && e == null){

                        Log.d("Baid", "Logged in!");
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else if(e != null){

                        clearText();
                        email.setHint("Incorrect Email/Password");
                        email.setHintTextColor(Color.RED);
                        Log.d("Baid", "Error: " + e.getMessage());
                    }
                }
            });



        }
    }

    private void clearText(){

        email.setText("");
        password.setText("");
    }




    @Override
    protected void onResume() {
        super.onResume();

        if(ParseUser.getCurrentUser() != null){

            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
