package com.meivaldi.trencenter.activity.caleg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.activity.LoginActivity;
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.app.AppController;
import com.meivaldi.trencenter.app.Config;
import com.meivaldi.trencenter.fragment.AccountFragment;
import com.meivaldi.trencenter.fragment.FragmentHomeCaleg;
import com.meivaldi.trencenter.fragment.HomeFragment;
import com.meivaldi.trencenter.fragment.MessageFragment;
import com.meivaldi.trencenter.helper.SQLiteHandler;
import com.meivaldi.trencenter.helper.SessionManager;
import com.meivaldi.trencenter.util.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Caleg extends AppCompatActivity {

    private SessionManager session;
    private SQLiteHandler db;
    private Toolbar toolbar;
    private BottomNavigationView navigation;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private static String TAG = Caleg.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caleg);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new FragmentHomeCaleg());

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
        FirebaseMessaging.getInstance().subscribeToTopic("berita");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

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

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("FIREBASE", "Firebase reg id: " + regId);

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
        } else if(id == R.id.action_refresh){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        Intent intent = new Intent(Caleg.this, LoginActivity.class);
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
                    fragment = new FragmentHomeCaleg();
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
