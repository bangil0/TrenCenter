package com.meivaldi.trencenter.activity.relawan;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.activity.DetailPenghargaan;
import com.meivaldi.trencenter.activity.DetailPlatform;
import com.meivaldi.trencenter.activity.DetailVisiMisi;
import com.meivaldi.trencenter.activity.LoginActivity;
import com.meivaldi.trencenter.activity.ScanKartu;
import com.meivaldi.trencenter.activity.caleg.DataCaleg;
import com.meivaldi.trencenter.activity.pendukung.InputPendukung;
import com.meivaldi.trencenter.activity.super_admin.Dashboard_SuperAdmin;
import com.meivaldi.trencenter.activity.tim_pemenangan.ProgramKerja_TimPemenangan;
import com.meivaldi.trencenter.activity.tim_pemenangan.Tim_Pemenangan;
import com.meivaldi.trencenter.app.Config;
import com.meivaldi.trencenter.fragment.FragmentHomePendukung;
import com.meivaldi.trencenter.fragment.FragmentHomeRelawan;
import com.meivaldi.trencenter.fragment.MessageFragment;
import com.meivaldi.trencenter.fragment.ProfileRelawan;
import com.meivaldi.trencenter.helper.SQLiteHandler;
import com.meivaldi.trencenter.helper.SessionManager;
import com.meivaldi.trencenter.model.Message;
import com.meivaldi.trencenter.util.NotificationUtils;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar toolbar;

    private SQLiteHandler db;
    private SessionManager session;
    private BottomNavigationView navigation;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new FragmentHomeRelawan());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                }
            }
        };


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
                        startActivity(new Intent(getApplicationContext(), DataCaleg.class));

                        return true;
                    case R.id.nav_target:
                        startActivity(new Intent(getApplicationContext(), DetailVisiMisi.class));

                        return true;
                    case R.id.nav_platform:
                        startActivity(new Intent(getApplicationContext(), DetailPlatform.class));

                        return true;
                    case R.id.nav_progja:
                        startActivity(new Intent(getApplicationContext(), ProgramKerja_TimPemenangan.class));

                        return true;
                    case R.id.nav_penghargaan:
                        startActivity(new Intent(getApplicationContext(), DetailPenghargaan.class));

                        return true;
                    case R.id.nav_call:
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:085761806490"));

                        if(ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                            return false;
                        }

                        startActivity(callIntent);

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

        displayFirebaseRegId();
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new FragmentHomeRelawan();
                    loadFragment(fragment);

                    return true;
                case R.id.navigation_message:
                    fragment = new MessageFragment();
                    loadFragment(fragment);

                    return true;
                case R.id.navigation_profile:
                    fragment = new ProfileRelawan();
                    loadFragment(fragment);

                    return true;
            }

            return false;
        }
    };

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

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        ActivityCompat.finishAffinity(MainActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
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
