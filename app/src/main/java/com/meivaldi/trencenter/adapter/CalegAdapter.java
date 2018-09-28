package com.meivaldi.trencenter.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.app.AppController;
import com.meivaldi.trencenter.helper.CircleTransform;
import com.meivaldi.trencenter.helper.SQLiteHandler;
import com.meivaldi.trencenter.model.Caleg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 12/09/18.
 */

public class CalegAdapter extends ArrayAdapter {

    private Context context;
    private List<Caleg> calegList;
    private SQLiteHandler db;
    private String pengirim;

    public CalegAdapter(Context context, List<Caleg> calegList){
        super(context, 0, calegList);
        this.context = context;
        this.calegList = calegList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.user_list,parent,false);

        db = new SQLiteHandler(context);
        HashMap<String, String> user = db.getUserDetails();
        pengirim = user.get("username");

        final Caleg currentCaleg = calegList.get(position);

        TextView nama = (TextView) listItem.findViewById(R.id.namaCaleg);
        nama.setText(currentCaleg.getNama());

        ImageView imageView = (ImageView) listItem.findViewById(R.id.calegImage);
        String url = currentCaleg.getImage();

        LinearLayout panggil = (LinearLayout) listItem.findViewById(R.id.panggil);
        panggil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(currentCaleg.getNama(), pengirim);
            }
        });

        Glide.with(getContext()).load(url)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        return listItem;
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
}
