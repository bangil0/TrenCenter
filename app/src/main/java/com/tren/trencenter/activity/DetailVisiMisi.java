package com.tren.trencenter.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.tren.trencenter.R;
import com.tren.trencenter.fragment.MisiFragment;
import com.tren.trencenter.fragment.VisiFragment;
import com.tren.trencenter.helper.SQLiteHandler;
import com.tren.trencenter.helper.TabAdapter;
import java.util.HashMap;

public class DetailVisiMisi extends AppCompatActivity {

    private Toolbar toolbar;
    private SQLiteHandler db;
    private String tipe;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_visi_misi);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
