package com.meivaldi.trencenter.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.meivaldi.trencenter.activity.tim_pemenangan.DetailProgram_TimPemenangan;
import com.meivaldi.trencenter.activity.tim_pemenangan.ProgramKerja_TimPemenangan;
import com.meivaldi.trencenter.activity.tim_pemenangan.Tim_Pemenangan;
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.app.AppController;
import com.meivaldi.trencenter.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by root on 09/09/18.
 */

public class ScanQR extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private String id, tipe;
    private SQLiteHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(ScanQR.this,
                new String[]{Manifest.permission.CAMERA},
                1);

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        tipe = user.get("type");
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

        finish();
    }

    @Override
    public void handleResult(Result result) {
        Log.v("TAG", result.getText());
        Log.v("TAG", result.getBarcodeFormat().toString());

        String nik = result.getText();
        id = getIntent().getStringExtra("ID");

        updateStatus(nik, id);

        mScannerView.resumeCameraPreview(this);
    }

    private void updateStatus(final String nik, final String id) {
        String tag_string_req = "req_update_program";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_VERIFIED, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Update Program", "Update Response: " + response.toString());

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    String msg = jsonObject.getString("error_msg");

                    if(!error){
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getApplicationContext(), DetailProgram_TimPemenangan.class));
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Update Program", "Update Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Tidak ada koneksi internet", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nik", nik);
                params.put("id", id);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(tipe.equals("super_admin")){
            Intent intent = new Intent(getApplicationContext(),
                    Dashboard_SuperAdmin.class);
            startActivity(intent);
        } else if(tipe.equals("relawan")){
            Intent intent = new Intent(getApplicationContext(),
                    MainActivity.class);
            startActivity(intent);
        } else if(tipe.equals("pendukung")){
            Intent intent = new Intent(getApplicationContext(),
                    Pendukung.class);
            startActivity(intent);
        } else if(tipe.equals("tim_pemenangan")) {
            Intent intent = new Intent(getApplicationContext(),
                    Tim_Pemenangan.class);
            startActivity(intent);
        }
    }
}
