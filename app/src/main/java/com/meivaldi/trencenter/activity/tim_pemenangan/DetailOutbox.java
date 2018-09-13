package com.meivaldi.trencenter.activity.tim_pemenangan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.activity.pendukung.Pendukung;
import com.meivaldi.trencenter.activity.relawan.MainActivity;
import com.meivaldi.trencenter.activity.super_admin.Dashboard_SuperAdmin;
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.app.AppController;
import com.meivaldi.trencenter.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 07/09/18.
 */

public class DetailOutbox extends AppCompatActivity {

    private Toolbar toolbar;
    private HashMap<String, String> user;
    private SQLiteHandler db;
    private String tipe, receiver;

    private TextView penerima, isiPesan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesan);

        penerima = (TextView) findViewById(R.id.pengirim);
        isiPesan = (TextView) findViewById(R.id.isi_pesan);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pesan");

        db = new SQLiteHandler(getApplicationContext());
        user = db.getUserDetails();
        tipe = user.get("type");
        receiver = user.get("username");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tipe.equals("super_admin")) {
                    Intent intent = new Intent(getApplicationContext(),
                            Dashboard_SuperAdmin.class);
                    startActivity(intent);
                } else if (tipe.equals("relawan")) {
                    Intent intent = new Intent(getApplicationContext(),
                            MainActivity.class);
                    startActivity(intent);
                } else if (tipe.equals("pendukung")) {
                    Intent intent = new Intent(getApplicationContext(),
                            Pendukung.class);
                    startActivity(intent);
                } else if (tipe.equals("tim_pemenangan")) {
                    Intent intent = new Intent(getApplicationContext(),
                            Tim_Pemenangan.class);
                    startActivity(intent);
                }
            }
        });

        getInbox(receiver, tipe);
    }

    private void getInbox(final String receiver, final String tipe) {
        String tag_string_req = "req_program";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_OUTBOX, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Inbox", "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        JSONArray jsonArray = jObj.getJSONArray("outbox");

                        int index = getIntent().getIntExtra("INDEX", 0);
                        JSONArray inbox = jsonArray.getJSONArray(index);

                        String sender = inbox.getString(3);
                        String message = inbox.getString(7);

                        penerima.setText("Dari: " + sender);
                        isiPesan.setText("Pesan:\n" + message);
                    } else {
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Inbox", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Tidak ada koneksi internet", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pengirim", receiver);
                params.put("tipe", tipe);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(tipe.equals("super_admin")){
            Intent intent = new Intent(getApplicationContext(),
                    Dashboard_SuperAdmin.class);
            startActivity(intent);
        } else if(tipe.equals("relawan")){
            Intent intent = new Intent(getApplicationContext(),
                    MainActivity.class);
            startActivity(intent);
        } else if(tipe.equals("pendukung")){
            Intent intent = new Intent(getApplicationContext(),
                    Pendukung.class);
            startActivity(intent);
        } else if(tipe.equals("tim_pemenangan")) {
            Intent intent = new Intent(getApplicationContext(),
                    Tim_Pemenangan.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}