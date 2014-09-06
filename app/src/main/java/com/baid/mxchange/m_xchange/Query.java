package com.baid.mxchange.m_xchange;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Ish on 9/6/14.
 */
public class Query extends Activity {

    Button rest_call;
    TextView text;

    public static String CONSUMER_KEY = "XO49MfZXpbT66RObyDyb_76Zq_Ia";
    public static String CONSUMER_SECRET = "6O6ZENmgCDqmmxsExleytQewBCsa";
    public static String TOKEN = "16a1beda9c2b7bc38dea333348c233b1";

    public static URL url;
    protected void onCreate(Bundle b){
        Parse.initialize(this, "AQ2Vfb0vhbBq3N6t2Aeu4fpLaZ5Xp8HI42P1fOxr", "mkjVzwYH47zFQD6xOMNvwMmRHNxg0QAnDnS7AHUI");

        super.onCreate(b);
        setContentView(R.layout.test_layout);

        Log.d("Baid", "onCreate");
        rest_call = (Button) findViewById(R.id.rest_call);
        text = (TextView) findViewById(R.id.result);

        rest_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("Baid", "onClick");
               // new RequestTask().execute("https://api-km.it.umich.edu/token");
                //TOKEN: https://api-km.it.umich.edu/token

                try{

                    url = new URL("http://api-gw.it.umich.edu/Curriculum/SOC/v1/Terms");
                }   catch(Exception e){

                }

                if(isConnected())
                    new HttpAsyncTask().execute("http://www.android.com/");
            }
        });


    }


    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            JSONObject header = new JSONObject();
            header.put("Authorization", "Bearer " + TOKEN);

            //URL url2 = new URL("http://api-gw.it.umich.edu/Curriculum/SOC/v1/Terms");

            URL link = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) link.openConnection();
            connection.setRequestMethod("GET");
            //HttpURLConnection urlConnection = (HttpURLConnection) url2.openConnection();
            //urlConnection.setRequestMethod("GET");

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();


            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

           // httpResponse.getParams().setParameter("/Curriculum/SOC/v1/Terms", header);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            Log.d("Baid", result);
            text.setText(result);
        }
    }
    /*class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            //httpclient.getParams().setParameter(HttpProtocolParams.USER_AGENT, )
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    responseString = out.toString();
                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("Baid", "Done loading!");
            text.setText(result);
            //Do anything with response..
        }
    }*/



}
