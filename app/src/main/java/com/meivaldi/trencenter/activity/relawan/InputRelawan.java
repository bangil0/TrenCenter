package com.meivaldi.trencenter.activity.relawan;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.activity.super_admin.Dashboard_SuperAdmin;
import com.meivaldi.trencenter.activity.tim_pemenangan.Tim_Pemenangan;
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.app.AppController;
import com.meivaldi.trencenter.helper.CircleTransform;
import com.meivaldi.trencenter.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class InputRelawan extends AppCompatActivity {

    private Spinner status, jenisKelamin, kabupatenSP, kecamatanSP, kelurahanSP;
    private EditText KK, NIK, nama, tempat_lahir, tanggal_lahir, umur, suku, hp,
        alamat, rt, rw, tps;
    private Button input, upload;
    private Toolbar toolbar;
    private Calendar calendar;
    private ImageView profilePicture;
    private RelativeLayout container;
    private Bitmap imageBitmap;

    private SQLiteHandler db;
    private String tipe;
    private  HashMap<String, String> user;

    Dialog dialog;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_relawan);

        container = (RelativeLayout) findViewById(R.id.container);

        profilePicture = (ImageView) findViewById(R.id.profilePicture);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        input = (Button) findViewById(R.id.inputRelawan);
        upload = (Button) findViewById(R.id.uploadPhoto);

        status = (Spinner) findViewById(R.id.status);
        jenisKelamin = (Spinner) findViewById(R.id.jenisKelamin);
        kabupatenSP = (Spinner) findViewById(R.id.kabupaten);
        kecamatanSP = (Spinner) findViewById(R.id.kecamatan);
        kelurahanSP = (Spinner) findViewById(R.id.kelurahan);

        kecamatanSP.setClickable(false);
        kelurahanSP.setClickable(false);

        KK = (EditText) findViewById(R.id.kk);
        NIK = (EditText) findViewById(R.id.nik);
        nama = (EditText) findViewById(R.id.namaRelawan);
        tempat_lahir = (EditText) findViewById(R.id.tempatLahirRelawan);
        tanggal_lahir = (EditText) findViewById(R.id.tanggalLahirRelawan);
        umur = (EditText) findViewById(R.id.umurRelawan);
        suku = (EditText) findViewById(R.id.suku);
        hp = (EditText) findViewById(R.id.nomorHPRelawan);
        alamat = (EditText) findViewById(R.id.alamatRelawan);
        rt = (EditText) findViewById(R.id.rt);
        rw = (EditText) findViewById(R.id.rw);
        tps = (EditText) findViewById(R.id.tps);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        db = new SQLiteHandler(getApplicationContext());
        user = db.getUserDetails();
        tipe = user.get("type");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tipe.equals("relawan")){
                    Intent intent = new Intent(InputRelawan.this,
                            Dashboard_SuperAdmin.class);
                    startActivity(intent);
                } else if(tipe.equals("Relawan")){
                    Intent intent = new Intent(InputRelawan.this,
                            MainActivity.class);
                    startActivity(intent);
                } else if(tipe.equals("tim_pemenangan")){
                    Intent intent = new Intent(InputRelawan.this,
                            Tim_Pemenangan.class);
                    startActivity(intent);
                }
            }
        });

        String[] JenisKelamin = { "Pria", "Wanita" };
        String[] Status = { "Belum Menikah", "Menikah"};

        getKabupaten();

        ArrayAdapter<String> jenisKelaminAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, JenisKelamin);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, Status);

        kabupatenSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String kabs = adapterView.getSelectedItem().toString();
                getKecamatan(kabs);
                kecamatanSP.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        kecamatanSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String kec = adapterView.getSelectedItem().toString();
                getKelurahan(kec);
                kelurahanSP.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
                Bitmap bitmap = ((BitmapDrawable) profilePicture.getDrawable()).getBitmap();
                String foto = toBase64(bitmap);

                String kk = KK.getText().toString();
                String nik = NIK.getText().toString();
                String name = nama.getText().toString();
                String birthPlace = tempat_lahir.getText().toString();
                String birthDate = tanggal_lahir.getText().toString();
                String age = umur.getText().toString();
                String tribe = suku.getText().toString();
                String phone = hp.getText().toString();
                String address = alamat.getText().toString();
                String region = kabupatenSP.getSelectedItem().toString();
                String kec = kecamatanSP.getSelectedItem().toString();
                String kel = kelurahanSP.getSelectedItem().toString();
                String erwe = rw.getText().toString();
                String erte = rt.getText().toString();
                String tepees = tps.getText().toString();
                String gender = jenisKelamin.getSelectedItem().toString();
                String marriage = status.getSelectedItem().toString();
                String maker = user.get("name");

                checkEmptiness(kk, nik, name, birthPlace, birthDate, age, tribe, phone, address,
                        region, kec, kel, erwe, erte, tepees);

                addRelawan(kk, nik, name, birthPlace, birthDate, age, tribe, phone, address,
                        region, kec, kel, erwe, erte, tepees, gender, marriage, maker, foto);

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
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, 0);
                        }
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

    private void getKelurahan(final String kec) {
        String tag_string_req = "req_get_kabupaten";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_KELURAHAN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Ambil Data", "Berhasil mengambil data");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray kelurahanArray = jsonObject.getJSONArray("kelurahan");

                    String[] test = new String[kelurahanArray.length()];

                    for(int i=0; i<kelurahanArray.length(); i++){
                        test[i] = kelurahanArray.getJSONArray(i).getString(2);
                        Log.d("Kelurahan", test[i]);
                    }

                    ArrayAdapter<String> kelurahanAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, test);

                    kelurahanSP.setAdapter(kelurahanAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Tambah Relawan", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Berhasil menambah data", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("kec", kec);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getKecamatan(final String kabs) {
        String tag_string_req = "req_get_kabupaten";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_KECAMATAN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Ambil Data", "Berhasil mengambil data");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray kecamatanArray = jsonObject.getJSONArray("kecamatan");

                    String[] test = new String[kecamatanArray.length()];

                    for(int i=0; i<kecamatanArray.length(); i++){
                        test[i] = kecamatanArray.getJSONArray(i).getString(2);
                        Log.d("Kecamatan", test[i]);
                    }

                    ArrayAdapter<String> kecamatanAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, test);

                    kecamatanSP.setAdapter(kecamatanAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Tambah Relawan", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Berhasil menambah data", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("kab", kabs);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getKabupaten() {
        String tag_string_req = "req_get_kabupaten";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_GET_KABUPATEN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Ambil Data", "Berhasil mengambil data");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray kabupatenArray = jsonObject.getJSONArray("kabupaten");

                    String[] test = new String[kabupatenArray.length()];

                    for(int i=0; i<kabupatenArray.length(); i++){
                        test[i] = kabupatenArray.getJSONArray(i).getString(2);
                    }

                    ArrayAdapter<String> kabupatenAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, test);

                    kabupatenSP.setAdapter(kabupatenAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Tambah Relawan", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Berhasil menambah data", Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            profilePicture.setImageBitmap(imageBitmap);

            container.setBackground(null);

            dialog.dismiss();
        } else if(requestCode == 1 && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();

            Glide.with(this).load(selectedImage)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profilePicture);
            container.setBackground(null);

            dialog.dismiss();
        }
    }

    private void addRelawan(final String kk, final String nik, final String name, final String birthPlace, final String birthDate,
                            final String age, final String tribe, final String phone, final String address, final String region,
                            final String kec, final String kel, final String erwe, final String erte, final String tepees,
                            final String gender, final String marriage, final String maker, final String foto) {
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
                params.put("foto", foto);
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
                params.put("dibuat_oleh", maker);

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
