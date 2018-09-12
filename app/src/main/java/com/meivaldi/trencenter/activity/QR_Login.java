package com.meivaldi.trencenter.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.Result;
import com.meivaldi.trencenter.activity.pendukung.Pendukung;
import com.meivaldi.trencenter.activity.relawan.MainActivity;
import com.meivaldi.trencenter.activity.super_admin.Dashboard_SuperAdmin;
import com.meivaldi.trencenter.activity.tim_pemenangan.Tim_Pemenangan;
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.app.AppController;
import com.meivaldi.trencenter.helper.SQLiteHandler;
import com.meivaldi.trencenter.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QR_Login extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        ActivityCompat.requestPermissions(QR_Login.this,
                new String[]{Manifest.permission.CAMERA},
                1);

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Log.v("TAG", result.getText());
        Log.v("TAG", result.getBarcodeFormat().toString());

        String nik = result.getText();
        
        loginQR(nik);

        mScannerView.resumeCameraPreview(this);
    }

    private void loginQR(final String nik) {
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.QR_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        session.setLogin(true);

                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String username = user.getString("username");
                        String tipe = user.getString("tipe");
                        String created_at = user
                                .getString("created_at");

                        String foto;

                        if(tipe.equals("pendukung")){
                            foto = "http://103.28.53.181/~millenn1/dashboard/save/foto_pendukung/" + user.getString("foto");
                        } else if(tipe.equals("tim_pemenangan")){
                            foto = "http://103.28.53.181/~millenn1/dashboard/save/foto_pemenangan/" + user.getString("foto");
                        } else {
                            foto = "http://103.28.53.181/~millenn1/dashboard/save/foto/" + user.getString("foto");
                        }

                        if(tipe.equals("super_admin")){
                            Intent intent = new Intent(QR_Login.this,
                                    Dashboard_SuperAdmin.class);
                            startActivity(intent);
                        } else if(tipe.equals("relawan")){
                            Intent intent = new Intent(QR_Login.this,
                                    MainActivity.class);
                            startActivity(intent);
                        } else if(tipe.equals("pendukung")){
                            Intent intent = new Intent(QR_Login.this,
                                    Pendukung.class);
                            startActivity(intent);
                        } else if(tipe.equals("tim_pemenangan")) {
                            Intent intent = new Intent(QR_Login.this,
                                    Tim_Pemenangan.class);
                            startActivity(intent);
                        }

                        db.addUser(uid, name, username, foto, tipe, created_at);

                        finish();
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
                Log.e("TAG", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nik", nik);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    Toast.makeText(QR_Login.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

}
