package com.baid.mxchange.m_xchange;


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
 * A simple {@link Fragment} subclass.
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
        //authButton.setReadPermissions(Arrays.asList("public_profile"));


        return rootView;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if(id == signup.getId()){

            Intent intent = new Intent(getActivity(), Signup.class);
            startActivity(intent);
        }
        else if (id == login.getId()){

            String un = email.getText().toString() + UM_DOMAIN;
            String pw = password.getText().toString();
            ParseUser.logInInBackground(un, pw, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (parseUser != null && e == null) {

                        Log.d("Baid", "Logged in!");
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    } else if (e != null) {

                        clearText();
                        email.setHint("Incorrect Email/Password");
                        email.setHintTextColor(Color.RED);
                        Log.d("Baid", "Error: " + e.getMessage());
                    }
                }
            });



        }
    }

    //do
    private void onLoginButtonClicked(){

        Log.d("Baid", "onLoginButtonClicked");

        new AsyncTask<Void, Void, Object>(){


            @Override
            protected Object doInBackground(Void... params) {

                Log.d("Baid", "doInBackground");
                //ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
                ParseFacebookUtils.logIn(Arrays.asList("email", ParseFacebookUtils.Permissions.Friends.ABOUT_ME),
                        getActivity(), new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException err) {

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


                                startActivity(intent);
                            }
                        });


                return null;
            }
        }.execute();



    }

    private void clearText(){

        email.setText("");
        password.setText("");
    }




//    private UiLifecycleHelper uiHelper;
//    private Session.StatusCallback callback = new Session.StatusCallback() {
//        @Override
//        public void call(Session session, SessionState state, Exception exception) {
//            onSessionStateChange(session, state, exception);
//
//        }
//    };
//
//    private void onSessionStateChange(com.facebook.Session session, SessionState state, Exception exception) {
//
//        //user is logged in
//        if (state.isOpened()) {
//
//
//            Log.i(TAG, "Logged in...");
//            // Request user data and show the results
//            Request.newMeRequest(session, new Request.GraphUserCallback() {
//
//                // callback after Graph API response with user object
//                @Override
//                public void onCompleted(GraphUser user, Response response) {
//                    if (user != null) {
//
//                        String firstName = user.getFirstName();
//                        String lastName = user.getLastName();
//                        String id = user.getId();
//                        String email = user.getProperty("email").toString();
//
//                        Log.e("facebookid", id);
//                        Log.e("firstName", firstName);
//                        Log.e("lastName", lastName);
//                        Log.e("email", email);
//                    }
//                }
//            }).executeAsync();
//
//        } else if (state.isClosed()) {
//            Log.i(TAG, "Logged out...");
//        }
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        uiHelper = new UiLifecycleHelper(getActivity(), callback);
//        uiHelper.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        // For scenarios where the main activity is launched and user
//        // session is not null, the session state change notification
//        // may not be triggered. Trigger it if it's open/closed.
//        Session session = Session.getActiveSession();
//        if (session != null &&
//                (session.isOpened() || session.isClosed()) ) {
//            onSessionStateChange(session, session.getState(), null);
//        }
//
//        uiHelper.onResume();
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        uiHelper.onActivityResult(requestCode, resultCode, data);
//
//    }
//
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        uiHelper.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        uiHelper.onDestroy();
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        uiHelper.onSaveInstanceState(outState);
//    }
}
