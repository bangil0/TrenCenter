package com.meivaldi.trencenter.activity.caleg;

import android.content.Intent;
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
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.activity.pendukung.Pendukung;
import com.meivaldi.trencenter.activity.relawan.MainActivity;
import com.meivaldi.trencenter.activity.super_admin.Dashboard_SuperAdmin;
import com.meivaldi.trencenter.activity.tim_pemenangan.Tim_Pemenangan;
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.app.AppController;
import com.meivaldi.trencenter.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DetailCaleg extends AppCompatActivity {

    private ImageView fotoCaleg;
    private TextView namaCaleg, tempatCaleg, tanggalCaleg,
            umurCaleg, statusCaleg, genderCaleg, sukuCaleg, agamaCaleg,
            hpCaleg, alamatCaleg, fbCaleg, igCaleg;

    private HashMap<String, String> user;
    private Toolbar toolbar;
    private SQLiteHandler db;
    private String tipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_caleg);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        fotoCaleg = (ImageView) findViewById(R.id.foto_caleg);
        namaCaleg = (TextView) findViewById(R.id.namaCalon);
        tempatCaleg = (TextView) findViewById(R.id.tempatLahir);
        tanggalCaleg = (TextView) findViewById(R.id.tanggalLahir);
        umurCaleg = (TextView) findViewById(R.id.umur);
        statusCaleg = (TextView) findViewById(R.id.status);
        genderCaleg = (TextView) findViewById(R.id.jenisKelamin);
        sukuCaleg = (TextView) findViewById(R.id.suku);
        agamaCaleg = (TextView) findViewById(R.id.agama);
        hpCaleg = (TextView) findViewById(R.id.noHP);
        alamatCaleg = (TextView) findViewById(R.id.alamat);
        fbCaleg = (TextView) findViewById(R.id.facebook);
        igCaleg = (TextView) findViewById(R.id.instagram);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        db = new SQLiteHandler(getApplicationContext());
        user = db.getUserDetails();
        tipe = user.get("type");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getData();
    }

    private void getData() {
        String tag_string_req = "req_get_caleg";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_CALEG, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    int index = getIntent().getIntExtra("INDEX", 0);

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("caleg");
                    JSONArray calegData = jsonArray.getJSONArray(index);

                    String foto = "http://103.28.53.181/~millenn1/dashboard/save/foto_caleg/" + calegData.getString(1);
                    String namaValue = calegData.getString(4);
                    String tempatLahirValue = calegData.getString(5);
                    String tanggalLahirValue = calegData.getString(6);
                    String umurValue = calegData.getString(7);
                    String statusValue = calegData.getString(8);
                    String jenisKelaminValue = calegData.getString(9);
                    String sukuValue = calegData.getString(10);
                    String agamaValue = calegData.getString(11);
                    String hpValue = calegData.getString(12);
                    String alamatValue = calegData.getString(13);
                    String facebook = calegData.getString(20);
                    String instagram = calegData.getString(21);

                    namaCaleg.setText(namaValue);
                    tempatCaleg.setText(tempatLahirValue);
                    tanggalCaleg.setText(tanggalLahirValue);
                    umurCaleg.setText(umurValue);
                    statusCaleg.setText(statusValue);
                    genderCaleg.setText(jenisKelaminValue);
                    sukuCaleg.setText(sukuValue);
                    agamaCaleg.setText(agamaValue);
                    hpCaleg.setText(hpValue);
                    alamatCaleg.setText(alamatValue);
                    fbCaleg.setText(" " + facebook);
                    igCaleg.setText(" " + instagram);

                    Glide.with(getApplicationContext()).load(foto)
                            .crossFade()
                            .thumbnail(0.5f)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(fotoCaleg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Tambah Relawan", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Berhasil menambah data", Toast.LENGTH_LONG).show();
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

        finish();
    }
}
