package com.bmc.trencenter.activity.tim_pemenangan;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bmc.trencenter.activity.DetailLogistik;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bmc.trencenter.R;
import com.bmc.trencenter.activity.DetailProgram;
import com.bmc.trencenter.activity.ScanQR;
import com.bmc.trencenter.adapter.UserAdapter;
import com.bmc.trencenter.app.AppConfig;
import com.bmc.trencenter.app.AppController;
import com.bmc.trencenter.model.Caleg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailProgram_TimPemenangan extends AppCompatActivity {

    private static final String TAG = DetailProgram.class.getSimpleName();

    private TextView title, description;
    private ImageView image;
    private Toolbar toolbar;
    private Button scan;
    private String id;
    private boolean nav;
    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<Caleg> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_program__tim_pemenangan);
        getProgram();

        title = (TextView) findViewById(R.id.titleProgram);
        description = (TextView) findViewById(R.id.descriptionProgram);
        image = (ImageView) findViewById(R.id.image);
        scan = (Button) findViewById(R.id.scanPeserta);
        recyclerView = (RecyclerView) findViewById(R.id.peserta);

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(this, userList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(userAdapter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav = getIntent().getBooleanExtra("MAIN", false);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScanQR.class);
                intent.putExtra("ID", id);
                startActivity(intent);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nav){
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), ProgramKerja_TimPemenangan.class));
                }
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

                        id = program.getString(0);
                        String nama = program.getString(1);
                        String tanggalMulai = program.getString(2);
                        String tanggalSelesai = program.getString(3);
                        String lokasi = program.getString(4);
                        String deskripsi = program.getString(5);
                        String penanggungJawab = program.getString(6);
                        String gambar = "http://156.67.221.225/voting/dashboard/save/foto_program/" + program.getString(7);

                        title.setText(nama);
                        description.setText(deskripsi);

                        Glide.with(getApplicationContext()).load(gambar)
                                .crossFade()
                                .thumbnail(0.5f)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(image);

                        loadPeserta(id);

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

    private void loadPeserta(final String id) {
        String tag_string_req = "req_user_detail";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_PESERTA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");

                    if(!error){
                        JSONArray array = jsonObject.getJSONArray("data");
                        JSONArray foto = jsonObject.getJSONArray("foto");

                        String nama, fotos;

                        for(int i=0; i<array.length(); i++){
                            nama = array.getString(i);
                            fotos = foto.getString(i);

                            userList.add(new Caleg(fotos, nama));
                        }

                        userAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                    }

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
                params.put("id_program", id);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(nav){
            finish();
        } else {
            startActivity(new Intent(getApplicationContext(), ProgramKerja_TimPemenangan.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scan_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_scan){
            startActivity(new Intent(getApplicationContext(), ScanQR.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }

    private int dpToPx(int dp) throws IllegalStateException {
        Resources r = getResources();

        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}
