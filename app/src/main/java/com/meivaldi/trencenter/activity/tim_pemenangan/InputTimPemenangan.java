package com.meivaldi.trencenter.activity.tim_pemenangan;

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
import com.meivaldi.trencenter.activity.pendukung.Pendukung;
import com.meivaldi.trencenter.activity.relawan.MainActivity;
import com.meivaldi.trencenter.activity.super_admin.Dashboard_SuperAdmin;
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.app.AppController;
import com.meivaldi.trencenter.helper.CircleTransform;
import com.meivaldi.trencenter.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class InputTimPemenangan extends AppCompatActivity {

    private Toolbar toolbar;
    private SQLiteHandler db;
    private Spinner status, jenisKelamin, kabupaten, kecamatan, kelurahan;
    private EditText KK, NIK, nama, tempat_lahir, tanggal_lahir, umur, suku, hp,
            alamat, rt, rw, tps, username, facebook, instagram, agama;
    private Button input, upload;
    private Calendar calendar;
    private ImageView profilePicture;
    private RelativeLayout container;
    private Uri selectedImage;

    private static final int FROM_CAMERA = 100;
    private static final int FROM_GALLERY = 200;

    private int imageStatus;

    private Dialog dialog;
    private final Context context = this;

    private HashMap<String, String> user;
    private List<String> kabupatenList, kecamatanList, kelurahanList;

    private String kabInit = " -- KABUPATEN -- ";
    private String kecInit = " -- KECAMATAN -- ";
    private String kelInit = " -- KELURAHAN -- ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_tim_pemenangan);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        container = (RelativeLayout) findViewById(R.id.container);
        profilePicture = (ImageView) findViewById(R.id.profilePicture);

        input = (Button) findViewById(R.id.inputPendukung);
        upload = (Button) findViewById(R.id.uploadPhoto);

        status = (Spinner) findViewById(R.id.status);
        jenisKelamin = (Spinner) findViewById(R.id.jenisKelamin);
        kabupaten = (Spinner) findViewById(R.id.kabupaten);
        kecamatan = (Spinner) findViewById(R.id.kecamatan);
        kelurahan = (Spinner) findViewById(R.id.kelurahan);

        kabupatenList = new ArrayList<>();
        kecamatanList = new ArrayList<>();
        kelurahanList = new ArrayList<>();

        kabupatenList.add(kabInit);
        kecamatanList.add(kecInit);
        kelurahanList.add(kelInit);

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
        username = (EditText) findViewById(R.id.username);
        facebook = (EditText) findViewById(R.id.facebook);
        instagram = (EditText) findViewById(R.id.instagram);
        agama = (EditText) findViewById(R.id.agama);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tambah Tim Pemenangan");

        db = new SQLiteHandler(getApplicationContext());
        user = db.getUserDetails();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String[] JenisKelamin = { "Pria", "Wanita" };
        String[] Status = { "Belum Menikah", "Menikah", "Pisah"};

        ArrayAdapter<String> jenisKelaminAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, JenisKelamin);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, Status);

        status.setAdapter(statusAdapter);
        jenisKelamin.setAdapter(jenisKelaminAdapter);

        calendar = Calendar.getInstance();

        final DatePickerDialog datePickerDialog = new DatePickerDialog(InputTimPemenangan.this, new DatePickerDialog.OnDateSetListener() {

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

        getKabupaten();

        kabupaten.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kecamatanList.clear();
                String kabs = adapterView.getSelectedItem().toString();
                getKecamatan(kabs);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        kecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kelurahanList.clear();
                String kec = adapterView.getSelectedItem().toString();
                getKelurahan(kec);
                kelurahan.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = null;

                if(imageStatus == FROM_GALLERY){
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if(imageStatus == FROM_CAMERA){
                    bitmap = ((BitmapDrawable) profilePicture.getDrawable()).getBitmap();
                } else {
                    bitmap = ((BitmapDrawable) profilePicture.getDrawable()).getBitmap();
                }

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
                String region = kabupaten.getSelectedItem().toString();
                String kec = kecamatan.getSelectedItem().toString();
                String kel = kelurahan.getSelectedItem().toString();
                String erwe = rw.getText().toString();
                String erte = rt.getText().toString();
                String tepees = tps.getText().toString();
                String marriage = status.getSelectedItem().toString();
                String maker = user.get("type");
                String makerName = user.get("name");
                String Agama = agama.getText().toString();
                String userName = username.getText().toString();
                String fbAkun = facebook.getText().toString();
                String igAkun = instagram.getText().toString();

                String gn = jenisKelamin.getSelectedItem().toString();
                String gender = "B";

                if(gn.equals("Belum Menikah")){
                    gender = "B";
                } else if(gn.equals("Menikah")){
                    gender = "S";
                } else if(gn.equals("Pisah")){
                    gender = "P";
                }

                boolean status = checkEmptiness(kk, nik, name, birthPlace, birthDate, age, tribe, phone, address,
                        region, kec, kel, erwe, erte, tepees);

                if(status){
                    addPemenang(kk, nik, name, birthPlace, birthDate, age, tribe, phone, address,
                            region, kec, kel, erwe, erte, tepees, gender, marriage, maker, foto, userName,
                            fbAkun, igAkun, makerName, Agama);
                } else {
                    Toast.makeText(getApplicationContext(), "Tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
                }

                emptyField();
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
                ImageView galleryImg = (ImageView) dialog.findViewById(R.id.galleryLogo);
                ImageView cameraImg = (ImageView) dialog.findViewById(R.id.cameraLogo);

                cameraImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, 0);
                        }

                        dialog.dismiss();
                    }
                });

                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, 0);
                        }

                        dialog.dismiss();
                    }
                });

                galleryImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);

                        dialog.dismiss();
                    }
                });

                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    public String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
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

                    for(int i=0; i<kelurahanArray.length(); i++){
                        kelurahanList.add(kelurahanArray.getJSONArray(i).getString(2));
                    }

                    ArrayAdapter<String> kelurahanAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, kelurahanList);

                    kelurahan.setAdapter(kelurahanAdapter);

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

    private void emptyField() {
        KK.setText("");
        NIK.setText("");
        nama.setText("");
        tempat_lahir.setText("");
        umur.setText("");
        suku.setText("");
        hp.setText("");;
        alamat.setText("");
        rw.setText("");
        rt.setText("");
        tps.setText("");
        agama.setText("");
        username.setText("");
        facebook.setText("");
        instagram.setText("");
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

                    for(int i=0; i<kecamatanArray.length(); i++){
                        kecamatanList.add(kecamatanArray.getJSONArray(i).getString(2));
                    }

                    ArrayAdapter<String> kecamatanAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, kecamatanList);

                    kecamatan.setAdapter(kecamatanAdapter);

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

                    for(int i=0; i<kabupatenArray.length(); i++){
                        kabupatenList.add(kabupatenArray.getJSONArray(i).getString(2));
                    }

                    ArrayAdapter<String> kabupatenAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, kabupatenList);

                    kabupaten.setAdapter(kabupatenAdapter);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            imageStatus = FROM_CAMERA;
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profilePicture.setImageBitmap(imageBitmap);

            container.setBackground(null);

            dialog.dismiss();
        } else if(requestCode == 1 && resultCode == RESULT_OK){
            imageStatus = FROM_GALLERY;
            selectedImage = data.getData();

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

    private void addPemenang(final String kk, final String nik, final String name, final String birthPlace, final String birthDate,
                            final String age, final String tribe, final String phone, final String address, final String region,
                            final String kec, final String kel, final String erwe, final String erte, final String tepees,
                            final String gender, final String marriage, final String maker, final String foto, final String userName,
                            final String fbAkun, final String igAkun, final String makerName, final String agama) {
        String tag_string_req = "req_add_pemangan";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADD_PEMENANG, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Tambah Pemenang", "Berhasil menambah Pemenang");

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
                Log.e("Tambah Pemenang", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Berhasil menambah Pemenang", Toast.LENGTH_LONG).show();
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
                params.put("username", userName);
                params.put("facebook", fbAkun);
                params.put("instagram", igAkun);
                params.put("agama", agama);
                params.put("referensi", makerName);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private boolean checkEmptiness(String kk, String nik, String name, String birthPlace, String birthDate, String age, String tribe, String phone, String address, String region, String kec, String kel, String erwe, String erte, String tepees) {
        Drawable error = getApplicationContext().getResources().getDrawable(R.drawable.ic_error);
        Drawable success = getApplicationContext().getResources().getDrawable(R.drawable.ic_success);

        error.setBounds(0, 0, 50, 50);
        success.setBounds(0, 0, 50, 50);

        if(kk.isEmpty()){
            KK.setCompoundDrawables(null, null, error, null);
            KK.setHint("KK tidak boleh kosong!");

            return false;
        } else {
            KK.setCompoundDrawables(null, null, success, null);
        }

        if(nik.isEmpty()){
            NIK.setCompoundDrawables(null, null, error, null);
            NIK.setHint("NIK tidak boleh kosong!");

            return false;
        } else {
            NIK.setCompoundDrawables(null, null, success, null);
        }

        if(name.isEmpty()){
            nama.setCompoundDrawables(null, null, error, null);
            nama.setHint("Nama tidak boleh kosong!");

            return false;
        } else {
            nama.setCompoundDrawables(null, null, success, null);
        }

        if(birthPlace.isEmpty()){
            tempat_lahir.setCompoundDrawables(null, null, error, null);
            tempat_lahir.setHint("Tempat Lahir tidak boleh kosong!");

            return false;
        } else {
            tempat_lahir.setCompoundDrawables(null, null, success, null);
        }

        if(birthDate.isEmpty()){
            tanggal_lahir.setCompoundDrawables(null, null, error, null);
            tanggal_lahir.setHint("Tanggal Lahir tidak boleh kosong!");

            return false;
        } else {
            tanggal_lahir.setCompoundDrawables(null, null, success, null);
        }

        if(age.isEmpty()){
            umur.setCompoundDrawables(null, null, error, null);
            umur.setHint("Umur tidak boleh kosong!");

            return false;
        } else {
            umur.setCompoundDrawables(null, null, success, null);
        }

        if(tribe.isEmpty()){
            suku.setCompoundDrawables(null, null, error, null);
            suku.setHint("Suku tidak boleh kosong!");

            return false;
        } else {
            suku.setCompoundDrawables(null, null, success, null);
        }

        if(phone.isEmpty()){
            hp.setCompoundDrawables(null, null, error, null);
            hp.setHint("Nomor HP tidak boleh kosong");

            return false;
        } else {
            hp.setCompoundDrawables(null, null, success, null);
        }

        if(address.isEmpty()){
            alamat.setCompoundDrawables(null, null, error, null);
            alamat.setHint("Alamat tidak boleh kosong!");

            return false;
        } else {
            alamat.setCompoundDrawables(null, null, success, null);
        }

        if(erte.isEmpty()){
            rt.setCompoundDrawables(null, null, error, null);
            rt.setHint("RT tidak boleh kosong!");

            return false;
        } else {
            rt.setCompoundDrawables(null, null, success, null);
        }

        if(erwe.isEmpty()){
            rw.setCompoundDrawables(null, null, error, null);
            rw.setHint("RW tidak boleh kosong!");

            return false;
        } else {
            rw.setCompoundDrawables(null, null, success, null);
        }

        if(tepees.isEmpty()){
            tps.setCompoundDrawables(null, null, error, null);
            tps.setHint("TPS tidak boleh kosong!");

            return false;
        } else {
            tps.setCompoundDrawables(null, null, success, null);
        }

        if(kabupaten.getSelectedItem().toString().equals(kabInit)){
            Toast.makeText(getApplicationContext(), "Pilih Kabupaten!", Toast.LENGTH_SHORT).show();

            return false;
        }

        if(kecamatan.getSelectedItem().toString().equals(kecInit)){
            Toast.makeText(getApplicationContext(), "Pilih Kecamatan!", Toast.LENGTH_SHORT).show();

            return false;
        }

        if(kelurahan.getSelectedItem().toString().equals(kelInit)){
            Toast.makeText(getApplicationContext(), "Pilih Kelurahan!", Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
