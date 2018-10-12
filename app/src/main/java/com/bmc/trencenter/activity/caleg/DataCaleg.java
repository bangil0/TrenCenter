package com.bmc.trencenter.activity.caleg;

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
import com.bmc.trencenter.adapter.CalegAdapter;
import com.bmc.trencenter.app.AppConfig;
import com.bmc.trencenter.helper.HttpHandler;
import com.bmc.trencenter.helper.SQLiteHandler;
import com.bmc.trencenter.model.Caleg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class DataCaleg extends AppCompatActivity {

    private HashMap<String, String> user;
    private Toolbar toolbar;
    private SQLiteHandler db;
    private String tipe;

    private ArrayList<Caleg> calegList;
    private CalegAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_caleg);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.caleg_list);
        calegList = new ArrayList<>();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Data Caleg");
        getSupportActionBar();
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

        new GetCaleg().execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private class GetCaleg extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(AppConfig.URL_GET_DATA_CALEG);

            Log.e("Data Caleg", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray calegs = jsonObj.getJSONArray("caleg");

                    for (int i = 0; i < calegs.length(); i++) {
                        JSONArray program = calegs.getJSONArray(i);

                        String nama = program.getString(4);
                        String foto = program.getString(1);

                        calegList.add(new Caleg(foto, nama));
                    }
                } catch (final JSONException e) {
                    Log.e("Data Caleg", "Json parsing error: " + e.getMessage());
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
                Log.e("Data Caleg", "Couldn't get json from server.");
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
            adapter = new CalegAdapter(getApplicationContext(), calegList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(), DetailCaleg.class);
                    intent.putExtra("INDEX", i);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
