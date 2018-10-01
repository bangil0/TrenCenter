package com.meivaldi.trencenter.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.adapter.UserAdapter;
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.app.AppController;
import com.meivaldi.trencenter.model.Caleg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DaftarPemenangan extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;

    private ArrayList<Caleg> pemenanganList;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pemenangan);

        listView = (ListView) findViewById(R.id.pemenangan_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pemenanganList = new ArrayList<>();
        adapter = new UserAdapter(this, pemenanganList);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getRelawan();
    }

    private void getRelawan(){
        String tag_string_req = "req_pemenangan";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_PEMENANGAN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Inbox", "Login Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        JSONArray data = jObj.getJSONArray("data");
                        JSONArray foto = jObj.getJSONArray("foto");

                        for(int i=0; i<data.length(); i++){
                            pemenanganList.add(new Caleg("http://156.67.221.225/trencenter/voting/dashboard/save/foto_pemenangan/" + foto.getString(i),
                                    data.getString(i)));
                        }

                        listView.setAdapter(adapter);

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
                Log.e("Inbox", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Tidak ada koneksi internet", Toast.LENGTH_LONG).show();
            }
        });

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
