package com.baid.mxchange.m_xchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;


public class MyActivity extends ActionBarActivity implements  View.OnClickListener{

    RelativeLayout background;
    EditText email, password;
    Button login;
    TextView signup;

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if(id == signup.getId()){

            Intent intent = new Intent(MyActivity.this, Signup.class);
            startActivity(intent);
        }
        else if (id == login.getId()){

            String empty = "";
            String un = email.getText().toString();
            String pw = password.getText().toString();
            ParseUser.logInInBackground(un, pw, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if(parseUser != null && e == null){

                        Log.d("Baid", "Logged in!");
                        Intent intent = new Intent(MyActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else if(e != null){

                        Log.d("Baid", "Error: " + e.getMessage());
                    }
                }
            });



        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Parse.initialize(this, "AQ2Vfb0vhbBq3N6t2Aeu4fpLaZ5Xp8HI42P1fOxr", "mkjVzwYH47zFQD6xOMNvwMmRHNxg0QAnDnS7AHUI");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        email = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(this);
        signup = (TextView) findViewById(R.id.signup);
        signup.setOnClickListener(this);


        background = (RelativeLayout) findViewById(R.id.background);


    }




    @Override
    protected void onResume() {
        super.onResume();

        if(ParseUser.getCurrentUser() != null){

            Intent intent = new Intent(MyActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
