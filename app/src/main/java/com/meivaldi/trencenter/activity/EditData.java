package com.meivaldi.trencenter.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.activity.tim_pemenangan.InputTimPemenangan;
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditData extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner status, jenisKelamin;
    private EditText nama, tempat_lahir, tanggal_lahir, umur, suku, hp,
            alamat, facebook, instagram, agama;
    private Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Data Caleg");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DetailCalegAdmin.class));
            }
        });

        edit = (Button) findViewById(R.id.editData);

        status = (Spinner) findViewById(R.id.status);
        jenisKelamin = (Spinner) findViewById(R.id.jenisKelamin);
        nama = (EditText) findViewById(R.id.nama);
        tempat_lahir = (EditText) findViewById(R.id.tempatLahir);
        tanggal_lahir = (EditText) findViewById(R.id.tanggalLahir);
        umur = (EditText) findViewById(R.id.umur);
        suku = (EditText) findViewById(R.id.suku);
        hp = (EditText) findViewById(R.id.nomorHP);
        alamat = (EditText) findViewById(R.id.alamat);
        facebook = (EditText) findViewById(R.id.facebook);
        instagram = (EditText) findViewById(R.id.instagram);
        agama = (EditText) findViewById(R.id.agama);

        String[] JenisKelamin = { "Pria", "Wanita" };
        String[] Status = { "Belum Menikah", "Menikah", "Pisah"};

        ArrayAdapter<String> jenisKelaminAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, JenisKelamin);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, Status);

        status.setAdapter(statusAdapter);
        jenisKelamin.setAdapter(jenisKelaminAdapter);

        Calendar calendar = Calendar.getInstance();

        final DatePickerDialog datePickerDialog = new DatePickerDialog(EditData.this, new DatePickerDialog.OnDateSetListener() {

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

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nama.getText().toString();
                String birthPlace = tempat_lahir.getText().toString();
                String birthDate = tanggal_lahir.getText().toString();
                String age = umur.getText().toString();
                String gender = jenisKelamin.getSelectedItem().toString();
                String tribe = suku.getText().toString();
                String phone = hp.getText().toString();
                String address = alamat.getText().toString();
                String Agama = agama.getText().toString();
                String fbAkun = facebook.getText().toString();
                String igAkun = instagram.getText().toString();

                String mr = status.getSelectedItem().toString();
                String marriage = "B";

                if(mr.equals("Belum Menikah")){
                    marriage = "B";
                } else if(mr.equals("Menikah")){
                    marriage = "S";
                } else if(mr.equals("Pisah")){
                    marriage = "P";
                }

                boolean status = checkEmptiness(name, birthPlace, birthDate, age, tribe, phone, address);

                if(status){
                    editData(name, birthPlace, birthDate, age, tribe, phone, address, gender, marriage,
                            fbAkun, igAkun,Agama);
                } else {
                    Toast.makeText(getApplicationContext(), "Tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
                }

                emptyField();
            }
        });
    }

    private void editData(final String name, final String birthPlace, final String birthDate, final String age, final String tribe,
                          final String phone, final String address, final String gender, final String marriage, final String fbAkun,
                          final String igAkun, final String agama) {
        String tag_string_req = "req_update_caleg";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE_CALEG, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Update Caleg", "Mengupdate data caleg");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("message");

                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Update Caleg", "Update Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Gagal mengupdate data!", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama", name);
                params.put("tempat_lahir", birthPlace);
                params.put("tanggal_lahir", birthDate);
                params.put("umur", age);
                params.put("status", marriage);
                params.put("jenis_kelamin", gender);
                params.put("suku", tribe);
                params.put("hp", phone);
                params.put("alamat", address);
                params.put("facebook", fbAkun);
                params.put("instagram", igAkun);
                params.put("agama", agama);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private boolean checkEmptiness(String name, String birthPlace, String birthDate, String age, String tribe, String phone, String address) {
        Drawable error = getApplicationContext().getResources().getDrawable(R.drawable.ic_error);
        Drawable success = getApplicationContext().getResources().getDrawable(R.drawable.ic_success);

        error.setBounds(0, 0, 50, 50);
        success.setBounds(0, 0, 50, 50);

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

        return true;
    }

    private void emptyField() {
        nama.setText("");
        tempat_lahir.setText("");
        umur.setText("");
        suku.setText("");
        hp.setText("");;
        alamat.setText("");
        agama.setText("");
        facebook.setText("");
        instagram.setText("");
    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}
