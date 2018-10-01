package com.meivaldi.trencenter.activity;

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

import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.adapter.PartnershipAdapter;
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.helper.HttpHandler;
import com.meivaldi.trencenter.model.BeritaModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Partnership extends AppCompatActivity {

    private ListView listView;
    private ArrayList<BeritaModel> partnerList;
    private PartnershipAdapter adapter;

    private Toolbar toolbar;

    private static final String TAG = ProgramKerja.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partnership);

        listView = (ListView) findViewById(R.id.partnership_list);
        partnerList = new ArrayList<>();

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

        new GetPartnership().execute();
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

    private class GetPartnership extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(AppConfig.URL_GET_PARTNERSHIP);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray berita = jsonObj.getJSONArray("partnership");

                    for (int i = 0; i < berita.length(); i++) {
                        JSONArray news = berita.getJSONArray(i);

                        String title = news.getString(3);
                        String foto = news.getString(7);

                        partnerList.add(new BeritaModel(title, foto));
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
            adapter = new PartnershipAdapter(getApplicationContext(), partnerList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(), DetailPartnership.class);
                    intent.putExtra("INDEX", i);
                    startActivity(intent);
                }
            });
        }
    }
}
