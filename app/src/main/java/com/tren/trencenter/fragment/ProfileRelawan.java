package com.tren.trencenter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tren.trencenter.activity.ProfilePicture;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tren.trencenter.R;
import com.tren.trencenter.activity.ChangePassword;
import com.tren.trencenter.activity.ChangeUsername;
import com.tren.trencenter.helper.CircleTransform;
import com.tren.trencenter.helper.SQLiteHandler;

import java.util.HashMap;

public class ProfileRelawan extends Fragment implements View.OnClickListener {

    private RelativeLayout userPhoto, userName, userPassword;
    private TextView name, status;
    private ImageView fotoProfil;

    private SQLiteHandler db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_relawan, container, false);

        rootView.setAlpha(0f);
        rootView.setVisibility(View.GONE);

        db = new SQLiteHandler(getContext());
        HashMap<String, String> user = db.getUserDetails();

        String nama = user.get("name");
        String type = user.get("type");
        String foto = user.get("foto");

        name = (TextView) rootView.findViewById(R.id.nama);
        status = (TextView) rootView.findViewById(R.id.status);
        fotoProfil = (ImageView) rootView.findViewById(R.id.fotoProfil);

        name.setText(nama);
        status.setText(type);

        Glide.with(getContext()).load(foto)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(fotoProfil);

        userPhoto = (RelativeLayout) rootView.findViewById(R.id.foto);
        userName = (RelativeLayout) rootView.findViewById(R.id.username_settings);
        userPassword = (RelativeLayout) rootView.findViewById(R.id.password_settings);

        userPhoto.setOnClickListener(this);
        userName.setOnClickListener(this);
        userPassword.setOnClickListener(this);

        rootView.setVisibility(View.VISIBLE);
        rootView.animate()
                .alpha(1f)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .setListener(null);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.foto:
                startActivity(new Intent(getContext(), ProfilePicture.class));

                return;
            case R.id.username_settings:
                startActivity(new Intent(getContext(), ChangeUsername.class));

                return;
            case R.id.password_settings:
                startActivity(new Intent(getContext(), ChangePassword.class));

                return;
        }
    }

}
