package com.bmc.trencenter.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bmc.trencenter.R;
import com.bmc.trencenter.activity.Berita;
import com.bmc.trencenter.activity.LayananActivity;
import com.bmc.trencenter.activity.Partnership;
import com.bmc.trencenter.activity.ProgramKerja;
import com.bmc.trencenter.activity.pendukung.InputPendukung;
import com.bmc.trencenter.adapter.Adapter;
import com.bmc.trencenter.adapter.CardAdapter;
import com.bmc.trencenter.adapter.LayananAdapter;
import com.bmc.trencenter.adapter.PartnershipPemenanganAdapter;
import com.bmc.trencenter.adapter.ViewPagerAdapter;
import com.bmc.trencenter.app.AppConfig;
import com.bmc.trencenter.app.AppController;
import com.bmc.trencenter.helper.SliderUtils;
import com.bmc.trencenter.model.Card;
import com.bmc.trencenter.model.LayananModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentHomeRelawan extends Fragment {

    private TextView hari, detik, menit, jam, selanjutnya, selanjutnya2, selanjutnya3, seeBerita;

    private FloatingActionButton createPendukung;

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private RequestQueue rq;
    private List<SliderUtils> sliderImg;

    private RecyclerView recyclerView, layananRecycler, partnershipRecycler;
    private List<Card> cardList, partnershipList;
    private List<LayananModel> layananList;
    private PartnershipPemenanganAdapter partnershipAdapter;

    private CardAdapter cardAdapter;
    private LayananAdapter layananAdapter;

    private TextView emptyProgram, emptyLayanan, emptyPartnership;

    String request_url = "http://156.67.221.225/voting/android/debug.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home_relawan, container, false);

        rootView.setAlpha(0f);
        rootView.setVisibility(View.GONE);

        hari = (TextView) rootView.findViewById(R.id.hari);
        jam = (TextView) rootView.findViewById(R.id.jam);
        menit = (TextView) rootView.findViewById(R.id.menit);
        detik = (TextView) rootView.findViewById(R.id.detik);
        selanjutnya = (TextView) rootView.findViewById(R.id.selanjutnya);
        selanjutnya2 = (TextView) rootView.findViewById(R.id.selanjutnya2);
        selanjutnya3 = (TextView) rootView.findViewById(R.id.selanjutnya3);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        layananRecycler = (RecyclerView) rootView.findViewById(R.id.recycler_layanan);
        partnershipRecycler = (RecyclerView) rootView.findViewById(R.id.recycler_partnership);
        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        seeBerita = (TextView) rootView.findViewById(R.id.seeBerita);

        seeBerita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Berita.class));
            }
        });

        selanjutnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ProgramKerja.class));
            }
        });

        selanjutnya2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), LayananActivity.class));
            }
        });

        selanjutnya3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Partnership.class));
            }
        });

        rq = Volley.newRequestQueue(getContext());
        sliderImg = new ArrayList<>();

        sendRequest();

        Timer timer = new Timer();
        timer.schedule(new MyTimerTask(), 2000, 4000);

        createPendukung = (FloatingActionButton) rootView.findViewById(R.id.createPendukung);
        createPendukung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), InputPendukung.class));
            }
        });

        layananList = new ArrayList<>();
        layananAdapter = new LayananAdapter(getContext(), layananList);
        cardList = new ArrayList<>();
        cardAdapter = new CardAdapter(getContext(), cardList);
        partnershipList = new ArrayList<>();
        partnershipAdapter = new PartnershipPemenanganAdapter(getContext(), partnershipList);

        emptyProgram = (TextView) rootView.findViewById(R.id.emptyKegiatan);
        emptyLayanan = (TextView) rootView.findViewById(R.id.emptyLayanan);
        emptyPartnership = (TextView) rootView.findViewById(R.id.emptyPartnership);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        partnershipRecycler.setLayoutManager(layoutManager);
        partnershipRecycler.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        partnershipRecycler.setItemAnimator(new DefaultItemAnimator());
        partnershipRecycler.setAdapter(partnershipAdapter);

        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cardAdapter);

        RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(getContext(), 1);
        layananRecycler.setLayoutManager(layoutManager2);
        layananRecycler.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        layananRecycler.setItemAnimator(new DefaultItemAnimator());
        layananRecycler.setAdapter(layananAdapter);

        countDown();
        getLayanan();
        getCards();
        getPartnership();

        rootView.setVisibility(View.VISIBLE);
        rootView.animate()
                .alpha(1f)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .setListener(null);

        return rootView;
    }

    public class MyTimerTask extends TimerTask{

        @Override
        public void run() {

            if(getActivity() == null)
                return;

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int length = adapter.getCount();

                    if(length == 2){
                        if(viewPager.getCurrentItem() == 0){
                            viewPager.setCurrentItem(1);
                        } else if(viewPager.getCurrentItem() == 1){
                            viewPager.setCurrentItem(0);
                        }
                    } else if(length == 3){
                        if(viewPager.getCurrentItem() == 0){
                            viewPager.setCurrentItem(1);
                        } else if(viewPager.getCurrentItem() == 1){
                            viewPager.setCurrentItem(2);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    } else if(length == 4){
                        if(viewPager.getCurrentItem() == 0){
                            viewPager.setCurrentItem(1);
                        } else if(viewPager.getCurrentItem() == 1){
                            viewPager.setCurrentItem(2);
                        } else if(viewPager.getCurrentItem() == 2){
                            viewPager.setCurrentItem(3);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }

                }
            });
        }
    }

    public void sendRequest(){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(request_url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String url = "http://156.67.221.225/voting/dashboard/save/foto_berita/";
                List<String> headlineList = new ArrayList<>();
                List<String> sourceList = new ArrayList<>();

                for (int i=0; i<response.length(); i++){
                    SliderUtils sliderUtils = new SliderUtils();
                    try {
                        JSONArray array = response.getJSONArray(i);

                        String foto = array.getString(7);
                        String tes = "";

                        for(int j=0; j<foto.length(); j++){
                            if(foto.charAt(j) == ' '){
                                tes += "%20";
                            } else {
                                tes += foto.charAt(j);
                            }
                        }

                        String image = url + tes;
                        headlineList.add(array.getString(3));
                        sourceList.add(array.getString(8));

                        sliderUtils.setSliderImageUrl(image);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    sliderImg.add(sliderUtils);
                }

                adapter = new ViewPagerAdapter(sliderImg, headlineList, sourceList, getContext());
                viewPager.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        rq.add(jsonArrayRequest);
    }

    private void countDown(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        String dateStart = format.format(c);
        String dateEnd = "04/17/2019 06:00:00";

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateEnd);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long diff = d2.getTime() - d1.getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        hari.setText("" + diffDays);
        jam.setText("" + diffHours);
        menit.setText("" + diffMinutes);
        detik.setText("" + diffSeconds);

        new CountDownTimer(diffSeconds, 1000){
            @Override
            public void onTick(long l) {
                detik.setText("" + l / 1000);
            }

            @Override
            public void onFinish() {
                countDown();
            }
        }.start();

    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getActivity().getResources();

        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void getLayanan() {
        String tag_string_req = "req_layanan";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_GET_LAYANAN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("BERITA", "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        JSONArray programs = jObj.getJSONArray("layanan");

                        if(programs.length() == 0){
                            emptyLayanan.setVisibility(View.VISIBLE);
                        } else {
                            emptyLayanan.setVisibility(View.GONE);
                        }

                        for (int i = 0; i < programs.length(); i++) {
                            JSONArray program = programs.getJSONArray(i);

                            String nama = program.getString(1);
                            String foto = "http://156.67.221.225/voting/dashboard/save/foto_layanan/" + program.getString(5);

                            layananList.add(new LayananModel(nama, foto));
                        }

                        layananAdapter.notifyDataSetChanged();
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
                Log.e("Berita", "Login Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        "Tidak ada koneksi internet", Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getCards() {
        String tag_string_req = "req_card";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_GET_CARD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("BERITA", "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        JSONArray programs = jObj.getJSONArray("cards");

                        if(programs.length() == 0){
                            emptyProgram.setVisibility(View.VISIBLE);
                        } else {
                            emptyProgram.setVisibility(View.GONE);
                        }

                        for (int i = 0; i < programs.length(); i++) {
                            JSONArray program = programs.getJSONArray(i);

                            String nama = program.getString(1);
                            String tanggalMulai = program.getString(2);
                            String foto = "http://156.67.221.225/voting/dashboard/save/foto_program/" + program.getString(7);

                            cardList.add(new Card(nama, tanggalMulai, foto));
                        }

                        cardAdapter.notifyDataSetChanged();
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
                Log.e("Berita", "Login Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        "Tidak ada koneksi internet", Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getPartnership() {
        String tag_string_req = "req_partnership";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_GET_PARTNERSHIP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("BERITA", "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        JSONArray programs = jObj.getJSONArray("partnership");

                        if(programs.length() == 0){
                            emptyPartnership.setVisibility(View.VISIBLE);
                        } else {
                            emptyPartnership.setVisibility(View.GONE);
                        }

                        for (int i = 0; i < programs.length(); i++) {
                            JSONArray program = programs.getJSONArray(i);

                            String nama = program.getString(1);
                            String tanggalMulai = program.getString(2);
                            String foto = "http://156.67.221.225/voting/dashboard/save/foto_partnership/" + program.getString(7);

                            partnershipList.add(new Card(nama, tanggalMulai, foto));
                        }

                        partnershipAdapter.notifyDataSetChanged();
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
                Log.e("Berita", "Login Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        "Tidak ada koneksi internet", Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
