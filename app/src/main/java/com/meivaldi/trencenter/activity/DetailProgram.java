package com.meivaldi.trencenter.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.meivaldi.trencenter.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DetailProgram extends AppCompatActivity {

    private static final String TAG = DetailProgram.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_program);
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection httpURLConnection = null;

            try {
                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONArray jsonArray = new JSONArray(s);

                for(int i=0; i<jsonArray.length(); i++){
                    JSONArray program = jsonArray.getJSONArray(i);

                    Log.i("Rido Meivaldi", program.getString(0));
                    Log.i("Rido Meivaldi", program.getString(1));
                    Log.i("Rido Meivaldi", program.getString(2));
                    Log.i("Rido Meivaldi", program.getString(3));
                    Log.i("Rido Meivaldi", program.getString(4));
                    Log.i("Rido Meivaldi", program.getString(5));
                    Log.i("Rido Meivaldi", program.getString(6));
                    Log.i("Rido Meivaldi", program.getString(7));
                    Log.i("Rido Meivaldi", program.getString(8));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
