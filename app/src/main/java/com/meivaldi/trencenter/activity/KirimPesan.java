package com.meivaldi.trencenter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KirimPesan extends AppCompatActivity {

    private Toolbar toolbar;
    private HashMap<String, String> user;
    private SQLiteHandler db;
    private String nama, pengirim, init = " -- PILIH NAMA -- ";

    private Spinner list;
    private EditText message;
    private Button send;
    private List<String> nameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kirim_pesan);

        message = (EditText) findViewById(R.id.message);
        send = (Button) findViewById(R.id.send);
        list = (Spinner) findViewById(R.id.list_nama);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Kirim Pesan");

        db = new SQLiteHandler(getApplicationContext());
        user = db.getUserDetails();
        nama = user.get("name");
        pengirim = user.get("username");

        nameList = new ArrayList<>();
        nameList.add(init);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String penerima = list.getSelectedItem().toString();
                String isi = message.getText().toString();

                if(penerima.equals(init)){
                    Toast.makeText(getApplicationContext(), "Pilih penerima!", Toast.LENGTH_SHORT).show();

                    return;
                } else {
                    sendMessage(pengirim, penerima, isi);
                }
            }
        });

        getListName(nama);
    }

    private void getListName(final String nama_user) {
        String tag_string_req = "req_user_name";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_NAMES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Kirim Pesan", "Mengambil daftar nama");
                Log.d("Response", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");

                    if(!error){
                        JSONArray jsonArray = jsonObject.getJSONArray("nama");

                        for(int i=0; i<jsonArray.length(); i++){
                            nameList.add(jsonArray.getJSONArray(i).getString(0));
                        }

                        ArrayAdapter<String> namaAdapter = new ArrayAdapter<String>(getApplicationContext(),
                                R.layout.spinner_item, nameList);

                        list.setAdapter(namaAdapter);
                    } else {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Kirim Pesan", "Sending Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Terjadi kesalahan.", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama", nama_user);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void sendMessage(final String sender, final String receiver, final String isi) {
        String tag_string_req = "req_send_message";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SEND_MESSAGE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Kirim Pesan", "Mengirim Pesan");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String pesan = jsonObject.getString("error_msg");
                    boolean error = jsonObject.getBoolean("error");

                    if(!error){
                        Toast.makeText(getApplicationContext(), pesan, Toast.LENGTH_SHORT).show();

                        message.setText("");

                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), pesan, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Kirim Pesan", "Sending Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Terjadi kesalahan.", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pengirim", sender);
                params.put("penerima", receiver);
                params.put("isi", isi);

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
