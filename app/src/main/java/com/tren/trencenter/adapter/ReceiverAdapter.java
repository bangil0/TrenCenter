package com.tren.trencenter.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tren.trencenter.R;
import com.tren.trencenter.app.AppConfig;
import com.tren.trencenter.app.AppController;
import com.tren.trencenter.helper.CircleTransform;
import com.tren.trencenter.model.Caleg;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 17/11/18.
 */

public class ReceiverAdapter extends RecyclerView.Adapter<ReceiverAdapter.MyViewHolder> {

    private Context context;
    private List<Caleg> receiverList;

    public ReceiverAdapter(Context context, List<Caleg> receiverList) {
        this.context = context;
        this.receiverList = receiverList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Caleg caleg = receiverList.get(position);
        holder.nama.setText(caleg.getNama());

        Glide.with(context).load(caleg.getImage())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.foto);
    }

    private void sendMessage(final String nama, final String pengirim) {
        String tag_string_req = "req_broadcast";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_BROADCAST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    String msg = jsonObject.getString("error_msg");


                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SCAN KARTU", "Error: " + error.getMessage());
                Toast.makeText(context,
                        "Terjadi kesalahan.", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("penerima", nama);
                params.put("pengirim", pengirim);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public int getItemCount() {
        return receiverList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView nama;
        private ImageView foto;

        public MyViewHolder(View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.namaCaleg);
            foto = itemView.findViewById(R.id.calegImage);
        }
    }

}
