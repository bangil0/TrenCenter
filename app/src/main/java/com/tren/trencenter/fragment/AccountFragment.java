package com.tren.trencenter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tren.trencenter.activity.ProfilePicture;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tren.trencenter.R;
import com.tren.trencenter.activity.ChangePassword;
import com.tren.trencenter.activity.ChangeUsername;
import com.tren.trencenter.app.AppConfig;
import com.tren.trencenter.app.AppController;
import com.tren.trencenter.helper.CircleTransform;
import com.tren.trencenter.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountFragment extends Fragment implements View.OnClickListener{

    private RelativeLayout userPhoto, userName, userPassword;

    private SQLiteHandler db;
    private TextView name, status;
    private ImageView fotoProfil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        rootView.setAlpha(0f);
        rootView.setVisibility(View.GONE);

        userPhoto = (RelativeLayout) rootView.findViewById(R.id.foto);
        userName = (RelativeLayout) rootView.findViewById(R.id.username_settings);
        userPassword = (RelativeLayout) rootView.findViewById(R.id.password_settings);

        name = (TextView) rootView.findViewById(R.id.nama);
        status = (TextView) rootView.findViewById(R.id.status);
        fotoProfil = (ImageView) rootView.findViewById(R.id.fotoProfil);

        db = new SQLiteHandler(getContext());
        HashMap<String, String> user = db.getUserDetails();

        String nama = user.get("name");
        String tipe = user.get("type");
        String foto = user.get("foto");

        name.setText(nama);

        if(tipe.equals("tim_pemenangan")){
            getStatus(tipe, nama);
        } else {
            status.setText(tipe);
        }

        Glide.with(getContext()).load(foto)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(fotoProfil);

        userName.setOnClickListener(this);
        userPassword.setOnClickListener(this);
        userPhoto.setOnClickListener(this);

        rootView.setVisibility(View.VISIBLE);
        rootView.animate()
                .alpha(1f)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .setListener(null);

        return rootView;
    }

    private void getStatus(final String tipe, final String nama) {
        String tag_string_req = "req_status";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_STATUS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("STATUS", "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String jabatan = jObj.getString("jabatan");
                        status.setText(jabatan + " " + tipe);
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("STATUS", "Login Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        "Tidak ada koneksi internet", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("type", tipe);
                params.put("nama", nama);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.foto:
                startActivity(new Intent(getContext(), ProfilePicture.class));

                return;
            case R.id.username_settings:
                startActivity(new Intent(getContext(), ChangeUsername.class));

                getFragmentManager().beginTransaction()
                        .add(R.id.frame_container, new AccountFragment())
                        .addToBackStack(ChangeUsername.class.getSimpleName())
                        .commit();
                return;
            case R.id.password_settings:
                startActivity(new Intent(getContext(), ChangePassword.class));
                return;
        }
    }

}
