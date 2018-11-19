package com.bmc.trencenter.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
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
import com.bmc.trencenter.adapter.ReceiverAdapter;
import com.bmc.trencenter.fragment.HomeTimPemenangan;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bmc.trencenter.R;
import com.bmc.trencenter.adapter.UserAdapter;
import com.bmc.trencenter.app.AppConfig;
import com.bmc.trencenter.app.AppController;
import com.bmc.trencenter.helper.SQLiteHandler;
import com.bmc.trencenter.model.Caleg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailLogistik extends AppCompatActivity {

    private static final String TAG = DetailLogistik.class.getSimpleName();

    private TextView title, description;
    private ImageView image;
    private Toolbar toolbar;
    private Button button;

    private SQLiteHandler db;
    private HashMap<String, String> user;
    private String nama;

    private RecyclerView recyclerView;
    private List<Caleg> person;
    private ReceiverAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_logistik);

        db = new SQLiteHandler(getApplicationContext());
        user = db.getUserDetails();

        getDetailLogistik();

        title = (TextView) findViewById(R.id.titleProgram);
        description = (TextView) findViewById(R.id.descriptionProgram);
        image = (ImageView) findViewById(R.id.image);
        button = (Button) findViewById(R.id.scan);

        recyclerView = (RecyclerView) findViewById(R.id.penerima);
        person = new ArrayList<>();
        userAdapter = new ReceiverAdapter(this, person);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(userAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScanLogistik.class);
                intent.putExtra("NAMA", nama);
                startActivity(intent);
            }
        });

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
    }

    private void getDetailLogistik() {
        String tag_string_req = "req_logistik";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_GET_LOGISTIC, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        JSONArray jsonArray = jObj.getJSONArray("logistik");

                        int index = getIntent().getIntExtra("INDEX", 0);
                        JSONArray program = jsonArray.getJSONArray(index);

                        String uid = program.getString(0);
                        nama = program.getString(1);
                        String tanggalMulai = program.getString(2);
                        String tanggalSelesai = program.getString(3);
                        String lokasi = program.getString(4);
                        String deskripsi = program.getString(5);
                        String penanggungJawab = program.getString(6);
                        String gambar = "http://156.67.221.225/voting/dashboard/save/foto_logistik/" + program.getString(7);

                        Glide.with(getApplicationContext()).load(gambar)
                                .crossFade()
                                .thumbnail(0.5f)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(image);

                        title.setText(nama);
                        description.setText(deskripsi);

                        getReceiver(nama);

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

    private void getReceiver(final String nama) {
        String tag_string_req = "req_receiver";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_RECEIVER, new Response.Listener<String>() {

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

                            person.add(new Caleg(fotos, nama));
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
                params.put("logistik", nama);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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
