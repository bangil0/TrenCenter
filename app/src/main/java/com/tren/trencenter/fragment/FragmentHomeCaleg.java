package com.tren.trencenter.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tren.trencenter.R;
import com.tren.trencenter.adapter.LogisticReportAdapter;
import com.tren.trencenter.app.AppConfig;
import com.tren.trencenter.app.AppController;
import com.tren.trencenter.model.LogisticReport;

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

    private LinearLayout kota, denai, deli, belawan, amplas, area, marelan, labuhan, tembung, perjuangan, timur;
    private TextView total1, total2, total3, total4, total5, total6, total7, total8, total9, total10, total11;

    private LinearLayout kotaPemenangan, denaiPemenangan, deliPemenangan, belawanPemenangan,
            amplasPemenangan, areaPemenangan, marelanPemenangan, labuhanPemenangan, tembungPemenangan
            , perjuanganPemenangan, timurPemenangan;
    private TextView total1Pemenangan, total2Pemenangan, total3Pemenangan, total4Pemenangan,
            total5Pemenangan, total6Pemenangan, total7Pemenangan, total8Pemenangan, total9Pemenangan,
            total10Pemenangan, total11Pemenangan;

    private LinearLayout kotaPendukung, denaiPendukung, deliPendukung, belawanPendukung,
            amplasPendukung, areaPendukung, marelanPendukung, labuhanPendukung, tembungPendukung
            , perjuanganPendukung, timurPendukung;
    private TextView total1Pendukung, total2Pendukung, total3Pendukung, total4Pendukung,
            total5Pendukung, total6Pendukung, total7Pendukung, total8Pendukung, total9Pendukung,
            total10Pendukung, total11Pendukung;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_caleg, container, false);

        kota = (LinearLayout) view.findViewById(R.id.chart1);
        denai = (LinearLayout) view.findViewById(R.id.chart2);
        deli = (LinearLayout) view.findViewById(R.id.chart3);
        belawan = (LinearLayout) view.findViewById(R.id.chart4);
        amplas = (LinearLayout) view.findViewById(R.id.chart5);
        area = (LinearLayout) view.findViewById(R.id.chart6);
        marelan = (LinearLayout) view.findViewById(R.id.chart7);
        labuhan = (LinearLayout) view.findViewById(R.id.chart8);
        tembung = (LinearLayout) view.findViewById(R.id.chart9);
        perjuangan = (LinearLayout) view.findViewById(R.id.chart10);
        timur = (LinearLayout) view.findViewById(R.id.chart11);

        total1 = (TextView) view.findViewById(R.id.sum1);
        total2 = (TextView) view.findViewById(R.id.sum2);
        total3 = (TextView) view.findViewById(R.id.sum3);
        total4 = (TextView) view.findViewById(R.id.sum4);
        total5 = (TextView) view.findViewById(R.id.sum5);
        total6 = (TextView) view.findViewById(R.id.sum6);
        total7 = (TextView) view.findViewById(R.id.sum7);
        total8 = (TextView) view.findViewById(R.id.sum8);
        total9 = (TextView) view.findViewById(R.id.sum9);
        total10 = (TextView) view.findViewById(R.id.sum10);
        total11 = (TextView) view.findViewById(R.id.sum11);

        kotaPemenangan = (LinearLayout) view.findViewById(R.id.chart1Pemenangan);
        denaiPemenangan = (LinearLayout) view.findViewById(R.id.chart2Pemenangan);
        deliPemenangan = (LinearLayout) view.findViewById(R.id.chart3Pemenangan);
        belawanPemenangan = (LinearLayout) view.findViewById(R.id.chart4Pemenangan);
        amplasPemenangan = (LinearLayout) view.findViewById(R.id.chart5Pemenangan);
        areaPemenangan = (LinearLayout) view.findViewById(R.id.chart6Pemenangan);
        marelanPemenangan = (LinearLayout) view.findViewById(R.id.chart7Pemenangan);
        labuhanPemenangan = (LinearLayout) view.findViewById(R.id.chart8Pemenangan);
        tembungPemenangan = (LinearLayout) view.findViewById(R.id.chart9Pemenangan);
        perjuanganPemenangan = (LinearLayout) view.findViewById(R.id.chart10Pemenangan);
        timurPemenangan = (LinearLayout) view.findViewById(R.id.chart11Pemenangan);

        total1Pemenangan = (TextView) view.findViewById(R.id.sum1Pemenangan);
        total2Pemenangan = (TextView) view.findViewById(R.id.sum2Pemenangan);
        total3Pemenangan = (TextView) view.findViewById(R.id.sum3Pemenangan);
        total4Pemenangan = (TextView) view.findViewById(R.id.sum4Pemenangan);
        total5Pemenangan = (TextView) view.findViewById(R.id.sum5Pemenangan);
        total6Pemenangan = (TextView) view.findViewById(R.id.sum6Pemenangan);
        total7Pemenangan = (TextView) view.findViewById(R.id.sum7Pemenangan);
        total8Pemenangan = (TextView) view.findViewById(R.id.sum8Pemenangan);
        total9Pemenangan = (TextView) view.findViewById(R.id.sum9Pemenangan);
        total10Pemenangan = (TextView) view.findViewById(R.id.sum10Pemenangan);
        total11Pemenangan = (TextView) view.findViewById(R.id.sum11Pemenangan);

        kotaPendukung = (LinearLayout) view.findViewById(R.id.chart1Pendukung);
        denaiPendukung = (LinearLayout) view.findViewById(R.id.chart2Pendukung);
        deliPendukung = (LinearLayout) view.findViewById(R.id.chart3Pendukung);
        belawanPendukung = (LinearLayout) view.findViewById(R.id.chart4Pendukung);
        amplasPendukung = (LinearLayout) view.findViewById(R.id.chart5Pendukung);
        areaPendukung = (LinearLayout) view.findViewById(R.id.chart6Pendukung);
        marelanPendukung = (LinearLayout) view.findViewById(R.id.chart7Pendukung);
        labuhanPendukung = (LinearLayout) view.findViewById(R.id.chart8Pendukung);
        tembungPendukung = (LinearLayout) view.findViewById(R.id.chart9Pendukung);
        perjuanganPendukung = (LinearLayout) view.findViewById(R.id.chart10Pendukung);
        timurPendukung = (LinearLayout) view.findViewById(R.id.chart11Pendukung);

        total1Pendukung = (TextView) view.findViewById(R.id.sum1Pendukung);
        total2Pendukung = (TextView) view.findViewById(R.id.sum2Pendukung);
        total3Pendukung = (TextView) view.findViewById(R.id.sum3Pendukung);
        total4Pendukung = (TextView) view.findViewById(R.id.sum4Pendukung);
        total5Pendukung = (TextView) view.findViewById(R.id.sum5Pendukung);
        total6Pendukung = (TextView) view.findViewById(R.id.sum6Pendukung);
        total7Pendukung = (TextView) view.findViewById(R.id.sum7Pendukung);
        total8Pendukung = (TextView) view.findViewById(R.id.sum8Pendukung);
        total9Pendukung = (TextView) view.findViewById(R.id.sum9Pendukung);
        total10Pendukung = (TextView) view.findViewById(R.id.sum10Pendukung);
        total11Pendukung = (TextView) view.findViewById(R.id.sum11Pendukung);

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
                        String totalPemenangan = jsonObject.getString("totalPemenangan");
                        String totalRelawan = jsonObject.getString("totalRelawan");
                        String totalPendukung = jsonObject.getString("totalPendukung");

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
                        JSONObject sum = jsonObject.getJSONObject("relawan");
                        int first = jsonObject.getInt("kota_relawan");
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)kota.getLayoutParams();
                        layoutParams.height = dpToPx(25);
                        layoutParams.width = dpToPx(first);
                        kota.setLayoutParams(layoutParams);
                        total1.setText(sum.getString("kota"));

                        int second = jsonObject.getInt("denai_relawan");
                        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams)denai.getLayoutParams();
                        layoutParams2.height = dpToPx(25);
                        layoutParams2.width = dpToPx(second);
                        denai.setLayoutParams(layoutParams2);
                        total2.setText(sum.getString("denai"));

                        int third = jsonObject.getInt("deli_relawan");
                        RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams)deli.getLayoutParams();
                        layoutParams3.height = dpToPx(25);
                        layoutParams3.width = dpToPx(third);
                        deli.setLayoutParams(layoutParams3);
                        total3.setText(sum.getString("deli"));

                        int fourth = jsonObject.getInt("belawan_relawan");
                        RelativeLayout.LayoutParams layoutParams4 = (RelativeLayout.LayoutParams)belawan.getLayoutParams();
                        layoutParams4.height = dpToPx(25);
                        layoutParams4.width = dpToPx(fourth);
                        belawan.setLayoutParams(layoutParams4);
                        total4.setText(sum.getString("belawan"));

                        int fifth = jsonObject.getInt("amplas_relawan");
                        RelativeLayout.LayoutParams layoutParams5 = (RelativeLayout.LayoutParams)amplas.getLayoutParams();
                        layoutParams5.height = dpToPx(25);
                        layoutParams5.width = dpToPx(fifth);
                        amplas.setLayoutParams(layoutParams5);
                        total5.setText(sum.getString("amplas"));

                        int sixth = jsonObject.getInt("area_relawan");
                        RelativeLayout.LayoutParams layoutParams6 = (RelativeLayout.LayoutParams)area.getLayoutParams();
                        layoutParams6.height = dpToPx(25);
                        layoutParams6.width = dpToPx(sixth);
                        area.setLayoutParams(layoutParams6);
                        total6.setText(sum.getString("area"));

                        int seventh = jsonObject.getInt("marelan_relawan");
                        RelativeLayout.LayoutParams layoutParams7 = (RelativeLayout.LayoutParams)marelan.getLayoutParams();
                        layoutParams7.height = dpToPx(25);
                        layoutParams7.width = dpToPx(seventh);
                        marelan.setLayoutParams(layoutParams7);
                        total7.setText(sum.getString("marelan"));

                        int eight = jsonObject.getInt("labuhan_relawan");
                        RelativeLayout.LayoutParams layoutParams8 = (RelativeLayout.LayoutParams)labuhan.getLayoutParams();
                        layoutParams8.height = dpToPx(25);
                        layoutParams8.width = dpToPx(eight);
                        labuhan.setLayoutParams(layoutParams8);
                        total8.setText(sum.getString("labuhan"));

                        int nineth = jsonObject.getInt("tembung_relawan");
                        RelativeLayout.LayoutParams layoutParams9 = (RelativeLayout.LayoutParams)tembung.getLayoutParams();
                        layoutParams9.height = dpToPx(25);
                        layoutParams9.width = dpToPx(nineth);
                        tembung.setLayoutParams(layoutParams9);
                        total9.setText(sum.getString("tembung"));

                        int tenth = jsonObject.getInt("perjuangan_relawan");
                        RelativeLayout.LayoutParams layoutParams10 = (RelativeLayout.LayoutParams)perjuangan.getLayoutParams();
                        layoutParams10.height = dpToPx(25);
                        layoutParams10.width = dpToPx(tenth);
                        perjuangan.setLayoutParams(layoutParams10);
                        total10.setText(sum.getString("perjuangan"));

                        int eleven = jsonObject.getInt("timur_relawan");
                        RelativeLayout.LayoutParams layoutParams11 = (RelativeLayout.LayoutParams)timur.getLayoutParams();
                        layoutParams11.height = dpToPx(25);
                        layoutParams11.width = dpToPx(eleven);
                        timur.setLayoutParams(layoutParams11);
                        total11.setText(sum.getString("timur"));

                        //pemenangan
                        JSONObject sumPemenangan = jsonObject.getJSONObject("pemenangan");
                        int firstPemenangan = jsonObject.getInt("kota_pemenangan");
                        RelativeLayout.LayoutParams layoutParamsPemenangan = (RelativeLayout.LayoutParams)kotaPemenangan.getLayoutParams();
                        layoutParamsPemenangan.height = dpToPx(25);
                        layoutParamsPemenangan.width = dpToPx(firstPemenangan);
                        kotaPemenangan.setLayoutParams(layoutParamsPemenangan);
                        total1Pemenangan.setText(sumPemenangan.getString("kota"));

                        int secondPemenangan = jsonObject.getInt("denai_pemenangan");
                        RelativeLayout.LayoutParams layoutParams2Pemenangan = (RelativeLayout.LayoutParams)denaiPemenangan.getLayoutParams();
                        layoutParams2Pemenangan.height = dpToPx(25);
                        layoutParams2Pemenangan.width = dpToPx(secondPemenangan);
                        denaiPemenangan.setLayoutParams(layoutParams2Pemenangan);
                        total2Pemenangan.setText(sumPemenangan.getString("denai"));

                        int thirdPemenangan = jsonObject.getInt("deli_pemenangan");
                        RelativeLayout.LayoutParams layoutParams3Pemenangan = (RelativeLayout.LayoutParams)deliPemenangan.getLayoutParams();
                        layoutParams3Pemenangan.height = dpToPx(25);
                        layoutParams3Pemenangan.width = dpToPx(thirdPemenangan);
                        deliPemenangan.setLayoutParams(layoutParams3Pemenangan);
                        total3Pemenangan.setText(sumPemenangan.getString("deli"));

                        int fourthPemenangan = jsonObject.getInt("belawan_pemenangan");
                        RelativeLayout.LayoutParams layoutParams4Pemenangan = (RelativeLayout.LayoutParams)belawanPemenangan.getLayoutParams();
                        layoutParams4Pemenangan.height = dpToPx(25);
                        layoutParams4Pemenangan.width = dpToPx(fourthPemenangan);
                        belawanPemenangan.setLayoutParams(layoutParams4Pemenangan);
                        total4Pemenangan.setText(sumPemenangan.getString("belawan"));

                        int fifthPemenangan = jsonObject.getInt("amplas_pemenangan");
                        RelativeLayout.LayoutParams layoutParams5Pemenangan = (RelativeLayout.LayoutParams)amplasPemenangan.getLayoutParams();
                        layoutParams5Pemenangan.height = dpToPx(25);
                        layoutParams5Pemenangan.width = dpToPx(fifthPemenangan);
                        amplasPemenangan.setLayoutParams(layoutParams5Pemenangan);
                        total5Pemenangan.setText(sumPemenangan.getString("amplas"));

                        int sixthPemenangan = jsonObject.getInt("area_pemenangan");
                        RelativeLayout.LayoutParams layoutParams6Pemenangan = (RelativeLayout.LayoutParams)areaPemenangan.getLayoutParams();
                        layoutParams6Pemenangan.height = dpToPx(25);
                        layoutParams6Pemenangan.width = dpToPx(sixthPemenangan);
                        areaPemenangan.setLayoutParams(layoutParams6Pemenangan);
                        total6Pemenangan.setText(sumPemenangan.getString("area"));

                        int seventhPemenangan = jsonObject.getInt("marelan_pemenangan");
                        RelativeLayout.LayoutParams layoutParams7Pemenangan = (RelativeLayout.LayoutParams)marelanPemenangan.getLayoutParams();
                        layoutParams7Pemenangan.height = dpToPx(25);
                        layoutParams7Pemenangan.width = dpToPx(seventhPemenangan);
                        marelanPemenangan.setLayoutParams(layoutParams7Pemenangan);
                        total7Pemenangan.setText(sumPemenangan.getString("marelan"));

                        int eighthPemenangan = jsonObject.getInt("labuhan_pemenangan");
                        RelativeLayout.LayoutParams layoutParams8Pemenangan = (RelativeLayout.LayoutParams)labuhanPemenangan.getLayoutParams();
                        layoutParams8Pemenangan.height = dpToPx(25);
                        layoutParams8Pemenangan.width = dpToPx(eighthPemenangan);
                        labuhanPemenangan.setLayoutParams(layoutParams8Pemenangan);
                        total8Pemenangan.setText(sumPemenangan.getString("labuhan"));

                        int ninethPemenangan = jsonObject.getInt("tembung_pemenangan");
                        RelativeLayout.LayoutParams layoutParams9Pemenangan = (RelativeLayout.LayoutParams)tembungPemenangan.getLayoutParams();
                        layoutParams9Pemenangan.height = dpToPx(25);
                        layoutParams9Pemenangan.width = dpToPx(ninethPemenangan);
                        tembungPemenangan.setLayoutParams(layoutParams9Pemenangan);
                        total9Pemenangan.setText(sumPemenangan.getString("tembung"));

                        int tenthPemenangan = jsonObject.getInt("perjuangan_pemenangan");
                        RelativeLayout.LayoutParams layoutParams10Pemenangan = (RelativeLayout.LayoutParams)perjuanganPemenangan.getLayoutParams();
                        layoutParams10Pemenangan.height = dpToPx(25);
                        layoutParams10Pemenangan.width = dpToPx(tenthPemenangan);
                        perjuanganPemenangan.setLayoutParams(layoutParams10Pemenangan);
                        total10Pemenangan.setText(sumPemenangan.getString("perjuangan"));

                        int elevenPemenangan = jsonObject.getInt("timur_pemenangan");
                        RelativeLayout.LayoutParams layoutParams11Pemenangan = (RelativeLayout.LayoutParams)timurPemenangan.getLayoutParams();
                        layoutParams11Pemenangan.height = dpToPx(25);
                        layoutParams11Pemenangan.width = dpToPx(elevenPemenangan);
                        timurPemenangan.setLayoutParams(layoutParams11Pemenangan);
                        total11Pemenangan.setText(sumPemenangan.getString("timur"));

                        //pendukung
                        JSONObject sumPendukung = jsonObject.getJSONObject("pendukung");
                        int firstPendukung = jsonObject.getInt("kota_pendukung");
                        RelativeLayout.LayoutParams layoutParamsPendukung = (RelativeLayout.LayoutParams)kotaPendukung.getLayoutParams();
                        layoutParamsPendukung.height = dpToPx(25);
                        layoutParamsPendukung.width = dpToPx(firstPendukung);
                        kotaPendukung.setLayoutParams(layoutParamsPendukung);
                        total1Pendukung.setText(sumPendukung.getString("kota"));

                        int secondPendukung = jsonObject.getInt("denai_pendukung");
                        RelativeLayout.LayoutParams layoutParams2Pendukung = (RelativeLayout.LayoutParams)denaiPendukung.getLayoutParams();
                        layoutParams2Pendukung.height = dpToPx(25);
                        layoutParams2Pendukung.width = dpToPx(secondPendukung);
                        denaiPendukung.setLayoutParams(layoutParams2Pendukung);
                        total2Pendukung.setText(sumPendukung.getString("denai"));

                        int thirdPendukung = jsonObject.getInt("deli_pendukung");
                        RelativeLayout.LayoutParams layoutParams3Pendukung = (RelativeLayout.LayoutParams)deliPendukung.getLayoutParams();
                        layoutParams3Pendukung.height = dpToPx(25);
                        layoutParams3Pendukung.width = dpToPx(thirdPendukung);
                        deliPendukung.setLayoutParams(layoutParams3Pendukung);
                        total3Pendukung.setText(sumPendukung.getString("deli"));

                        int fourthPendukung = jsonObject.getInt("belawan_pendukung");
                        RelativeLayout.LayoutParams layoutParams4Pendukung = (RelativeLayout.LayoutParams)belawanPendukung.getLayoutParams();
                        layoutParams4Pendukung.height = dpToPx(25);
                        layoutParams4Pendukung.width = dpToPx(fourthPendukung);
                        belawanPendukung.setLayoutParams(layoutParams4Pendukung);
                        total4Pendukung.setText(sumPendukung.getString("belawan"));

                        int fifthPendukung = jsonObject.getInt("amplas_pendukung");
                        RelativeLayout.LayoutParams layoutParams5Pendukung = (RelativeLayout.LayoutParams)amplasPendukung.getLayoutParams();
                        layoutParams5Pendukung.height = dpToPx(25);
                        layoutParams5Pendukung.width = dpToPx(fifthPendukung);
                        amplasPendukung.setLayoutParams(layoutParams5Pendukung);
                        total5Pendukung.setText(sumPendukung.getString("amplas"));

                        int sixthPendukung = jsonObject.getInt("area_pendukung");
                        RelativeLayout.LayoutParams layoutParams6Pendukung = (RelativeLayout.LayoutParams)areaPendukung.getLayoutParams();
                        layoutParams6Pendukung.height = dpToPx(25);
                        layoutParams6Pendukung.width = dpToPx(sixthPendukung);
                        areaPendukung.setLayoutParams(layoutParams6Pendukung);
                        total6Pendukung.setText(sumPendukung.getString("area"));

                        int seventhPendukung = jsonObject.getInt("marelan_pendukung");
                        RelativeLayout.LayoutParams layoutParams7Pendukung = (RelativeLayout.LayoutParams)marelanPendukung.getLayoutParams();
                        layoutParams7Pendukung.height = dpToPx(25);
                        layoutParams7Pendukung.width = dpToPx(seventhPendukung);
                        marelanPendukung.setLayoutParams(layoutParams7Pendukung);
                        total7Pendukung.setText(sumPendukung.getString("marelan"));

                        int eighthPendukung = jsonObject.getInt("labuhan_pendukung");
                        RelativeLayout.LayoutParams layoutParams8Pendukung = (RelativeLayout.LayoutParams)labuhanPendukung.getLayoutParams();
                        layoutParams8Pendukung.height = dpToPx(25);
                        layoutParams8Pendukung.width = dpToPx(eighthPendukung);
                        labuhanPendukung.setLayoutParams(layoutParams8Pendukung);
                        total8Pendukung.setText(sumPendukung.getString("labuhan"));

                        int ninethPendukung = jsonObject.getInt("tembung_pendukung");
                        RelativeLayout.LayoutParams layoutParams9Pendukung = (RelativeLayout.LayoutParams)tembungPendukung.getLayoutParams();
                        layoutParams9Pendukung.height = dpToPx(25);
                        layoutParams9Pendukung.width = dpToPx(ninethPendukung);
                        tembungPendukung.setLayoutParams(layoutParams9Pendukung);
                        total9Pendukung.setText(sumPendukung.getString("tembung"));

                        int tenthPendukung = jsonObject.getInt("perjuangan_pendukung");
                        RelativeLayout.LayoutParams layoutParams10Pendukung = (RelativeLayout.LayoutParams)perjuanganPendukung.getLayoutParams();
                        layoutParams10Pendukung.height = dpToPx(25);
                        layoutParams10Pendukung.width = dpToPx(tenthPendukung);
                        perjuanganPendukung.setLayoutParams(layoutParams10Pendukung);
                        total10Pendukung.setText(sumPendukung.getString("perjuangan"));

                        int elevenPendukung = jsonObject.getInt("timur_pendukung");
                        RelativeLayout.LayoutParams layoutParams11Pendukung = (RelativeLayout.LayoutParams)timurPendukung.getLayoutParams();
                        layoutParams11Pendukung.height = dpToPx(25);
                        layoutParams11Pendukung.width = dpToPx(elevenPendukung);
                        timurPendukung.setLayoutParams(layoutParams11Pendukung);
                        total11Pendukung.setText(sumPendukung.getString("timur"));

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
