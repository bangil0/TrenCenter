package com.meivaldi.trencenter.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.activity.ChangePassword;
import com.meivaldi.trencenter.activity.ChangeUsername;
import com.meivaldi.trencenter.helper.CircleTransform;
import com.meivaldi.trencenter.helper.SQLiteHandler;

import java.util.HashMap;

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
        status.setText(tipe);

        Glide.with(getContext()).load(foto)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(fotoProfil);

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
            case R.id.fotoProfil:
                Toast.makeText(getContext(), "Masih Dalam Pengembangan", Toast.LENGTH_SHORT).show();

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
