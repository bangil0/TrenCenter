package com.tren.trencenter.activity.super_admin;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tren.trencenter.activity.ChangePhone;
import com.tren.trencenter.activity.DetailPenghargaanAdmin;
import com.tren.trencenter.activity.Partnership;
import com.tren.trencenter.activity.tim_pemenangan.ProgramKerja_TimPemenangan;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tren.trencenter.R;
import com.tren.trencenter.activity.DetailCalegAdmin;
import com.tren.trencenter.activity.DetailPlatformAdmin;
import com.tren.trencenter.activity.LayananActivity;
import com.tren.trencenter.activity.LoginActivity;
import com.tren.trencenter.activity.ScanKartu;
import com.tren.trencenter.activity.VisiMisiAdmin;
import com.tren.trencenter.app.AppConfig;
import com.tren.trencenter.app.AppController;
import com.tren.trencenter.app.Config;
import com.tren.trencenter.fragment.AccountFragment;
import com.tren.trencenter.fragment.HomeFragment;
import com.tren.trencenter.fragment.MessageFragment;
import com.tren.trencenter.helper.SQLiteHandler;
import com.tren.trencenter.helper.SessionManager;
import com.tren.trencenter.util.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Dashboard_SuperAdmin extends AppCompatActivity {

    private SessionManager session;
    private SQLiteHandler db;
    private Toolbar toolbar;
    private BottomNavigationView navigation;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private static String TAG = Dashboard_SuperAdmin.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_super_admin);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new HomeFragment());

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
                        startActivity(new Intent(getApplicationContext(), DetailCalegAdmin.class));

                        return true;
                    case R.id.nav_target:
                        startActivity(new Intent(getApplicationContext(), VisiMisiAdmin.class));

                        return true;
                    case R.id.nav_platform:
                        startActivity(new Intent(getApplicationContext(), DetailPlatformAdmin.class));

                        return true;
                    case R.id.nav_progja:
                        startActivity(new Intent(getApplicationContext(), ProgramKerja_TimPemenangan.class));

                        return true;
                    case R.id.nav_layanan:
                        startActivity(new Intent(getApplicationContext(), LayananActivity.class));

                        return true;
                    case R.id.nav_partnership:
                        startActivity(new Intent(getApplicationContext(), Partnership. class));

                        return true;
                    case R.id.nav_penghargaan:
                        startActivity(new Intent(getApplicationContext(), DetailPenghargaanAdmin.class));

                        return true;
                    case R.id.nav_call:
                        ActivityCompat.requestPermissions(Dashboard_SuperAdmin.this,
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

        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
        FirebaseMessaging.getInstance().subscribeToTopic("berita");
        FirebaseMessaging.getInstance().subscribeToTopic("layanan");

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                }
            }
        };

        HashMap<String, String> details = db.getUserDetails();
        String id = details.get("id");
        String token = FirebaseInstanceId.getInstance().getToken();

        sendToken(id, token);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("PERMISSION", "Permission Granted");

                    makeCall();

                } else {

                }
                return;
            }

        }
    }

    private void makeCall() {
        String tag_string_req = "req_get_phone";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_PHONE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String phone = jObj.getString("hp");

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone));
                    startActivity(callIntent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Token Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void sendToken(final String id, final String token){
        String tag_string_req = "req_save_token";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SAVE_TOKEN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String msg = jObj.getString("error_msg");

                    Log.d("TOKEN", msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Token Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Tidak ada koneksi internet", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_user", id);
                params.put("token", token);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("FIREBASE", "Firebase reg id: " + regId);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_logout){
            logoutUser();
            return true;
        } else if(id == R.id.action_change_phone){
            startActivity(new Intent(getApplicationContext(), ChangePhone.class));
        }

        if(toggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        Intent intent = new Intent(Dashboard_SuperAdmin.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_message:
                    fragment = new MessageFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    fragment = new AccountFragment();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        FirebaseMessaging.getInstance().unsubscribeFromTopic("layanan");
    }
}