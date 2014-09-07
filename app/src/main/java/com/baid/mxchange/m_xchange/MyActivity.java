package com.baid.mxchange.m_xchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


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

            Intent intent = new Intent(MyActivity.this, MainActivity.class);
            startActivity(intent);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
