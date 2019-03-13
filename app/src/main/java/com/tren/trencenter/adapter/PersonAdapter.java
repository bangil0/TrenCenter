package com.tren.trencenter.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tren.trencenter.R;
import com.tren.trencenter.app.AppConfig;
import com.tren.trencenter.app.AppController;
import com.tren.trencenter.helper.CircleTransform;
import com.tren.trencenter.model.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 07/01/19.
 */

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.MyViewHolder> {

    private Context context;
    private List<Person> calegList;
    private Dialog dialog;

    public PersonAdapter(Context context, List<Person> calegList) {
        this.context = context;
        this.calegList = calegList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Person current = calegList.get(position);
        holder.nama.setText(current.getNama());

        Glide.with(context).load(current.getImage())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.foto);

        if(current.getVerified() == 0) {
            holder.parent.setBackgroundResource(R.color.red);
        } else {
            holder.parent.setBackgroundResource(R.color.green);
        }

        if(current.getVerified() == 0) {
            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog);
                    dialog.setCancelable(true);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    Button iya = (Button) dialog.findViewById(R.id.iya);
                    Button nggak = (Button) dialog.findViewById(R.id.nggak);

                    iya.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateValue(current.getNama(), current.getTipe());
                            holder.parent.setBackgroundResource(R.color.green);
                            dialog.dismiss();
                        }
                    });

                    nggak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });
        }

    }

    private void updateValue(final String nama, final String tipe) {
        String tag_string_req = "req_pemenangan";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_VERIFIED_USER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Inbox", "Login Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String msg = jObj.getString("message");
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Inbox", "Login Error: " + error.getMessage());
                Toast.makeText(context,
                        "Tidak ada koneksi internet", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("tipe", tipe);
                params.put("nama", nama);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public int getItemCount() {
        return calegList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nama;
        private ImageView foto;
        private LinearLayout parent;

        public MyViewHolder(View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.namaCaleg);
            foto = itemView.findViewById(R.id.calegImage);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}