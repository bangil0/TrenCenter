package com.bmc.trencenter.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bmc.trencenter.R;
import com.bmc.trencenter.fragment.MisiAdminFragment;
import com.bmc.trencenter.fragment.VisiAdminFragment;
import com.bmc.trencenter.helper.SQLiteHandler;
import com.bmc.trencenter.helper.TabAdapter;

import java.util.HashMap;

public class VisiMisiAdmin extends AppCompatActivity {

    private Toolbar toolbar;
    private SQLiteHandler db;
    private String tipe;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visi_misi_admin);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        tipe = user.get("type");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Visi Misi Caleg");
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new VisiAdminFragment(), "Visi");
        adapter.addFragment(new MisiAdminFragment(), "Misi");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

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
