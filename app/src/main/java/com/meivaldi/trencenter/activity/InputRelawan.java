package com.meivaldi.trencenter.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class InputRelawan extends AppCompatActivity {

    private Spinner status, jenisKelamin;
    private EditText KK, NIK, nama, tempat_lahir, tanggal_lahir, umur, suku, hp,
        alamat, kabupaten, kecamatan, kelurahan, rt, rw, tps;
    private Button input, upload;
    private Toolbar toolbar;
    private Calendar calendar;
    private ImageView profilePicture;

    Dialog dialog;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_relawan);

        profilePicture = (ImageView) findViewById(R.id.profilePicture);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        input = (Button) findViewById(R.id.inputRelawan);
        upload = (Button) findViewById(R.id.uploadPhoto);

        status = (Spinner) findViewById(R.id.status);
        jenisKelamin = (Spinner) findViewById(R.id.jenisKelamin);

        KK = (EditText) findViewById(R.id.kk);
        NIK = (EditText) findViewById(R.id.nik);
        nama = (EditText) findViewById(R.id.namaRelawan);
        tempat_lahir = (EditText) findViewById(R.id.tempatLahirRelawan);
        tanggal_lahir = (EditText) findViewById(R.id.tanggalLahirRelawan);
        umur = (EditText) findViewById(R.id.umurRelawan);
        suku = (EditText) findViewById(R.id.suku);
        hp = (EditText) findViewById(R.id.nomorHPRelawan);
        alamat = (EditText) findViewById(R.id.alamatRelawan);
        kabupaten = (EditText) findViewById(R.id.kabupaten);
        kecamatan = (EditText) findViewById(R.id.kecamatan);
        kelurahan = (EditText) findViewById(R.id.kelurahan);
        rt = (EditText) findViewById(R.id.rt);
        rw = (EditText) findViewById(R.id.rw);
        tps = (EditText) findViewById(R.id.tps);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Dashboard_SuperAdmin.class));
                finish();
            }
        });

        String[] JenisKelamin = { "Pria", "Wanita" };
        String[] Status = { "Belum Menikah", "Menikah"};

        ArrayAdapter<String> jenisKelaminAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, JenisKelamin);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, Status);

        status.setAdapter(statusAdapter);
        jenisKelamin.setAdapter(jenisKelaminAdapter);

        calendar = Calendar.getInstance();

        final DatePickerDialog datePickerDialog = new DatePickerDialog(InputRelawan.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                tanggal_lahir.setText(dateFormatter.format(newDate.getTime()));
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        tanggal_lahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String kk = KK.getText().toString();
                String nik = NIK.getText().toString();
                String name = nama.getText().toString();
                String birthPlace = tempat_lahir.getText().toString();
                String birthDate = tanggal_lahir.getText().toString();
                String age = umur.getText().toString();
                String tribe = suku.getText().toString();
                String phone = hp.getText().toString();
                String address = alamat.getText().toString();
                String region = kabupaten.getText().toString();
                String kec = kecamatan.getText().toString();
                String kel = kelurahan.getText().toString();
                String erwe = rw.getText().toString();
                String erte = rt.getText().toString();
                String tepees = tps.getText().toString();
                String gender = jenisKelamin.getSelectedItem().toString();
                String marriage = status.getSelectedItem().toString();

                checkEmptiness(kk, nik, name, birthPlace, birthDate, age, tribe, phone, address,
                        region, kec, kel, erwe, erte, tepees);

                addRelawan(kk, nik, name, birthPlace, birthDate, age, tribe, phone, address,
                        region, kec, kel, erwe, erte, tepees, gender, marriage);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.setCancelable(true);

                TextView camera = (TextView) dialog.findViewById(R.id.camera);
                TextView gallery = (TextView) dialog.findViewById(R.id.gallery);

                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, 0);
                    }
                });

                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);
                    }
                });

                dialog.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 0:
                Uri cameraCapture = data.getData();
                profilePicture.setImageURI(cameraCapture);
                dialog.dismiss();

                return;
            case 1:
                Uri selectedImage = data.getData();
                profilePicture.setImageURI(selectedImage);
                dialog.dismiss();

                return;
        }
    }

    private void addRelawan(final String kk, final String nik, final String name, final String birthPlace, final String birthDate,
                            final String age, final String tribe, final String phone, final String address, final String region,
                            final String kec, final String kel, final String erwe, final String erte, final String tepees,
                            final String gender, final String marriage) {
        String tag_string_req = "req_add_relawan";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADD_RELAWAN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Tambah Relawan", "Berhasil menambah relawan");

                try{
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    String msg = jObj.getString("error_msg");

                    if(!error){
                        Toast.makeText(getApplicationContext(), "" + jObj, Toast.LENGTH_SHORT).show();
                        Log.d("JSON", "" + jObj);
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Tambah Relawan", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Berhasil menambah data", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("foto", "image.jpg");
                params.put("kk", kk);
                params.put("nik", nik);
                params.put("nama", name);
                params.put("tempat_lahir", birthPlace);
                params.put("tanggal_lahir", birthDate);
                params.put("umur", age);
                params.put("status", marriage);
                params.put("jenis_kelamin", gender);
                params.put("suku", tribe);
                params.put("hp", phone);
                params.put("alamat", address);
                params.put("kabupaten", region);
                params.put("kecamatan", kec);
                params.put("kelurahan", kel);
                params.put("rt", erte);
                params.put("rw", erwe);
                params.put("tps", tepees);
                params.put("dibuat_oleh", "admin");

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void checkEmptiness(String kk, String nik, String name, String birthPlace, String birthDate, String age, String tribe, String phone, String address, String region, String kec, String kel, String erwe, String erte, String tepees) {
        Drawable error = getApplicationContext().getResources().getDrawable(R.drawable.ic_error);
        Drawable success = getApplicationContext().getResources().getDrawable(R.drawable.ic_success);

        error.setBounds(0, 0, 50, 50);
        success.setBounds(0, 0, 50, 50);

        if(kk.isEmpty()){
            KK.setCompoundDrawables(null, null, error, null);
            KK.setHint("KK tidak boleh kosong!");
        } else {
            KK.setCompoundDrawables(null, null, success, null);
        }

        if(nik.isEmpty()){
            NIK.setCompoundDrawables(null, null, error, null);
            NIK.setHint("NIK tidak boleh kosong!");
        } else {
            NIK.setCompoundDrawables(null, null, success, null);
        }

        if(name.isEmpty()){
            nama.setCompoundDrawables(null, null, error, null);
            nama.setHint("Nama tidak boleh kosong!");
        } else {
            nama.setCompoundDrawables(null, null, success, null);
        }

        if(birthPlace.isEmpty()){
            tempat_lahir.setCompoundDrawables(null, null, error, null);
            tempat_lahir.setHint("Tempat Lahir tidak boleh kosong!");
        } else {
            tempat_lahir.setCompoundDrawables(null, null, success, null);
        }

        if(birthDate.isEmpty()){
            tanggal_lahir.setCompoundDrawables(null, null, error, null);
            tanggal_lahir.setHint("Tanggal Lahir tidak boleh kosong!");
        } else {
            tanggal_lahir.setCompoundDrawables(null, null, success, null);
        }

        if(age.isEmpty()){
            umur.setCompoundDrawables(null, null, error, null);
            umur.setHint("Umur tidak boleh kosong!");
        } else {
            umur.setCompoundDrawables(null, null, success, null);
        }

        if(tribe.isEmpty()){
            suku.setCompoundDrawables(null, null, error, null);
            suku.setHint("Suku tidak boleh kosong!");
        } else {
            suku.setCompoundDrawables(null, null, success, null);
        }

        if(phone.isEmpty()){
            hp.setCompoundDrawables(null, null, error, null);
            hp.setHint("Nomor HP tidak boleh kosong");
        } else {
            hp.setCompoundDrawables(null, null, success, null);
        }

        if(address.isEmpty()){
            alamat.setCompoundDrawables(null, null, error, null);
            alamat.setHint("Alamat tidak boleh kosong!");
        } else {
            alamat.setCompoundDrawables(null, null, success, null);
        }

        if(region.isEmpty()){
            kabupaten.setCompoundDrawables(null, null, error, null);
            kabupaten.setHint("Kabupaten tidak boleh kosong!");
        } else {
            kabupaten.setCompoundDrawables(null, null, success, null);
        }

        if(kec.isEmpty()){
            kecamatan.setCompoundDrawables(null, null, error, null);
            kecamatan.setHint("Kecamatan tidak boleh kosong!");
        } else {
            kecamatan.setCompoundDrawables(null, null, success, null);
        }

        if(kel.isEmpty()){
            kelurahan.setCompoundDrawables(null, null, error, null);
            kelurahan.setHint("Kelurahan tidak boleh kosong!");
        } else {
            kelurahan.setCompoundDrawables(null, null, success, null);
        }

        if(erte.isEmpty()){
            rt.setCompoundDrawables(null, null, error, null);
            rt.setHint("RT tidak boleh kosong!");
        } else {
            rt.setCompoundDrawables(null, null, success, null);
        }

        if(erwe.isEmpty()){
            rw.setCompoundDrawables(null, null, error, null);
            rw.setHint("RW tidak boleh kosong!");
        } else {
            rw.setCompoundDrawables(null, null, success, null);
        }

        if(tepees.isEmpty()){
            tps.setCompoundDrawables(null, null, error, null);
            tps.setHint("TPS tidak boleh kosong!");
        } else {
            tps.setCompoundDrawables(null, null, success, null);
        }

        return;
    }
}
