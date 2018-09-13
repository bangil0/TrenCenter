package com.meivaldi.trencenter.activity.tim_pemenangan;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.activity.DetailProgram;
import com.meivaldi.trencenter.activity.ProgramKerja;
import com.meivaldi.trencenter.activity.relawan.MainActivity;
import com.meivaldi.trencenter.adapter.ProgramAdapter;
import com.meivaldi.trencenter.helper.HttpHandler;
import com.meivaldi.trencenter.model.Program;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProgramKerja_TimPemenangan extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Program> programList;
    private ProgramAdapter adapter;
    private Toolbar toolbar;

    private List<String> idProgram;

    private static final String TAG = ProgramKerja.class.getSimpleName();
    private static final String url = "http://103.28.53.181/~millenn1/android/getProgram.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_kerja_tim_pemenangan);

        listView = (ListView) findViewById(R.id.program_list);
        programList = new ArrayList<>();
        idProgram = new ArrayList<>();

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

        new ProgramKerja_TimPemenangan.GetPrograms().execute();
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

    private class GetPrograms extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray programs = jsonObj.getJSONArray("programs");

                    for (int i = 0; i < programs.length(); i++) {
                        JSONArray program = programs.getJSONArray(i);

                        idProgram.add(program.getString(0));
                        String nama = program.getString(1);
                        String tanggalMulai = program.getString(2);
                        String lokasi = program.getString(4);
                        String foto = program.getString(7);

                        programList.add(new Program(nama, tanggalMulai, lokasi, foto));
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
            adapter = new ProgramAdapter(getApplicationContext(), programList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(), DetailProgram_TimPemenangan.class);
                    intent.putExtra("INDEX", i);
                    intent.putExtra("id", idProgram.get(i));
                    startActivity(intent);
                }
            });
        }
    }

}
