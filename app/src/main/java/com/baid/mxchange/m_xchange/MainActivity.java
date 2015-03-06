package com.baid.mxchange.m_xchange;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by Ish on 9/6/14.
 * This activity is where all fragments will lay on top of
 */
public class MainActivity extends Activity {

    public static Boolean buy;

    public static String course;
    public static ParseObject courseObject;
    public static ParseObject sportsGame;
    public static ParseObject reviewCourse;


    public static SearchTextbooks searchResults;
    public static SearchTickets ticketResults;
    public static SearchInterface searchInterface;

    private int phoneNumber = -1;

    ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //real database
        //Parse.initialize(this, "Hv2s5UNlCaykyL5JxX5EIGYaxQrXAV6Ci2W6TikL", "nmVHe8v5c9pmDz9Wvh8o7zWQNKO88WVmtyKL56Hy");


        //test database
        Parse.initialize(this, "7ybxS3opTh2dYIeo0DLDguiqpvmtlanXoSCZzIdw", "xgenFXtatwsewpQ4YQiPXxC8V3qjPb9JbrFPls9n");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        DashboardFragment newFragment = new DashboardFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.frame, newFragment);
        transaction.commit();

        //get whether or not new user
        Intent intent = getIntent();
        boolean isNewUser = intent.getBooleanExtra("newUser", false);

        //if user is new, then we need to store the user's phone number as well retrieve data from facebook
        if (isNewUser) {

            //get phone number
            phoneNumberDialog();

            // Fetch Facebook user info if the session is active
            Session session = ParseFacebookUtils.getSession();
            if (session != null && session.isOpened()) {
                makeMeRequest();
            }

        }

        searchInterface = new SearchInterface() {

            @Override
            public void onStart() {

                loadingDialog = ProgressDialog.show(MainActivity.this, "",
                        "Loading. Please wait...", true);
            }

            @Override
            public void onSearchComplete() {


                //launches results fragment
                ResultsFragment resultsFragment = new ResultsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, resultsFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                if (loadingDialog != null)
                    loadingDialog.dismiss();
            }


        };

    }


    private void phoneNumberDialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Enter Phone Number");
        alert.setMessage("Don't worry, we won't share it with anyone without your permission");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();

                try{

                    phoneNumber = Integer.parseInt(value);
                    Log.d("Baid", "Number: " + phoneNumber);

                    ParseUser currentUser = ParseUser.getCurrentUser();
                    currentUser.put("phone", phoneNumber);
                    currentUser.saveInBackground();

                }catch (NumberFormatException e){


                    Log.d("Baid", "Number format exception");
                }


            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    private void makeMeRequest() {

        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        // handle response
                        if (user != null) {

                            String firstName = user.getFirstName();
                            String lastName = user.getLastName();
                            String id = user.getId();
                            String email = user.getProperty("email").toString();

                            Log.e("facebookid", id);
                            Log.e("firstName", firstName);
                            Log.e("lastName", lastName);
                            Log.e("email", email);

                            ParseUser currentUser = ParseUser.getCurrentUser();

                            currentUser.put("email", email);
                            currentUser.put("firstName", firstName);
                            currentUser.put("lastName", lastName);


                            currentUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                    if(e != null){

                                        Log.d("Baid","Error: " + e.getMessage());
                                        return;
                                    }

                                    Log.d("Baid", "User saved with Facebook information");

                                }
                            });


                        } else if (response.getError() != null) {
                            // handle error
                            Log.d("Baid", "Error retrieving Facebook information");
                        }
                    }
                });
        request.executeAsync();

    }

    //if user has same email as existing user, link users.
    private void userAlreadyExists(){


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

        //return user to dashboard
        if (id == R.id.dashboard){

            DashboardFragment resultsFragment = new DashboardFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, resultsFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }
        //show user profile
        else if (id == R.id.profile) {

            ProfileFragment resultsFragment = new ProfileFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, resultsFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        //get information about application
        else if (id == R.id.info) {

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Information");
            alertDialog.setMessage(getString(R.string.info));
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        }
        else if (id == R.id.new_post){

            SelectCourseFragment resultsFragment = new SelectCourseFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, resultsFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }

        return super.onOptionsItemSelected(item);
    }


}
