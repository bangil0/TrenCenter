package com.meivaldi.trencenter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.activity.EditVisiMisi;
import com.meivaldi.trencenter.adapter.MisiAdapter;
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.app.AppController;
import com.meivaldi.trencenter.listener.MisiListener;
import com.meivaldi.trencenter.model.VisiMisiModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MisiAdminFragment extends Fragment implements MisiListener.RecyclerItemTouchHelperListener{

    private RecyclerView recyclerView;
    private List<VisiMisiModel> misi;
    private MisiAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_misi_admin, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.misi_list);
        misi = new ArrayList<>();
        adapter = new MisiAdapter(getContext(), misi);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new MisiListener(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        getVisi("51");
        return rootView;
    }

    private void getVisi(final String id){
        String tag_string_req = "req_visi";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_VISI_MISI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Inbox", "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        JSONArray data = jObj.getJSONArray("data");

                        for(int i=0; i<data.length(); i++){
                            misi.add(new VisiMisiModel(data.getJSONArray(i).getString(1)));
                        }

                        adapter.notifyDataSetChanged();

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
                Log.e("Inbox", "Login Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        "Tidak ada koneksi internet", Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_caleg", id);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        VisiMisiModel visi_misi = misi.get(position);
        String old = visi_misi.getValue();

        Intent intent = new Intent(getContext(), EditVisiMisi.class);
        intent.putExtra("OLD", old);
        intent.putExtra("TIPE", "Misi");
        startActivity(intent);
    }
}
