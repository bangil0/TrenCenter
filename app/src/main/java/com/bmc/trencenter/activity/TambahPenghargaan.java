package com.bmc.trencenter.activity;

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
import com.bmc.trencenter.R;
import com.bmc.trencenter.app.AppConfig;
import com.bmc.trencenter.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TambahPenghargaan extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etValue;
    private Button tambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_penghargaan);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        etValue = (EditText) findViewById(R.id.etValue);
        tambah = (Button) findViewById(R.id.tambah);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Penghargaan");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DetailPenghargaanAdmin.class));
            }
        });

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = etValue.getText().toString();

                if(!value.isEmpty()){
                    tambahPenghargaan(value);
                } else {
                    Toast.makeText(getApplicationContext(), "Field tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void tambahPenghargaan(final String value) {
        String tag_string_req = "req_edit_penghargaan";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_TAMBAH_PENGHARGAAN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Edit", "Edit Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String message = jObj.getString("message");

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), DetailPenghargaanAdmin.class));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Edit", "Login Error: " + error.getMessage());
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("penghargaan", value);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
