package com.meivaldi.trencenter.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.activity.ProgramKerja;
import com.meivaldi.trencenter.activity.caleg.DataCaleg;
import com.meivaldi.trencenter.activity.pendukung.InputPendukung;
import com.meivaldi.trencenter.adapter.SliderPagerAdapter;
import com.meivaldi.trencenter.helper.FragmentSlider;
import com.meivaldi.trencenter.helper.SliderIndicator;
import com.meivaldi.trencenter.helper.SliderView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentHomeRelawan.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentHomeRelawan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHomeRelawan extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView hari, detik, menit, jam;
    private RelativeLayout programKerja, profilCaleg;

    private FloatingActionButton createPendukung;

    private SliderPagerAdapter mAdapter;
    private SliderIndicator mIndicator;

    private SliderView sliderView;
    private LinearLayout mLinearLayout;

    public FragmentHomeRelawan() {
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentHomeRelawan newInstance(String param1, String param2) {
        FragmentHomeRelawan fragment = new FragmentHomeRelawan();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home_relawan, container, false);

        hari = (TextView) rootView.findViewById(R.id.hari);
        jam = (TextView) rootView.findViewById(R.id.jam);
        menit = (TextView) rootView.findViewById(R.id.menit);
        detik = (TextView) rootView.findViewById(R.id.detik);
        programKerja = (RelativeLayout) rootView.findViewById(R.id.programKerja);
        profilCaleg = (RelativeLayout) rootView.findViewById(R.id.profil_caleg);

        sliderView = (SliderView) rootView.findViewById(R.id.sliderView);
        mLinearLayout = (LinearLayout) rootView.findViewById(R.id.pagesContainer);

        setupSlider();

        programKerja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ProgramKerja.class));
            }
        });

        profilCaleg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), DataCaleg.class));
            }
        });

        createPendukung = (FloatingActionButton) rootView.findViewById(R.id.createPendukung);
        createPendukung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), InputPendukung.class));
            }
        });

        countDown();

        return rootView;
    }

    private void setupSlider() {
        sliderView.setDurationScroll(800);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FragmentSlider.newInstance("https://image.tmdb.org/t/p/w600_and_h900_bestv2/lXlCTkYRcJBReiE1ghXWPM3cdae.jpg"));
        fragments.add(FragmentSlider.newInstance("https://image.tmdb.org/t/p/w600_and_h900_bestv2/9u72dJxrEcwgJynDbPhIfWOayRM.jpg"));
        fragments.add(FragmentSlider.newInstance("https://image.tmdb.org/t/p/w250_and_h141_bestv2/biN2sqExViEh8IYSJrXlNKjpjxx.jpg"));
        fragments.add(FragmentSlider.newInstance("https://image.tmdb.org/t/p/w250_and_h141_bestv2/o9OKe3M06QMLOzTl3l6GStYtnE9.jpg"));

        mAdapter = new SliderPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
        sliderView.setAdapter(mAdapter);
        mIndicator = new SliderIndicator(getContext(), mLinearLayout, sliderView, R.drawable.indicator_circle);
        mIndicator.setPageCount(fragments.size());
        mIndicator.show();
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


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
