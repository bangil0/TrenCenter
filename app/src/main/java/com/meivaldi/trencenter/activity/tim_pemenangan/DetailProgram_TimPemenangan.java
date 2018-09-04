package com.meivaldi.trencenter.activity.tim_pemenangan;

import android.content.Intent;
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
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.activity.DetailProgram;
import com.meivaldi.trencenter.activity.ProgramKerja;
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailProgram_TimPemenangan extends AppCompatActivity {

    private static final String TAG = DetailProgram.class.getSimpleName();

    private TextView title, description;
    private ImageView image;
    private Toolbar toolbar;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_program__tim_pemenangan);
        getProgram();

        title = (TextView) findViewById(R.id.titleProgram);
        description = (TextView) findViewById(R.id.descriptionProgram);
        image = (ImageView) findViewById(R.id.image);
        button = (Button) findViewById(R.id.join);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Berhasil bergabung", Toast.LENGTH_SHORT).show();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProgramKerja_TimPemenangan.class));
            }
        });
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

                        String nama = program.getString(1);
                        String tanggalMulai = program.getString(2);
                        String tanggalSelesai = program.getString(3);
                        String lokasi = program.getString(4);
                        String deskripsi = program.getString(5);
                        String penanggungJawab = program.getString(6);
                        String gambar = program.getString(7);

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
        startActivity(new Intent(getApplicationContext(), ProgramKerja_TimPemenangan.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
