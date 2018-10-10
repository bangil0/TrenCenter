package com.meivaldi.trencenter.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.activity.DetailPesan;
import com.meivaldi.trencenter.adapter.MessageAdapter;
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.app.AppController;
import com.meivaldi.trencenter.helper.SQLiteHandler;
import com.meivaldi.trencenter.listener.RecyclerTouchListener;
import com.meivaldi.trencenter.model.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OutboxFragment extends Fragment {

    private RecyclerView recyclerView;
    private MessageAdapter mAdapter;
    private ArrayList<Message> messagesList;

    private SQLiteHandler db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_outbox, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_outbox);
        messagesList = new ArrayList<>();
        mAdapter = new MessageAdapter(getContext(), messagesList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), DetailPesan.class);
                intent.putExtra("INDEX", position);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        db = new SQLiteHandler(getContext());
        HashMap<String, String> user = db.getUserDetails();
        String pengirim = user.get("username");
        String tipe = user.get("type");

        loadMessage(pengirim, tipe);

        return rootView;
    }

    private void loadMessage(final String nama, final String tipe) {
        String tag_string_req = "req_get_outbox";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_OUTBOX, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Ambil Pesan", "Mengambil Pesan");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");

                    if(!error){
                        JSONArray jsonArray = jsonObject.getJSONArray("outbox");

                        for(int i = 0; i<jsonArray.length(); i++){
                            JSONArray array = jsonArray.getJSONArray(i);
                            String penerima = array.getString(4);
                            String tanggal = array.getString(8);
                            String foto = array.getString(6);

                            Log.d("FOTO", foto);

                            messagesList.add(new Message(penerima, tanggal, foto));
                        }

                        mAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Ambil Pesan", "Sending Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        "Terjadi kesalahan.", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pengirim", nama);
                params.put("tipe", tipe);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}