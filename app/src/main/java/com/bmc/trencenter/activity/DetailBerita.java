package com.bmc.trencenter.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bmc.trencenter.R;
import com.bmc.trencenter.app.AppConfig;
import com.bmc.trencenter.app.AppController;
import com.bmc.trencenter.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DetailBerita extends AppCompatActivity {

    private Toolbar toolbar;
    private HashMap<String, String> user;
    private SQLiteHandler db;
    private String tipe;
    
    private TextView judul, pembuat, kategori, isi, sumber;
    private ImageView fotoBerita;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_berita);
        
        judul = (TextView) findViewById(R.id.judul);
        pembuat = (TextView) findViewById(R.id.maker);
        kategori = (TextView) findViewById(R.id.kategori);
        isi = (TextView) findViewById(R.id.isiBerita);
        sumber = (TextView) findViewById(R.id.sumber); 
        fotoBerita = (ImageView) findViewById(R.id.fotoBerita);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Berita");

        db = new SQLiteHandler(getApplicationContext());
        user = db.getUserDetails();
        tipe = user.get("type");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBerita();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        
        getBerita();
    }

    private void getBerita() {
        String tag_string_req = "req_berita";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_GET_BERITA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("BERITA", "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        JSONArray jsonArray = jObj.getJSONArray("berita");
                        int index = getIntent().getIntExtra("INDEX", 0);

                        JSONArray program = jsonArray.getJSONArray(index);

                        String category = program.getString(1);
                        String title = program.getString(2);
                        String content = program.getString(4);
                        String sender = program.getString(5);
                        String gambar = "http://156.67.221.225/voting/dashboard/save/foto_berita/" + program.getString(7);
                        String maker = program.getString(8);

                        Glide.with(getApplicationContext()).load(gambar)
                                .crossFade()
                                .thumbnail(0.5f)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(fotoBerita);

                        judul.setText(title);
                        pembuat.setText("Oleh: " + maker);
                        kategori.setText("Kategori: " + category);
                        isi.setText(content);
                        sumber.setText("Penulis: " + sender);

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
                Log.e("Berita", "Login Error: " + error.getMessage());
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
}
