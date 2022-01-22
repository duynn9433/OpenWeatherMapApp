package com.duynn.openweathermapapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    private static final String apiKey = "9ae9e45a439d7168be105de77fd026a4";
    private String location = "Hanoi";
    String urlString = "https://api.openweathermap.org/data/2.5/weather?q="+location+"&APPID=" + apiKey;


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
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonI = jsonArray.getJSONObject(i);
                    Log.i("Info",jsonI.toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new APICaller().execute(urlString);
    }
}