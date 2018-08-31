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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.app.AppController;
import com.meivaldi.trencenter.fragment.AccountFragment;
import com.meivaldi.trencenter.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangeUsername extends AppCompatActivity{

    private Toolbar toolbar;
    private EditText usernameLama, usernameBaru;
    private Button change;

    private SQLiteHandler db;

    private String tipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        usernameBaru = (EditText) findViewById(R.id.newUsername);
        usernameLama = (EditText) findViewById(R.id.oldUsername);

        change = (Button) findViewById(R.id.changeUSername);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        tipe = user.get("type");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tipe.equals("relawan")){
                    Intent intent = new Intent(ChangeUsername.this,
                            Dashboard_SuperAdmin.class);
                    startActivity(intent);
                } else if(tipe.equals("Relawan")){
                    Intent intent = new Intent(ChangeUsername.this,
                            MainActivity.class);
                    startActivity(intent);
                } else if(tipe.equals("tim_pemenangan")){
                    Intent intent = new Intent(ChangeUsername.this,
                            MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lama = usernameLama.getText().toString();
                String baru = usernameBaru.getText().toString();

                if(lama.isEmpty() || baru.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Field tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                update(lama, baru);
            }
        });

    }

    private void update(final String lama, final String baru) {
        String tag_string_req = "req_update";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE, new Response.Listener<String>() {

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
    protected void onPause() {
        super.onPause();
        finish();
    }
}
