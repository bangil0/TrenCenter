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
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditVisiMisi extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etValue;
    private Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_visi_misi);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        etValue = (EditText) findViewById(R.id.etValue);
        update = (Button) findViewById(R.id.ubah);

        final String tipe = getIntent().getExtras().getString("TIPE");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit " + tipe);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), VisiMisiAdmin.class));
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = etValue.getText().toString();
                String old = getIntent().getStringExtra("OLD");
                String type = getIntent().getStringExtra("TIPE");
                String tipe = type.toLowerCase();

                if(!value.isEmpty()){
                    updateVisiMisi(value, old, tipe);
                } else {
                    Toast.makeText(getApplicationContext(), "Field tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void updateVisiMisi(final String value, final String old, final String tipe) {
        String tag_string_req = "req_edit_visi_misi";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_EDIT_VISI_MISI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Edit", "Edit Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String message = jObj.getString("message");

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), VisiMisiAdmin.class));

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
                params.put("old", old);
                params.put("value", value);
                params.put("tipe", tipe);

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
