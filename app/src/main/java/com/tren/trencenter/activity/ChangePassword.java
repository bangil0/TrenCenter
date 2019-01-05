package com.tren.trencenter.activity;

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
import com.tren.trencenter.R;
import com.tren.trencenter.app.AppConfig;
import com.tren.trencenter.app.AppController;
import com.tren.trencenter.helper.SQLiteHandler;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText passwordLama, passwordBaru;
    private Button change;

    private SQLiteHandler db;
    private String tipe, id;

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
        id = user.get("id");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lama = passwordLama.getText().toString();
                String baru = passwordBaru.getText().toString();

                updatePassword(lama, baru, id);
            }
        });
    }

    private void updatePassword(final String lama, final String baru, final String id) {
        String tag_string_req = "req_update";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE_PASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("UPDATE", "Login Response: " + response.toString());
                Toast.makeText(getApplicationContext(), "Berhasil di Update!", Toast.LENGTH_SHORT).show();
                finish();
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
                params.put("id", id);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
