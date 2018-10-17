package com.bmc.trencenter.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bmc.trencenter.R;
import com.bmc.trencenter.activity.tim_pemenangan.DetailLayanan_TimPemenangan;
import com.bmc.trencenter.adapter.LogistikAdapter;
import com.bmc.trencenter.app.AppConfig;
import com.bmc.trencenter.helper.HttpHandler;
import com.bmc.trencenter.helper.SQLiteHandler;
import com.bmc.trencenter.model.Logistik;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LayananActivity extends AppCompatActivity {

    private ListView listView;
    private LogistikAdapter adapter;
    private ArrayList<Logistik> logistikList;

    private Toolbar toolbar;
    private SQLiteHandler db;

    private String tipe;
    private static final String TAG = LayananActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistik);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.logistik_list);
        logistikList = new ArrayList<>();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        tipe = user.get("type");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        new GetLogistic().execute();
    }

    private class GetLogistic extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(AppConfig.URL_GET_ALL_LAYANAN);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray programs = jsonObj.getJSONArray("layanan");

                    for (int i = 0; i < programs.length(); i++) {
                        JSONArray program = programs.getJSONArray(i);

                        String nama = program.getString(1);
                        String tanggalMulai = program.getString(2);
                        String lokasi = program.getString(3);
                        String foto = "http://156.67.221.225/voting/dashboard/save/foto_layanan/" + program.getString(5);

                        logistikList.add(new Logistik(nama, tanggalMulai, lokasi, foto));
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new LogistikAdapter(getApplicationContext(), logistikList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(tipe.equals("super_admin") || tipe.equals("caleg")){
                        Intent intent = new Intent(getApplicationContext(), DetailLayanan_TimPemenangan.class);
                        intent.putExtra("INDEX", i);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), DetailLayanan.class);
                        intent.putExtra("INDEX", i);
                        startActivity(intent);
                    }
                }
            });
        }
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
