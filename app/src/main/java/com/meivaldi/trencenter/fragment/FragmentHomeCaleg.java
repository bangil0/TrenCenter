package com.meivaldi.trencenter.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.adapter.LogisticReportAdapter;
import com.meivaldi.trencenter.app.AppConfig;
import com.meivaldi.trencenter.app.AppController;
import com.meivaldi.trencenter.model.LogisticReport;

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
    private GraphView pemenanganView, relawanView, pendukungView;
    private Calendar calendar;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private static String TAG = FragmentHomeCaleg.class.getSimpleName();

    private LogisticReportAdapter adapter;
    private List<LogisticReport> list;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_caleg, container, false);

        hari = (TextView) view.findViewById(R.id.hari);
        jam = (TextView) view.findViewById(R.id.jam);
        menit = (TextView) view.findViewById(R.id.menit);
        detik = (TextView) view.findViewById(R.id.detik);
        pemenangan = (TextView) view.findViewById(R.id.totalPemenangan);
        relawan = (TextView) view.findViewById(R.id.totalRelawan);
        pendukung = (TextView) view.findViewById(R.id.totalPendukung);

        listView = (ListView) view.findViewById(R.id.logistikList);
        pemenanganView = (GraphView) view.findViewById(R.id.grafikPemenangan);
        relawanView = (GraphView) view.findViewById(R.id.grafikRelawan);
        pendukungView = (GraphView) view.findViewById(R.id.grafikPendukung);
        calendar = Calendar.getInstance();

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        list = new ArrayList<>();

        getSummaries();
        getCharts(dateFormat.format(calendar.getTime()));

        countDown();

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

                        adapter = new LogisticReportAdapter(getContext(), list);
                        listView.setAdapter(adapter);
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
                        JSONArray pemenangan = jsonObject.getJSONArray("pemenangan");
                        JSONArray relawan = jsonObject.getJSONArray("relawan");
                        JSONArray pendukung = jsonObject.getJSONArray("pendukung");

                        calendar.add(Calendar.DATE, -6);
                        Date d1 = calendar.getTime();
                        calendar.add(Calendar.DATE, 1);
                        Date d2 = calendar.getTime();
                        calendar.add(Calendar.DATE, 1);
                        Date d3 = calendar.getTime();
                        calendar.add(Calendar.DATE, 1);
                        Date d4 = calendar.getTime();
                        calendar.add(Calendar.DATE, 1);
                        Date d5 = calendar.getTime();
                        calendar.add(Calendar.DATE, 1);
                        Date d6 = calendar.getTime();
                        calendar.add(Calendar.DATE, 1);
                        Date d7 = calendar.getTime();

                        LineGraphSeries<DataPoint> pemenanganChart = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(d1, pemenangan.getInt(0)),
                                new DataPoint(d2, pemenangan.getInt(1)),
                                new DataPoint(d3, pemenangan.getInt(2)),
                                new DataPoint(d4, pemenangan.getInt(3)),
                                new DataPoint(d5, pemenangan.getInt(4)),
                                new DataPoint(d6, pemenangan.getInt(5)),
                                new DataPoint(d7, pemenangan.getInt(6))
                        });

                        LineGraphSeries<DataPoint> relawanChart = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(d1, relawan.getInt(0)),
                                new DataPoint(d2, relawan.getInt(1)),
                                new DataPoint(d3, relawan.getInt(2)),
                                new DataPoint(d4, relawan.getInt(3)),
                                new DataPoint(d5, relawan.getInt(4)),
                                new DataPoint(d6, relawan.getInt(5)),
                                new DataPoint(d7, relawan.getInt(6))
                        });

                        LineGraphSeries<DataPoint> pendukungChart = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(d1, pendukung.getInt(0)),
                                new DataPoint(d2, pendukung.getInt(1)),
                                new DataPoint(d3, pendukung.getInt(2)),
                                new DataPoint(d4, pendukung.getInt(3)),
                                new DataPoint(d5, pendukung.getInt(4)),
                                new DataPoint(d6, pendukung.getInt(5)),
                                new DataPoint(d7, pendukung.getInt(6))
                        });

                        pemenanganView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
                        pemenanganView.getGridLabelRenderer().setNumHorizontalLabels(5);
                        pemenanganView.getViewport().setMinX(d1.getTime());
                        pemenanganView.getViewport().setMaxX(d7.getTime());
                        pemenanganView.getViewport().setXAxisBoundsManual(true);
                        pemenanganView.getGridLabelRenderer().setHumanRounding(false);
                        pemenanganView.addSeries(pemenanganChart);

                        relawanView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
                        relawanView.getGridLabelRenderer().setNumHorizontalLabels(2);
                        relawanView.getViewport().setMinX(d1.getTime());
                        relawanView.getViewport().setMaxX(d7.getTime());
                        relawanView.getViewport().setXAxisBoundsManual(true);
                        relawanView.getGridLabelRenderer().setHumanRounding(false);
                        relawanView.addSeries(relawanChart);

                        pendukungView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
                        pendukungView.getGridLabelRenderer().setNumHorizontalLabels(3);
                        pendukungView.getViewport().setMinX(d1.getTime());
                        pendukungView.getViewport().setMaxX(d7.getTime());
                        pendukungView.getViewport().setXAxisBoundsManual(true);
                        pendukungView.getGridLabelRenderer().setHumanRounding(false);
                        pendukungView.addSeries(pendukungChart);
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

}
