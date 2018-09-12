package com.meivaldi.trencenter.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.activity.caleg.DetailCaleg;
import com.meivaldi.trencenter.activity.pendukung.Pendukung;
import com.meivaldi.trencenter.activity.relawan.MainActivity;
import com.meivaldi.trencenter.activity.super_admin.Dashboard_SuperAdmin;
import com.meivaldi.trencenter.activity.tim_pemenangan.Tim_Pemenangan;
import com.meivaldi.trencenter.adapter.CalegAdapter;
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.app.AppController;
import com.meivaldi.trencenter.fragment.InboxFragment;
import com.meivaldi.trencenter.fragment.MisiFragment;
import com.meivaldi.trencenter.fragment.OutboxFragment;
import com.meivaldi.trencenter.fragment.VisiFragment;
import com.meivaldi.trencenter.helper.HttpHandler;
import com.meivaldi.trencenter.helper.SQLiteHandler;
import com.meivaldi.trencenter.helper.TabAdapter;
import com.meivaldi.trencenter.model.Caleg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailVisiMisi extends AppCompatActivity {

    private Toolbar toolbar;
    private String[] visi, misi;
    private ListView listView;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_visi_misi);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Visi Misi Caleg");
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),
                            VisiMisi.class);
                    startActivity(intent);
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new VisiFragment(), "Visi");
        adapter.addFragment(new MisiFragment(), "Misi");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
