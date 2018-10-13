package com.bmc.trencenter.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bmc.trencenter.R;
import com.bmc.trencenter.app.AppConfig;
import com.bmc.trencenter.app.AppController;
import com.bmc.trencenter.helper.CircleTransform;
import com.bmc.trencenter.helper.SQLiteHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfilePicture extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView foto;
    private Button update;
    private Uri selectedImage;

    private SQLiteHandler db;
    private HashMap<String, String> user;
    private Dialog dialog;

    private static final int FROM_CAMERA = 100;
    private static final int FROM_GALLERY = 200;
    private int imageStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);

        foto = (ImageView) findViewById(R.id.user_foto);
        update = (Button) findViewById(R.id.ubah_foto_profil);

        db = new SQLiteHandler(this);
        user = new HashMap<>();
        user = db.getUserDetails();

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(user.get("name"));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String uri = user.get("foto");
        Glide.with(this).load(uri)
                .bitmapTransform(new CircleTransform(this))
                .fitCenter()
                .into(foto);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(ProfilePicture.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            imageStatus = FROM_CAMERA;
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            foto.setImageBitmap(imageBitmap);

            dialog.dismiss();
        } else if(requestCode == 1 && resultCode == RESULT_OK){
            imageStatus = FROM_GALLERY;
            selectedImage = data.getData();

            Glide.with(this).load(selectedImage)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(false)
                    .into(foto);

            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String foto = toBase64(bitmap);
            String tipe = user.get("type");
            String name = user.get("name");

            uploadFoto(foto, tipe, name);

            dialog.dismiss();
        }
    }

    private void uploadFoto(final String foto, final String tipe, final String name) {
        String tag_string_req = "req_edit_foto";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_EDIT_FOTO, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Ganti Foto", "Berhasil mengganti foto");

                try{
                    JSONObject jObj = new JSONObject(response);
                    String msg = jObj.getString("message");

                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                } catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Ganti Foto", "Ganti Foto Error!");
                Toast.makeText(getApplicationContext(),
                        "Berhasil mengganti foto", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("foto", foto);
                params.put("tipe", tipe);
                params.put("nama", name);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @Override
    public void finish() {
        super.finish();
    }

}
