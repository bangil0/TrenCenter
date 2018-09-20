package com.meivaldi.trencenter.activity.pendukung;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.activity.DetailPenghargaan;
import com.meivaldi.trencenter.activity.DetailPlatform;
import com.meivaldi.trencenter.activity.DetailVisiMisi;
import com.meivaldi.trencenter.activity.LoginActivity;
import com.meivaldi.trencenter.activity.ProgramKerja;
import com.meivaldi.trencenter.activity.ScanKartu;
import com.meivaldi.trencenter.activity.caleg.DataCaleg;
import com.meivaldi.trencenter.activity.caleg.DetailCaleg;
import com.meivaldi.trencenter.activity.relawan.MainActivity;
import com.meivaldi.trencenter.activity.tim_pemenangan.ProgramKerja_TimPemenangan;
import com.meivaldi.trencenter.activity.tim_pemenangan.Tim_Pemenangan;
import com.meivaldi.trencenter.fragment.FragmentHomePendukung;
import com.meivaldi.trencenter.fragment.FragmentHomeRelawan;
import com.meivaldi.trencenter.fragment.ProfileRelawan;
import com.meivaldi.trencenter.helper.SQLiteHandler;
import com.meivaldi.trencenter.helper.SessionManager;

public class Pendukung extends AppCompatActivity {

    private Toolbar toolbar;

    private SQLiteHandler db;
    private SessionManager session;
    private BottomNavigationView navigation;


    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendukung);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new FragmentHomePendukung());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), DetailCaleg.class));

                        return true;
                    case R.id.nav_target:
                        startActivity(new Intent(getApplicationContext(), DetailVisiMisi.class));

                        return true;
                    case R.id.nav_platform:
                        startActivity(new Intent(getApplicationContext(), DetailPlatform.class));

                        return true;
                    case R.id.nav_progja:
                        startActivity(new Intent(getApplicationContext(), ProgramKerja.class));

                        return true;
                    case R.id.nav_penghargaan:
                        startActivity(new Intent(getApplicationContext(), DetailPenghargaan.class));

                        return true;
                    case R.id.nav_call:
                        ActivityCompat.requestPermissions(Pendukung.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                0);

                        return true;
                    case R.id.nav_scan:

                        startActivity(new Intent(getApplicationContext(), ScanKartu.class));

                        return true;
                    default:
                        return false;
                }
            }
        });

        db = new SQLiteHandler(getApplicationContext());

        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home_relawan:
                    fragment = new FragmentHomePendukung();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile_relawan:
                    fragment = new ProfileRelawan();
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("PERMISSION", "Permission Granted");

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:085761806490"));

                    startActivity(callIntent);
                } else {

                }
                return;
            }

        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_logout){
            logoutUser();
            return true;
        }

        if(toggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        Intent intent = new Intent(Pendukung.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawers();
            return;
        }

        MenuItem homeItem = navigation.getMenu().getItem(0);

        if (navigation.getSelectedItemId() != homeItem.getItemId()) {
            navigation.setSelectedItemId(homeItem.getItemId());
        } else {
            super.onBackPressed();
        }
    }
}
