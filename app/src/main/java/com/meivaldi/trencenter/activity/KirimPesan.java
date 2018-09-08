package com.meivaldi.trencenter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.activity.pendukung.Pendukung;
import com.meivaldi.trencenter.activity.relawan.MainActivity;
import com.meivaldi.trencenter.activity.super_admin.Dashboard_SuperAdmin;
import com.meivaldi.trencenter.activity.tim_pemenangan.Tim_Pemenangan;
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.app.AppController;
import com.meivaldi.trencenter.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class KirimPesan extends AppCompatActivity {

    private Toolbar toolbar;
    private HashMap<String, String> user;
    private SQLiteHandler db;
    private String tipe, pengirim;

    private EditText username, message;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kirim_pesan);

        username = (EditText) findViewById(R.id.username);
        message = (EditText) findViewById(R.id.message);
        send = (Button) findViewById(R.id.send);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Kirim Pesan");

        db = new SQLiteHandler(getApplicationContext());
        user = db.getUserDetails();
        tipe = user.get("type");
        pengirim = user.get("username");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String penerima = username.getText().toString();
                String isi = message.getText().toString();

                sendMessage(pengirim, penerima, isi);
            }
        });
    }

    private void sendMessage(final String sender, final String receiver, final String isi) {
        String tag_string_req = "req_send_message";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SEND_MESSAGE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Kirim Pesan", "Mengirim Pesan");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String pesan = jsonObject.getString("error_msg");
                    boolean error = jsonObject.getBoolean("error");

                    if(!error){
                        Toast.makeText(getApplicationContext(), pesan, Toast.LENGTH_SHORT).show();

                        username.setText("");
                        message.setText("");

                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), pesan, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Kirim Pesan", "Sending Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Terjadi kesalahan.", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pengirim", sender);
                params.put("penerima", receiver);
                params.put("isi", isi);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}
