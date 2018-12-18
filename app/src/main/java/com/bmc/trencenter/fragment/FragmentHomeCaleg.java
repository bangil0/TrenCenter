package com.bmc.trencenter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.bmc.trencenter.R;
import com.bmc.trencenter.adapter.LogisticReportAdapter;
import com.bmc.trencenter.app.AppConfig;
import com.bmc.trencenter.app.AppController;
import com.bmc.trencenter.model.LogisticReport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentHomeCaleg extends Fragment {

    private TextView hari, jam, menit, detik, pemenangan, relawan, pendukung;
    private Calendar calendar;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private static String TAG = FragmentHomeCaleg.class.getSimpleName();

    private LogisticReportAdapter adapter;
    private List<LogisticReport> list;
    private RecyclerView recyclerView;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");

    private LinearLayout senin, selasa, rabu, kamis, jumat, sabtu, minggu;
    private TextView total1, total2, total3, total4, total5, total6, total7;
    private TextView hari1, hari2, hari3, hari4, hari5, hari6, hari7;

    private LinearLayout seninPemenangan, selasaPemenangan, rabuPemenangan, kamisPemenangan,
            jumatPemenangan, sabtuPemenangan, mingguPemenangan;
    private TextView total1Pemenangan, total2Pemenangan, total3Pemenangan, total4Pemenangan,
            total5Pemenangan, total6Pemenangan, total7Pemenangan;
    private TextView hari1Pemenangan, hari2Pemenangan, hari3Pemenangan, hari4Pemenangan,
            hari5Pemenangan, hari6Pemenangan, hari7Pemenangan;

    private LinearLayout seninPendukung, selasaPendukung, rabuPendukung, kamisPendukung,
            jumatPendukung, sabtuPendukung, mingguPendukung;
    private TextView total1Pendukung, total2Pendukung, total3Pendukung, total4Pendukung,
            total5Pendukung, total6Pendukung, total7Pendukung;
    private TextView hari1Pendukung, hari2Pendukung, hari3Pendukung, hari4Pendukung,
            hari5Pendukung, hari6Pendukung, hari7Pendukung;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_caleg, container, false);

        senin = (LinearLayout) view.findViewById(R.id.chart1);
        selasa = (LinearLayout) view.findViewById(R.id.chart2);
        rabu = (LinearLayout) view.findViewById(R.id.chart3);
        kamis = (LinearLayout) view.findViewById(R.id.chart4);
        jumat = (LinearLayout) view.findViewById(R.id.chart5);
        sabtu = (LinearLayout) view.findViewById(R.id.chart6);
        minggu = (LinearLayout) view.findViewById(R.id.chart7);

        total1 = (TextView) view.findViewById(R.id.sum1);
        total2 = (TextView) view.findViewById(R.id.sum2);
        total3 = (TextView) view.findViewById(R.id.sum3);
        total4 = (TextView) view.findViewById(R.id.sum4);
        total5 = (TextView) view.findViewById(R.id.sum5);
        total6 = (TextView) view.findViewById(R.id.sum6);
        total7 = (TextView) view.findViewById(R.id.sum7);

        hari1 = (TextView) view.findViewById(R.id.date1);
        hari2 = (TextView) view.findViewById(R.id.date2);
        hari3 = (TextView) view.findViewById(R.id.date3);
        hari4 = (TextView) view.findViewById(R.id.date4);
        hari5 = (TextView) view.findViewById(R.id.date5);
        hari6 = (TextView) view.findViewById(R.id.date6);
        hari7 = (TextView) view.findViewById(R.id.date7);

        seninPemenangan = (LinearLayout) view.findViewById(R.id.chart1Pemenangan);
        selasaPemenangan = (LinearLayout) view.findViewById(R.id.chart2Pemenangan);
        rabuPemenangan = (LinearLayout) view.findViewById(R.id.chart3Pemenangan);
        kamisPemenangan = (LinearLayout) view.findViewById(R.id.chart4Pemenangan);
        jumatPemenangan = (LinearLayout) view.findViewById(R.id.chart5Pemenangan);
        sabtuPemenangan = (LinearLayout) view.findViewById(R.id.chart6Pemenangan);
        mingguPemenangan = (LinearLayout) view.findViewById(R.id.chart7Pemenangan);

        total1Pemenangan = (TextView) view.findViewById(R.id.sum1Pemenangan);
        total2Pemenangan = (TextView) view.findViewById(R.id.sum2Pemenangan);
        total3Pemenangan = (TextView) view.findViewById(R.id.sum3Pemenangan);
        total4Pemenangan = (TextView) view.findViewById(R.id.sum4Pemenangan);
        total5Pemenangan = (TextView) view.findViewById(R.id.sum5Pemenangan);
        total6Pemenangan = (TextView) view.findViewById(R.id.sum6Pemenangan);
        total7Pemenangan = (TextView) view.findViewById(R.id.sum7Pemenangan);

        seninPendukung = (LinearLayout) view.findViewById(R.id.chart1Pendukung);
        selasaPendukung = (LinearLayout) view.findViewById(R.id.chart2Pendukung);
        rabuPendukung = (LinearLayout) view.findViewById(R.id.chart3Pendukung);
        kamisPendukung = (LinearLayout) view.findViewById(R.id.chart4Pendukung);
        jumatPendukung = (LinearLayout) view.findViewById(R.id.chart5Pendukung);
        sabtuPendukung = (LinearLayout) view.findViewById(R.id.chart6Pendukung);
        mingguPendukung = (LinearLayout) view.findViewById(R.id.chart7Pendukung);

        total1Pendukung = (TextView) view.findViewById(R.id.sum1Pendukung);
        total2Pendukung = (TextView) view.findViewById(R.id.sum2Pendukung);
        total3Pendukung = (TextView) view.findViewById(R.id.sum3Pendukung);
        total4Pendukung = (TextView) view.findViewById(R.id.sum4Pendukung);
        total5Pendukung = (TextView) view.findViewById(R.id.sum5Pendukung);
        total6Pendukung = (TextView) view.findViewById(R.id.sum6Pendukung);
        total7Pendukung = (TextView) view.findViewById(R.id.sum7Pendukung);

        hari1Pendukung = (TextView) view.findViewById(R.id.date1Pendukung);
        hari2Pendukung = (TextView) view.findViewById(R.id.date2Pendukung);
        hari3Pendukung = (TextView) view.findViewById(R.id.date3Pendukung);
        hari4Pendukung = (TextView) view.findViewById(R.id.date4Pendukung);
        hari5Pendukung = (TextView) view.findViewById(R.id.date5Pendukung);
        hari6Pendukung = (TextView) view.findViewById(R.id.date6Pendukung);
        hari7Pendukung = (TextView) view.findViewById(R.id.date7Pendukung);

        hari1Pemenangan = (TextView) view.findViewById(R.id.date1Pemenangan);
        hari2Pemenangan = (TextView) view.findViewById(R.id.date2Pemenangan);
        hari3Pemenangan = (TextView) view.findViewById(R.id.date3Pemenangan);
        hari4Pemenangan = (TextView) view.findViewById(R.id.date4Pemenangan);
        hari5Pemenangan = (TextView) view.findViewById(R.id.date5Pemenangan);
        hari6Pemenangan = (TextView) view.findViewById(R.id.date6Pemenangan);
        hari7Pemenangan = (TextView) view.findViewById(R.id.date7Pemenangan);

        hari = (TextView) view.findViewById(R.id.hari);
        jam = (TextView) view.findViewById(R.id.jam);
        menit = (TextView) view.findViewById(R.id.menit);
        detik = (TextView) view.findViewById(R.id.detik);
        pemenangan = (TextView) view.findViewById(R.id.totalPemenangan);
        relawan = (TextView) view.findViewById(R.id.totalRelawan);
        pendukung = (TextView) view.findViewById(R.id.totalPendukung);

        recyclerView = (RecyclerView) view.findViewById(R.id.logistikList);
        calendar = Calendar.getInstance();

        list = new ArrayList<>();
        adapter = new LogisticReportAdapter(getContext(), list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        getSummaries();
        getCharts(dateFormat.format(calendar.getTime()));

        countDown();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -6);
        Date d1 = c.getTime();
        c.add(Calendar.DATE, 1);
        Date d2 = c.getTime();
        c.add(Calendar.DATE, 1);
        Date d3 = c.getTime();
        c.add(Calendar.DATE, 1);
        Date d4 = c.getTime();
        c.add(Calendar.DATE, 1);
        Date d5 = c.getTime();
        c.add(Calendar.DATE, 1);
        Date d6 = c.getTime();
        c.add(Calendar.DATE, 1);
        Date d7 = c.getTime();

        String h1 = sdf.format(d1);
        String h2 = sdf.format(d2);
        String h3 = sdf.format(d3);
        String h4 = sdf.format(d4);
        String h5 = sdf.format(d5);
        String h6 = sdf.format(d6);
        String h7 = sdf.format(d7);

        hari1.setText(h1);
        hari2.setText(h2);
        hari3.setText(h3);
        hari4.setText(h4);
        hari5.setText(h5);
        hari6.setText(h6);
        hari7.setText(h7);

        hari1Pemenangan.setText(h1);
        hari2Pemenangan.setText(h2);
        hari3Pemenangan.setText(h3);
        hari4Pemenangan.setText(h4);
        hari5Pemenangan.setText(h5);
        hari6Pemenangan.setText(h6);
        hari7Pemenangan.setText(h7);

        hari1Pendukung.setText(h1);
        hari2Pendukung.setText(h2);
        hari3Pendukung.setText(h3);
        hari4Pendukung.setText(h4);
        hari5Pendukung.setText(h5);
        hari6Pendukung.setText(h6);
        hari7Pendukung.setText(h7);

        return view;
    }

    private void getSummaries(){
        String tag_string_req = "req_summaries";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_SUMMARIES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");

                    if(!error){
                        String totalPemenangan = jsonObject.getString("pemenangan");
                        String totalRelawan = jsonObject.getString("relawan");
                        String totalPendukung = jsonObject.getString("pendukung");

                        pemenangan.setText(totalPemenangan);
                        relawan.setText(totalRelawan);
                        pendukung.setText(totalPendukung);

                        JSONArray logistik = jsonObject.getJSONArray("logistik");

                        String nama, foto, total;
                        for(int i=0; i<logistik.length(); i++){
                            nama = logistik.getJSONArray(i).getString(0);
                            foto = logistik.getJSONArray(i).getString(1);
                            total = logistik.getJSONArray(i).getString(2);

                            list.add(new LogisticReport(nama, total, foto));
                        }

                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SUMMARIES", "Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        "Terjadi kesalahan.", Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void getCharts(final String date) {
        String tag_string_req = "req_charts";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_CHART, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");

                    if(!error){
                        JSONArray sum = jsonObject.getJSONArray("relawan");
                        int monday = jsonObject.getInt("senin");
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)senin.getLayoutParams();
                        layoutParams.height = dpToPx(25);
                        layoutParams.width = dpToPx(monday);
                        senin.setLayoutParams(layoutParams);
                        total1.setText(sum.getString(0));

                        int tuesday = jsonObject.getInt("selasa");
                        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams)selasa.getLayoutParams();
                        layoutParams2.height = dpToPx(25);
                        layoutParams2.width = dpToPx(tuesday);
                        selasa.setLayoutParams(layoutParams2);
                        total2.setText(sum.getString(1));

                        int wednesday = jsonObject.getInt("rabu");
                        RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams)rabu.getLayoutParams();
                        layoutParams3.height = dpToPx(25);
                        layoutParams3.width = dpToPx(wednesday);
                        rabu.setLayoutParams(layoutParams3);
                        total3.setText(sum.getString(2));

                        int thursday = jsonObject.getInt("kamis");
                        RelativeLayout.LayoutParams layoutParams4 = (RelativeLayout.LayoutParams)kamis.getLayoutParams();
                        layoutParams4.height = dpToPx(25);
                        layoutParams4.width = dpToPx(thursday);
                        kamis.setLayoutParams(layoutParams4);
                        total4.setText(sum.getString(3));

                        int friday = jsonObject.getInt("jumat");
                        RelativeLayout.LayoutParams layoutParams5 = (RelativeLayout.LayoutParams)jumat.getLayoutParams();
                        layoutParams5.height = dpToPx(25);
                        layoutParams5.width = dpToPx(friday);
                        jumat.setLayoutParams(layoutParams5);
                        total5.setText(sum.getString(4));

                        int saturday = jsonObject.getInt("sabtu");
                        RelativeLayout.LayoutParams layoutParams6 = (RelativeLayout.LayoutParams)sabtu.getLayoutParams();
                        layoutParams6.height = dpToPx(25);
                        layoutParams6.width = dpToPx(saturday);
                        sabtu.setLayoutParams(layoutParams6);
                        total6.setText(sum.getString(5));

                        int sunday = jsonObject.getInt("minggu");
                        RelativeLayout.LayoutParams layoutParams7 = (RelativeLayout.LayoutParams)minggu.getLayoutParams();
                        layoutParams7.height = dpToPx(25);
                        layoutParams7.width = dpToPx(sunday);
                        minggu.setLayoutParams(layoutParams7);
                        total7.setText(sum.getString(6));

                        //pemenangan
                        JSONArray sumPemenangan = jsonObject.getJSONArray("pemenangan");
                        int mondayPemenangan = jsonObject.getInt("senin_pemenangan");
                        RelativeLayout.LayoutParams layoutParamsPemenangan = (RelativeLayout.LayoutParams)seninPemenangan.getLayoutParams();
                        layoutParamsPemenangan.height = dpToPx(25);
                        layoutParamsPemenangan.width = dpToPx(mondayPemenangan);
                        seninPemenangan.setLayoutParams(layoutParamsPemenangan);
                        total1Pemenangan.setText(sumPemenangan.getString(0));

                        int tuesdayPemenangan = jsonObject.getInt("selasa_pemenangan");
                        RelativeLayout.LayoutParams layoutParams2Pemenangan = (RelativeLayout.LayoutParams)selasaPemenangan.getLayoutParams();
                        layoutParams2Pemenangan.height = dpToPx(25);
                        layoutParams2Pemenangan.width = dpToPx(tuesdayPemenangan);
                        selasaPemenangan.setLayoutParams(layoutParams2Pemenangan);
                        total2Pemenangan.setText(sumPemenangan.getString(1));

                        int wednesdayPemenangan = jsonObject.getInt("rabu_pemenangan");
                        RelativeLayout.LayoutParams layoutParams3Pemenangan = (RelativeLayout.LayoutParams)rabuPemenangan.getLayoutParams();
                        layoutParams3Pemenangan.height = dpToPx(25);
                        layoutParams3Pemenangan.width = dpToPx(wednesdayPemenangan);
                        rabuPemenangan.setLayoutParams(layoutParams3Pemenangan);
                        total3Pemenangan.setText(sumPemenangan.getString(2));

                        int thursdayPemenangan = jsonObject.getInt("kamis_pemenangan");
                        RelativeLayout.LayoutParams layoutParams4Pemenangan = (RelativeLayout.LayoutParams)kamisPemenangan.getLayoutParams();
                        layoutParams4Pemenangan.height = dpToPx(25);
                        layoutParams4Pemenangan.width = dpToPx(thursdayPemenangan);
                        kamisPemenangan.setLayoutParams(layoutParams4Pemenangan);
                        total4Pemenangan.setText(sumPemenangan.getString(3));

                        int fridayPemenangan = jsonObject.getInt("jumat_pemenangan");
                        RelativeLayout.LayoutParams layoutParams5Pemenangan = (RelativeLayout.LayoutParams)jumatPemenangan.getLayoutParams();
                        layoutParams5Pemenangan.height = dpToPx(25);
                        layoutParams5Pemenangan.width = dpToPx(fridayPemenangan);
                        jumatPemenangan.setLayoutParams(layoutParams5Pemenangan);
                        total5Pemenangan.setText(sumPemenangan.getString(4));

                        int saturdayPemenangan = jsonObject.getInt("sabtu_pemenangan");
                        RelativeLayout.LayoutParams layoutParams6Pemenangan = (RelativeLayout.LayoutParams)sabtuPemenangan.getLayoutParams();
                        layoutParams6Pemenangan.height = dpToPx(25);
                        layoutParams6Pemenangan.width = dpToPx(saturdayPemenangan);
                        sabtuPemenangan.setLayoutParams(layoutParams6Pemenangan);
                        total6Pemenangan.setText(sumPemenangan.getString(5));

                        int sundayPemenangan = jsonObject.getInt("minggu_pemenangan");
                        RelativeLayout.LayoutParams layoutParams7Pemenangan = (RelativeLayout.LayoutParams)mingguPemenangan.getLayoutParams();
                        layoutParams7Pemenangan.height = dpToPx(25);
                        layoutParams7Pemenangan.width = dpToPx(sundayPemenangan);
                        mingguPemenangan.setLayoutParams(layoutParams7Pemenangan);
                        total7Pemenangan.setText(sumPemenangan.getString(6));

                        //pendukung
                        JSONArray sumPendukung = jsonObject.getJSONArray("pendukung");
                        int mondayPendukung = jsonObject.getInt("senin_pendukung");
                        RelativeLayout.LayoutParams layoutParamsPendukung = (RelativeLayout.LayoutParams)seninPendukung.getLayoutParams();
                        layoutParamsPendukung.height = dpToPx(25);
                        layoutParamsPendukung.width = dpToPx(mondayPendukung);
                        seninPendukung.setLayoutParams(layoutParamsPendukung);
                        total1Pendukung.setText(sumPendukung.getString(0));

                        int tuesdayPendukung = jsonObject.getInt("selasa_pendukung");
                        RelativeLayout.LayoutParams layoutParams2Pendukung = (RelativeLayout.LayoutParams)selasaPendukung.getLayoutParams();
                        layoutParams2Pendukung.height = dpToPx(25);
                        layoutParams2Pendukung.width = dpToPx(tuesdayPendukung);
                        selasaPendukung.setLayoutParams(layoutParams2Pendukung);
                        total2Pendukung.setText(sumPendukung.getString(1));

                        int wednesdayPendukung = jsonObject.getInt("rabu_pendukung");
                        RelativeLayout.LayoutParams layoutParams3Pendukung = (RelativeLayout.LayoutParams)rabuPendukung.getLayoutParams();
                        layoutParams3Pendukung.height = dpToPx(25);
                        layoutParams3Pendukung.width = dpToPx(wednesdayPendukung);
                        rabuPendukung.setLayoutParams(layoutParams3Pendukung);
                        total3Pendukung.setText(sumPendukung.getString(2));

                        int thursdayPendukung = jsonObject.getInt("kamis_pendukung");
                        RelativeLayout.LayoutParams layoutParams4Pendukung = (RelativeLayout.LayoutParams)kamisPendukung.getLayoutParams();
                        layoutParams4Pendukung.height = dpToPx(25);
                        layoutParams4Pendukung.width = dpToPx(thursdayPendukung);
                        kamisPendukung.setLayoutParams(layoutParams4Pendukung);
                        total4Pendukung.setText(sumPendukung.getString(3));

                        int fridayPendukung = jsonObject.getInt("jumat_pendukung");
                        RelativeLayout.LayoutParams layoutParams5Pendukung = (RelativeLayout.LayoutParams)jumatPendukung.getLayoutParams();
                        layoutParams5Pendukung.height = dpToPx(25);
                        layoutParams5Pendukung.width = dpToPx(fridayPendukung);
                        jumatPendukung.setLayoutParams(layoutParams5Pendukung);
                        total5Pendukung.setText(sumPendukung.getString(4));

                        int saturdayPendukung = jsonObject.getInt("sabtu_pendukung");
                        RelativeLayout.LayoutParams layoutParams6Pendukung = (RelativeLayout.LayoutParams)sabtuPendukung.getLayoutParams();
                        layoutParams6Pendukung.height = dpToPx(25);
                        layoutParams6Pendukung.width = dpToPx(saturdayPendukung);
                        sabtuPendukung.setLayoutParams(layoutParams6Pendukung);
                        total6Pendukung.setText(sumPendukung.getString(5));

                        int sundayPendukung = jsonObject.getInt("minggu_pendukung");
                        RelativeLayout.LayoutParams layoutParams7Pendukung = (RelativeLayout.LayoutParams)mingguPendukung.getLayoutParams();
                        layoutParams7Pendukung.height = dpToPx(25);
                        layoutParams7Pendukung.width = dpToPx(sundayPendukung);
                        mingguPendukung.setLayoutParams(layoutParams7Pendukung);
                        total7Pendukung.setText(sumPendukung.getString(6));

                    } else {
                        Toast.makeText(getContext(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("CHART", "Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        "Terjadi kesalahan.", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("date", date);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private int dpToPx(int dp) {
        float density = getContext().getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
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
            public void onTick(final long l) {
                detik.post(new Runnable() {
                    @Override
                    public void run() {
                        detik.setText("" + l / 1000);
                    }
                });
            }

            @Override
            public void onFinish() {
                countDown();
            }
        }.start();

    }

}
