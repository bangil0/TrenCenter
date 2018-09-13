package com.meivaldi.trencenter.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.app.AppController;
import com.meivaldi.trencenter.fragment.HomeTimPemenangan;
import com.meivaldi.trencenter.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DetailProgram extends AppCompatActivity {

    private static final String TAG = DetailProgram.class.getSimpleName();

    private TextView title, description;
    private ImageView image;
    private Toolbar toolbar;
    private Button button;

    private SQLiteHandler db;
    private HashMap<String, String> user;

    private String uid, idProgram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_program);

        db = new SQLiteHandler(getApplicationContext());
        user = db.getUserDetails();

        getProgram();

        title = (TextView) findViewById(R.id.titleProgram);
        description = (TextView) findViewById(R.id.descriptionProgram);
        image = (ImageView) findViewById(R.id.image);
        button = (Button) findViewById(R.id.join);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idProgram = getIntent().getStringExtra("id");
                joinProgram();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void joinProgram(){
        uid = user.get("id");

        String tag_string_req = "req_join_program";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_JOIN_PROGRAM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    String msg = jObj.getString("error_msg");

                    if (!error) {
                        Toast.makeText(getApplicationContext(),
                                msg, Toast.LENGTH_SHORT).show();

                        button.setEnabled(false);
                        button.setText("Menunggu Verifikasi");
                    } else {
                        Toast.makeText(getApplicationContext(),
                                msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Tidak ada koneksi internet", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_user", uid);
                params.put("id_program", idProgram);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getProgram() {
        String tag_string_req = "req_program";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.GET_PROGRAM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        JSONArray jsonArray = jObj.getJSONArray("programs");

                        int index = getIntent().getIntExtra("INDEX", 0);
                        JSONArray program = jsonArray.getJSONArray(index);

                        String uid = program.getString(0);
                        String nama = program.getString(1);
                        String tanggalMulai = program.getString(2);
                        String tanggalSelesai = program.getString(3);
                        String lokasi = program.getString(4);
                        String deskripsi = program.getString(5);
                        String penanggungJawab = program.getString(6);
                        String gambar = "http://103.28.53.181/~millenn1/dashboard/save/foto_program/" + program.getString(7);

                        Glide.with(getApplicationContext()).load(gambar)
                                .crossFade()
                                .thumbnail(0.5f)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(image);

                        title.setText(nama);
                        description.setText(deskripsi);

                    } else {
                        String errorMsg = jObj.getString("error_msg");
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
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Tidak ada koneksi internet", Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
