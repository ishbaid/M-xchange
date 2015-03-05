package com.baid.mxchange.m_xchange;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;


/**
 * Login screen
 *
 * User can either log in via a Blue Exchange account or Facebook
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    LinearLayout background;
    EditText email, password;
    Button login;
    TextView signup;

    final static String UM_DOMAIN = "@umich.edu";
    private static final String TAG = "LoginFragment";

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.login_activity, container, false);

        email = (EditText) rootView.findViewById(R.id.username);
        password = (EditText) rootView.findViewById(R.id.password);

        login = (Button) rootView.findViewById(R.id.login);
        login.setOnClickListener(this);
        signup = (TextView) rootView.findViewById(R.id.row_label);
        signup.setOnClickListener(this);


        background = (LinearLayout) rootView.findViewById(R.id.background);

        LoginButton authButton = (LoginButton) rootView.findViewById(R.id.authButton);
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Baid", "Click");
                onLoginButtonClicked();
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        //Launch sign up activity
        if (id == signup.getId()) {

            Intent intent = new Intent(getActivity(), Signup.class);
            startActivity(intent);
        }

        //attempt to login user
        else if (id == login.getId()) {

            String un = email.getText().toString() + UM_DOMAIN;
            String pw = password.getText().toString();
            ParseUser.logInInBackground(un, pw, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {

                    //successfull login
                    if (parseUser != null && e == null) {

                        Log.d("Baid", "Logged in!");
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }

                    //ERROR
                    else if (e != null) {

                        clearText();
                        email.setHint("ERROR");
                        email.setHintTextColor(Color.RED);

                        //tell user what is wrong
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Error");
                        alertDialog.setMessage(e.getMessage());
                        alertDialog.setCanceledOnTouchOutside(true);
                        alertDialog.show();


                        Log.d("Baid", "Error: " + e.getMessage());
                    }
                }
            });


        }
    }

    //Facebook login
    private void onLoginButtonClicked() {

        Log.d("Baid", "onLoginButtonClicked");

        //login user on another thread
        new AsyncTask<Void, Void, Object>() {


            @Override
            protected Object doInBackground(Void... params) {

                Log.d("Baid", "doInBackground");

                //attempt to login user
                ParseFacebookUtils.logIn(Arrays.asList("email", ParseFacebookUtils.Permissions.Friends.ABOUT_ME),
                        getActivity(), new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException err) {


                                if(err != null){

                                    //tell user what is wrong
                                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                    alertDialog.setTitle("Error");
                                    alertDialog.setMessage(err.getMessage());
                                    alertDialog.setCanceledOnTouchOutside(true);
                                    alertDialog.show();
                                    return;
                                }

                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                Log.d("Baid", "Log in is complete");
                                if (user == null) {
                                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                                    return;
                                } else if (user.isNew()) {

                                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                                    intent.putExtra("newUser", true);

                                } else {

                                    Log.d("MyApp", "User logged in through Facebook!");
                                    intent.putExtra("newUser", false);
                                }

                                //launches dashboard
                                startActivity(intent);
                            }
                        });


                return null;
            }
        }.execute();


    }

    private void clearText() {

        email.setText("");
        password.setText("");
    }
}