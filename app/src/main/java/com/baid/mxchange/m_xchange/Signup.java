package com.baid.mxchange.m_xchange;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

/**
 * Created by Ish on 9/6/14.
 */
public class Signup extends Activity {

    EditText first, last, email, phone, password, password2;
    Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Parse.initialize(this, "AQ2Vfb0vhbBq3N6t2Aeu4fpLaZ5Xp8HI42P1fOxr", "mkjVzwYH47zFQD6xOMNvwMmRHNxg0QAnDnS7AHUI");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        first = (EditText) findViewById(R.id.first);
        last =  (EditText) findViewById(R.id.last);
        email = (EditText) findViewById(R.id.username);
        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        password2 = (EditText) findViewById(R.id.password2);

        confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String empty = "";

                //ensures that all fields are filled in and both passwords are the same
                if(!first.getText().toString().equals(empty) && !last.getText().toString().equals(empty) && !email.getText().toString().equals(empty) && !phone.getText().toString().equals(empty) && password2.getText().toString().equals(password.getText().toString())){

                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("username", email.getText().toString());
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {

                            if(e == null){

                                if(objects.size() == 0){

                                    Log.d("Baid", "User doesn't exist");
                                    //create user object
                                    ParseUser user = new ParseUser();
                                    user.setUsername(email.getText().toString());
                                    user.setPassword(password.getText().toString());
                                    user.setEmail(email.getText().toString());
                                    user.put("phoneNumber", phone.getText().toString());
                                    user.put("firstName", first.getText().toString());
                                    user.put("lastName", last.getText().toString());

                                    user.signUpInBackground(new SignUpCallback() {
                                        @Override
                                        public void done(ParseException e) {

                                            if(e == null){

                                                Log.d("Baid", "Account created!");
                                                Toast.makeText(Signup.this, "Account Created!", Toast.LENGTH_SHORT).show();
                                                //launch main activity
                                                Intent intent = new Intent(Signup.this, MainActivity.class);
                                                startActivity(intent);
                                            }
                                            else{

                                                Log.d("Baid", "Error signing up!" + e.getMessage());
                                            }
                                        }
                                    });


                                }
                                else if(objects.size() == 1){

                                    Log.d("Baid", "User already exits");

                                    AlertDialog alertDialog = new AlertDialog.Builder(Signup.this).create();
                                    alertDialog.setTitle("Alert ");
                                    alertDialog.setMessage("Account already exists for email address");
                                    alertDialog.setCanceledOnTouchOutside(true);
                                    alertDialog.show();

                                }
                                else if(objects.size() > 1){


                                    Log.d("Baid", "Error. Duplicate accounts may exist");
                                }
                            }
                        }
                    });


                }
                else{

                    Log.d("Baid", "Error. Invalid input");
                    AlertDialog alertDialog = new AlertDialog.Builder(Signup.this).create();
                    alertDialog.setTitle("Alert ");
                    alertDialog.setMessage("Enter all fields and ensure both passwords are the same");
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();

                }
            }
        });



    }
}
