package com.meivaldi.trencenter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.activity.DetailPesan;
import com.meivaldi.trencenter.activity.DetailProgram;
import com.meivaldi.trencenter.adapter.MessageAdapter;
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.app.AppController;
import com.meivaldi.trencenter.helper.SQLiteHandler;
import com.meivaldi.trencenter.model.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InboxFragment extends Fragment {

    private ListView listView;
    private MessageAdapter mAdapter;
    private ArrayList<Message> messagesList;

    private SQLiteHandler db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);

        listView = (ListView) rootView.findViewById(R.id.inbox_list);
        messagesList = new ArrayList<>();

        db = new SQLiteHandler(getContext());
        HashMap<String, String> user = db.getUserDetails();
        String penerima = user.get("username");
        String tipe = user.get("type");

        loadMessage(penerima, tipe);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), DetailPesan.class);
                intent.putExtra("INDEX", i);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void loadMessage(final String nama, final String tipe) {
        String tag_string_req = "req_get_inbox";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_INBOX, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Ambil Pesan", "Mengambil Pesan");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");

                    if(!error){
                        JSONArray jsonArray = jsonObject.getJSONArray("inbox");

                        for(int i = 0; i<jsonArray.length(); i++){
                            JSONArray array = jsonArray.getJSONArray(i);
                            String pengirim = array.getString(1);
                            String tanggal = array.getString(5);
                            String foto = array.getString(3);

                            Log.d("FOTO", foto);

                            messagesList.add(new Message(pengirim, tanggal, foto));
                        }

                        mAdapter = new MessageAdapter(getContext(), messagesList);
                        listView.setAdapter(mAdapter);
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
                params.put("penerima", nama);
                params.put("tipe", tipe);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
