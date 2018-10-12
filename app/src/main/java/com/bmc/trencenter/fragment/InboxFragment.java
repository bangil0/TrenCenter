package com.bmc.trencenter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bmc.trencenter.R;
import com.bmc.trencenter.activity.DetailPesan;
import com.bmc.trencenter.adapter.MessageAdapter;
import com.bmc.trencenter.app.AppConfig;
import com.bmc.trencenter.app.AppController;
import com.bmc.trencenter.helper.SQLiteHandler;
import com.bmc.trencenter.listener.RecyclerItemTouchHelper;
import com.bmc.trencenter.listener.RecyclerTouchListener;
import com.bmc.trencenter.model.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InboxFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    private MessageAdapter mAdapter;
    private ArrayList<Message> messagesList;
    private RelativeLayout relativeLayout;

    private SwipeRefreshLayout swipeRefreshLayout;
    private SQLiteHandler db;
    private String penerima, tipe;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);

        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_layout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_inbox);
        messagesList = new ArrayList<>();
        mAdapter = new MessageAdapter(getContext(), messagesList);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);

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

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        db = new SQLiteHandler(getContext());
        HashMap<String, String> user = db.getUserDetails();
        penerima = user.get("username");
        tipe = user.get("type");
        loadMessage(penerima, tipe);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                messagesList.clear();
                loadMessage(penerima, tipe);
                swipeRefreshLayout.setRefreshing(false);
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
                            String pengirim = array.getString(3);
                            String tanggal = array.getString(8);
                            String foto = array.getString(5);

                            Log.d("FOTO", foto);

                            messagesList.add(new Message(pengirim, tanggal, foto));
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
                params.put("penerima", nama);
                params.put("tipe", tipe);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof MessageAdapter.MyViewHolder) {
            Message msg = messagesList.get(position);
            final String title = msg.getTitle();
            final String date = msg.getDate();
            final String foto = msg.getImage();

            mAdapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackbar = Snackbar
                    .make(relativeLayout, "Pesan telah dihapus!", Snackbar.LENGTH_LONG);

            snackbar.setCallback(new Snackbar.Callback(){
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);

                    deleteMessage(title, date, foto);
                }
            });
            snackbar.show();
        }
    }

    private void deleteMessage(final String title, final String date, final String foto) {
        String tag_string_req = "req_delete_message";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE_MESSAGE_ANOTHER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Hapus Pesan", "Menghapus Pesan");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");

                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Hapus Pesan", "Delete Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        "Terjadi kesalahan, silahkan periksa koneksi internet!", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("judul", title);
                params.put("tanggal", date);
                params.put("foto", foto);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
