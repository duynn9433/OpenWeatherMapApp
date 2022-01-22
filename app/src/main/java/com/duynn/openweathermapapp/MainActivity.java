package com.duynn.openweathermapapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private static final String apiKey = "9ae9e45a439d7168be105de77fd026a4";
    private String location = "Hanoi";
    String urlString = "https://api.openweathermap.org/data/2.5/weather?q="+location+"&APPID=" + apiKey;
    private TextView city;
    private TextView outputTextView;

    public void getWeather(View view){
        APICaller apiCaller = new APICaller();
        location = city.getText().toString();
        try {
            //encode to url
            location = URLEncoder.encode(location,"UTF-8");
            urlString = "https://api.openweathermap.org/data/2.5/weather?q="+location+"&APPID=" + apiKey;
            apiCaller.execute(urlString);

            //hide keyboard after click button
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(city.getWindowToken(),0);
        } catch (UnsupportedEncodingException e) {
//            Toast.makeText(getApplicationContext(),"Could not find weather!",Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }



    }
    public class APICaller extends AsyncTask<String,Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                URLConnection urlConnection = url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    stringBuilder.append((char) data) ;
                    data = reader.read();
                }
                return stringBuilder.toString();


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                System.out.println(jsonObject.toString());

                String weather = jsonObject.getString("weather");
                JSONArray jsonArray = new JSONArray(weather);

                String msg = "";
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonI = jsonArray.getJSONObject(i);
                    String main = jsonI.getString("main");
                    String des = jsonI.getString("description");
                    if(!main.equals("") && !des.equals("")){
                        msg+= main + ": "+des+"\n";
                    }
                    Log.i("Info",jsonI.toString());
                }
                if(!msg.equals("")){
                    outputTextView.setText(msg);
                }
                else
                    Toast.makeText(getApplicationContext(),"Could not find weather!",Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Could not find weather!",Toast.LENGTH_LONG).show();
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = findViewById(R.id.editTextLocation);
        outputTextView = findViewById(R.id.textViewOutput);
//        new APICaller().execute(urlString);
    }
}