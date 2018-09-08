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

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText passwordLama, passwordBaru;
    private Button change;

    private SQLiteHandler db;
    private String tipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        passwordLama = (EditText) findViewById(R.id.oldPassword);
        passwordBaru = (EditText) findViewById(R.id.newPassword);
        change = (Button) findViewById(R.id.changePassword);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        tipe = user.get("type");

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
                            MainActivity.class);
                    startActivity(intent);
                } else if(tipe.equals("tim_pemenangan")) {
                    Intent intent = new Intent(getApplicationContext(),
                            Tim_Pemenangan.class);
                    startActivity(intent);
                }
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lama = passwordLama.getText().toString();
                String baru = passwordBaru.getText().toString();

                updatePassword(lama, baru);
            }
        });
    }

    private void updatePassword(final String lama, final String baru) {
        String tag_string_req = "req_update";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE_PASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("UPDATE", "Login Response: " + response.toString());
                Toast.makeText(getApplicationContext(), "Berhasil di Update!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("UPDATE", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Error", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("lama", lama);
                params.put("baru", baru);

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
}
