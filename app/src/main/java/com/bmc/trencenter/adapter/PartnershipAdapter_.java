package com.bmc.trencenter.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bmc.trencenter.model.Caleg;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 06/11/18.
 */
public class PartnershipAdapter_ extends RecyclerView.Adapter<PartnershipAdapter_.MyViewHolder> {

    private Context context;
    private List<Caleg> calegList;

    private SQLiteHandler db;
    private HashMap<String, String> user = new HashMap<>();
    private String pengirim;

    public PartnershipAdapter_(Context context, List<Caleg> calegList) {
        this.context = context;
        this.calegList = calegList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list, parent, false);

        db = new SQLiteHandler(context);
        user = db.getUserDetails();
        pengirim = user.get("username");

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Caleg caleg = calegList.get(position);
        holder.nama.setText(caleg.getNama());

        Glide.with(context).load(caleg.getImage())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.foto);

        holder.kirim.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return calegList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView foto;
        public TextView nama;
        public LinearLayout kirim;

        public MyViewHolder(View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.calegImage);
            nama = itemView.findViewById(R.id.namaCaleg);
            kirim = itemView.findViewById(R.id.panggil);
        }
    }

}