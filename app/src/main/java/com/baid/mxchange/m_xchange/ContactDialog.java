package com.baid.mxchange.m_xchange;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by Ish on 12/17/14.
 */
public class ContactDialog extends Dialog implements View.OnClickListener {

    //fields
    TextView seller, title, version, course, price, condition, description;

    //actions
    TextView call, message, email;

    String phoneNumber = null;
    String emailValue = null;

    Context context;
    public ContactDialog(Context c){

        super(c);
        context = c;

    }

    //for textbooks
    public void setData(String conditionString, String descriptionString, String edition, String courseVal, double priceVal, boolean showNumber, String titleString, String name, Number number, String emailString){

        reformatText();

        seller.setText(seller.getText() + name);
        title.setText(title.getText() + titleString);
        version.setText(version.getText() + edition);
        course.setText(course.getText() + courseVal);
        String priceString = priceVal + "";
        price.setText(price.getText() + priceString);
        condition.setText(condition.getText() + conditionString);
        description.setText(description.getText() + descriptionString);

        phoneNumber = number + "";
        Log.d("Baid", "Number is " + phoneNumber);

        emailValue = emailString;
        Log.d("Baid", emailValue);

    }

    //for tickets
    public void setData(double priceVal, int row, int section, boolean showNumber, String name, Number number, String emailString){

        reformatText();

        title.setText(title.getText() + MainActivity.sportsGame.getString("opponent"));

        String rowString = row + "";
        if(row != -1)
            version.setText(version.getText() + rowString);;

        String secString = section + "";
        if(section != -1)
            course.setText(course.getText() + secString);

        String priceString = priceVal + "";
        price.setText(price.getText() + priceString);



        phoneNumber = number + "";
        Log.d("Baid", "Number is " + phoneNumber);

        emailValue = emailString;
        Log.d("Baid", emailValue);
    }

    private void reformatText(){

        if(!MainActivity.buy){

            seller.setText("Buyer: ");
        }

        //don't need to do anything
        if(MainActivity.book){


        }
        else{

            title.setText("Game: ");
            version.setText("Row: ");
            course.setText("Section: ");
            condition.setText("");
            description.setText("");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_dialog);

        seller = (TextView) findViewById(R.id.seller);
        title = (TextView) findViewById(R.id.title);
        version = (TextView) findViewById(R.id.version);
        course = (TextView) findViewById(R.id.course);
        price = (TextView) findViewById(R.id.price);
        condition = (TextView) findViewById(R.id.condition);
        description = (TextView) findViewById(R.id.description);

        call = (TextView) findViewById(R.id.call);
        message = (TextView) findViewById(R.id.message);
        email = (TextView) findViewById(R.id.email);

        call.setOnClickListener(this);
        message.setOnClickListener(this);
        email.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if(id == call.getId()){

            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            context.startActivity(intent);
        }
        else if(id == message.getId()){

            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setType("vnd.android-dir/mms-sms");
            sendIntent.setData(Uri.parse("sms:" + phoneNumber));
            sendIntent.putExtra("sms_body", "Hey, I just saw your post on Blue Exchange...");
            context.startActivity(sendIntent);
            Log.d("Baid", "Phone is " + phoneNumber);
        }
        else if(id == email.getId()){

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Blue Exchange");
            intent.putExtra(Intent.EXTRA_TEXT, "Hey, I just saw your post on Blue Exchange...");
            Intent mailer = Intent.createChooser(intent, null);
            context.startActivity(mailer);

        }
    }
}
