package com.bmc.trencenter.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.bmc.trencenter.adapter.NewAdapter;
import com.bmc.trencenter.adapter.PartnershipAdapter_;
import com.bmc.trencenter.model.Caleg;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bmc.trencenter.R;
import com.bmc.trencenter.app.AppConfig;
import com.bmc.trencenter.app.AppController;
import com.bmc.trencenter.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailPartnership extends AppCompatActivity {

    private static final String TAG = DetailPartnership.class.getSimpleName();

    private Toolbar toolbar;
    private HashMap<String, String> user;
    private SQLiteHandler db;
    private String tipe;

    private TextView judul, pembuat, kategori, isi;
    private ImageView fotoBerita;
    private Button pakai;

    private RecyclerView recyclerView;
    private ArrayList<Caleg> penggunaList;
    private PartnershipAdapter_ adapter;

    private String id_user, id_partnership;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_partnership);

        judul = (TextView) findViewById(R.id.judul);
        pembuat = (TextView) findViewById(R.id.maker);
        kategori = (TextView) findViewById(R.id.kategori);
        isi = (TextView) findViewById(R.id.isiBerita);
        fotoBerita = (ImageView) findViewById(R.id.fotoBerita);
        pakai = (Button) findViewById(R.id.pakaiPartnership);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Partnership");

        db = new SQLiteHandler(getApplicationContext());
        user = db.getUserDetails();
        tipe = user.get("type");
        id_user = user.get("id");

        recyclerView = (RecyclerView) findViewById(R.id.pengguna);
        penggunaList = new ArrayList<>();
        adapter = new PartnershipAdapter_(this, penggunaList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(tipe.equals("relawan") || tipe.equals("pendukung")){
            pakai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pakaiPartnership();
                }
            });
            recyclerView.setVisibility(View.GONE);
        } else {
            pakai.setVisibility(View.GONE);
        }

        getPartnership();
    }

    private void getPengguna(final String partnership) {
        String tag_string_req = "req_get_pengguna";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_PENGGUNA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Partnership Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        JSONArray fotoArray = jObj.getJSONArray("foto");

                        String nama_user, foto;
                        for(int i=0; i<jsonArray.length(); i++){
                            nama_user = jsonArray.getString(i);
                            foto = fotoArray.getString(i);

                            penggunaList.add(new Caleg(foto, nama_user));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Gagal mendapatkan data!", Toast.LENGTH_LONG).show();
                    }

                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Partnership Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Tidak ada koneksi internet", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_partnership", partnership);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void pakaiPartnership() {
        String tag_string_req = "req_receiver";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PAKAI_PARTNERSHIP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    String msg = jsonObject.getString("error_msg");

                    if(!error){
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PAKAI LAYANAN", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Terjadi kesalahan.", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_partnership", id_partnership);
                params.put("id_user", id_user);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getPartnership() {
        String tag_string_req = "req_partnership";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_GET_ALL_PARTNERSHIP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("BERITA", "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        JSONArray jsonArray = jObj.getJSONArray("partnership");

                        int index = getIntent().getIntExtra("INDEX", 0);
                        JSONArray program = jsonArray.getJSONArray(index);

                        id_partnership = program.getString(0);
                        String category = program.getString(1);
                        String title = program.getString(2);
                        String content = program.getString(4);
                        String gambar = "http://156.67.221.225/voting/dashboard/save/foto_partnership/" + program.getString(7);
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

                        getPengguna(id_partnership);

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
