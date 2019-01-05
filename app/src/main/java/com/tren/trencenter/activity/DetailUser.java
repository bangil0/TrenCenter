package com.tren.trencenter.activity;

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
import com.tren.trencenter.R;
import com.tren.trencenter.app.AppConfig;
import com.tren.trencenter.app.AppController;
import com.tren.trencenter.helper.CircleTransform;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailUser extends AppCompatActivity {

    private String nik;
    private ImageView foto_user;
    private Toolbar toolbar;

    private TextView user_nama, birth_day, birth_place,
        age, marriage, jenis_kelamin, tribe, religion,
        address, phone_number, fb, ig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        user_nama = (TextView) findViewById(R.id.namaCalon);
        birth_day = (TextView) findViewById(R.id.tanggalLahir);
        birth_place = (TextView) findViewById(R.id.tempatLahir);
        age = (TextView) findViewById(R.id.umur);
        marriage = (TextView) findViewById(R.id.status);
        jenis_kelamin = (TextView) findViewById(R.id.jenisKelamin);
        tribe = (TextView) findViewById(R.id.suku);
        religion = (TextView) findViewById(R.id.agama);
        address = (TextView) findViewById(R.id.alamat);
        phone_number = (TextView) findViewById(R.id.noHP);
        fb = (TextView) findViewById(R.id.facebook);
        ig = (TextView) findViewById(R.id.instagram);

        foto_user = (ImageView) findViewById(R.id.foto_user);
        nik = getIntent().getStringExtra("nik");

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

        loadData(nik);
    }

    private void loadData(final String nik) {
        String tag_string_req = "req_user_detail";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_USER_DETAIL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String nama = jsonObject.getString("nama");
                    String foto = jsonObject.getString("foto");
                    String birthPlace = jsonObject.getString("tempat_lahir");
                    String birthDate = jsonObject.getString("tanggal_lahir");
                    String umur = jsonObject.getString("umur");
                    String status = jsonObject.getString("status");
                    String gender = jsonObject.getString("jenis_kelamin");
                    String suku = jsonObject.getString("suku");
                    String hp = jsonObject.getString("hp");
                    String alamat = jsonObject.getString("alamat") + ", Kelurahan " + jsonObject.getString("kelurahan")
                            + ", Kecamatan " + jsonObject.getString("kecamatan") + ", Kabupaten " + jsonObject.getString("kabupaten");
                    String facebook = jsonObject.getString("facebook");
                    String instagram = jsonObject.getString("instagram");

                    Glide.with(getApplicationContext()).load(foto)
                            .crossFade()
                            .thumbnail(0.5f)
                            .bitmapTransform(new CircleTransform(getApplicationContext()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(foto_user);

                    user_nama.setText(nama);
                    birth_place.setText(birthPlace);
                    birth_day.setText(birthDate);
                    age.setText(umur);
                    marriage.setText(status);
                    jenis_kelamin.setText(gender);
                    tribe.setText(suku);
                    phone_number.setText(hp);
                    address.setText(alamat);
                    fb.setText(" " + facebook);
                    ig.setText(" " + instagram);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SCAN KARTU", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Terjadi kesalahan.", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nik", nik);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
