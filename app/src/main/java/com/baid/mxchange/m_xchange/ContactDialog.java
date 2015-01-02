package com.baid.mxchange.m_xchange;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
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
    boolean showPhoneNumber = false;
    boolean validNumber;

    Context context;
    public ContactDialog(Context c){

        super(c);
        context = c;


        validNumber = true;
        phoneNumber = "";

    }

    //for textbooks
    public void setData(String conditionString, String descriptionString, String edition, String courseVal, double priceVal, boolean showNumber, String titleString, String name, Number number, String emailString){

        reformatText();

        if(name != null)
            seller.setText(seller.getText() + " " +  name);
        if(titleString != null)
            title.setText(title.getText() + " " +  titleString);

        if(edition != null)
            version.setText(version.getText() + " " +  edition);

        if(courseVal != null)
            course.setText(course.getText() + " " + courseVal);


        String priceString = priceVal + "";


        if(priceString != null)
            price.setText(price.getText() + " " +  priceString);

        if(conditionString != null)
            condition.setText(condition.getText() + " " +  conditionString);

        if(descriptionString != null)
            description.setText(description.getText() + " " +  descriptionString);


        formatPhoneNumber(number);

        emailValue = emailString;

        showPhoneNumber = showNumber;

    }

    //for tickets
    public void setData(double priceVal, int row, int section, boolean showNumber, String name, Number number, String emailString){

        reformatText();

        seller.setText(seller.getText() + name);
        title.setText(title.getText() + MainActivity.sportsGame.getString("opponent"));

        String rowString = row + "";
        if(row != -1)
            version.setText(version.getText() + rowString);;

        String secString = section + "";
        if(section != -1)
            course.setText(course.getText() + secString);

        String priceString = priceVal + "";
        price.setText(price.getText() + priceString);


        formatPhoneNumber(number);


        Log.d("Baid", "Number is " + phoneNumber);

        emailValue = emailString;
        Log.d("Baid", emailValue);

        showPhoneNumber = showNumber;
    }



    private void formatPhoneNumber(Number number){

        if(number != null) {

            phoneNumber = number + "";

            //if number is negative, it cannot possibly be valid
            if(number.intValue() < 0)
                validNumber = false;

        }


        int eIndex = phoneNumber.indexOf("E");

        if(eIndex != -1){

            phoneNumber = phoneNumber.substring(0, eIndex);
            Log.d("Baid", "phone number is " + phoneNumber);

        }
        int pIndex = phoneNumber.indexOf(".");
        if(pIndex != -1){


            if(pIndex < phoneNumber.length() - 1){

                 phoneNumber = phoneNumber.substring(0, pIndex) + phoneNumber.substring(pIndex + 1);
            }
            else{

                phoneNumber = phoneNumber.substring(0, pIndex);
            }
        }
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
        price = (TextView) findViewById(R.id.price_edit);
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

        //CALL USER
        if(id == call.getId()){

            if(phoneNumber == null){

                Log.d("Baid", "Phone number is null");
                return;
            }

            if(showPhoneNumber == false || !validNumber){

                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Alert ");
                alertDialog.setMessage("This user's phone number is not available");
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
                return;


            }


            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            context.startActivity(intent);
        }
        //TEXT MESSAGE USER
        else if(id == message.getId()){

            if(phoneNumber == null){

                Log.d("Baid", "Phone number is null");
                return;
            }

            if(showPhoneNumber == false || !validNumber){

                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Alert ");
                alertDialog.setMessage("This user's phone number is not available");
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
                return;


            }

            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setType("vnd.android-dir/mms-sms");
            sendIntent.setData(Uri.parse("sms:" + phoneNumber));
            sendIntent.putExtra("sms_body", "Hey, I just saw your post on Blue Exchange...");
            try{

                context.startActivity(sendIntent);
                Log.d("Baid", "Phone is " + phoneNumber);
            }catch (ActivityNotFoundException e){

                Log.d("Baid", e.getMessage());

                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Something went wrong :/");
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
            }

        }

        //EMAIL USER
        else if(id == email.getId()){

            if(emailValue == null){

                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("This user unfortunately did not provide an email address :(");
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
                Log.d("Baid", "Email is null");
                return;
            }

            email();


        }
    }

    //one way that allows user to send emails
    private void email(){

        final Intent result = new Intent(android.content.Intent.ACTION_SEND);
        result.setType("plain/text");
        result.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { emailValue });
        result.putExtra(android.content.Intent.EXTRA_SUBJECT, "Blue Exchange");
        result.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, I just saw your post on Blue Exchange...");

        try {
            context.startActivity(result);

        }catch (ActivityNotFoundException e){

            Log.d("Baid", e.getMessage());

            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Something went wrong :/");
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();


        }

    }

}
