package com.baid.mxchange.m_xchange;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Ish on 9/7/14.
 */
public class ViewProfileActivity extends Activity {

    TextView name, email, number, pay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);
        Intent intent = getIntent();
        String nVal = intent.getStringExtra("name");
        String eVal = intent.getStringExtra("email");
        String nuVal = intent.getStringExtra("number");
        double payment = intent.getDoubleExtra("pay", -1);

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        number = (TextView) findViewById(R.id.number);
        pay = (TextView) findViewById(R.id.pay);

        if(nVal != null)
            name.setText(nVal);
        if(eVal != null)
            email.setText(eVal);
        if(nuVal != null)
            number.setText(nuVal);
        if(payment != -1)
            pay.setText("$" + payment);



    }
}
