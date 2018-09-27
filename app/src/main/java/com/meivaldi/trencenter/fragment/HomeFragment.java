package com.meivaldi.trencenter.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.activity.DaftarPendukung;
import com.meivaldi.trencenter.activity.DaftarRelawan;
import com.meivaldi.trencenter.activity.LogistikActivity;
import com.meivaldi.trencenter.activity.ProgramKerja;
import com.meivaldi.trencenter.activity.caleg.DataCaleg;
import com.meivaldi.trencenter.activity.caleg.DetailCaleg;
import com.meivaldi.trencenter.activity.pendukung.InputPendukung;
import com.meivaldi.trencenter.activity.relawan.InputRelawan;
import com.meivaldi.trencenter.activity.tim_pemenangan.InputTimPemenangan;
import com.meivaldi.trencenter.activity.tim_pemenangan.ProgramKerja_TimPemenangan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private TextView hari, detik, menit, jam;
    private RelativeLayout one, two, three, four, five, six, seven, eight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        one = (RelativeLayout) rootView.findViewById(R.id.layout_one);
        two = (RelativeLayout) rootView.findViewById(R.id.layout_two);
        three = (RelativeLayout) rootView.findViewById(R.id.layout_three);
        four = (RelativeLayout) rootView.findViewById(R.id.layout_four);
        five = (RelativeLayout) rootView.findViewById(R.id.layout_five);
        six = (RelativeLayout) rootView.findViewById(R.id.layout_six);
        seven = (RelativeLayout) rootView.findViewById(R.id.layout_seven);
        eight = (RelativeLayout) rootView.findViewById(R.id.layout_eight);

        hari = (TextView) rootView.findViewById(R.id.hari);
        jam = (TextView) rootView.findViewById(R.id.jam);
        menit = (TextView) rootView.findViewById(R.id.menit);
        detik = (TextView) rootView.findViewById(R.id.detik);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);

        countDown();

        rootView.setVisibility(View.VISIBLE);
        rootView.animate()
                .alpha(1f)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .setListener(null);

        return rootView;
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

        new CountDownTimer(diffSeconds * 1000, 1000){
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_one:
                startActivity(new Intent(getContext(), DetailCaleg.class));

                return;
            case R.id.layout_two:
                startActivity(new Intent(getContext(), InputTimPemenangan.class));

                return;
            case R.id.layout_three:
                startActivity(new Intent(getContext(), InputRelawan.class));

                return;
            case R.id.layout_four:
                startActivity(new Intent(getContext(), InputPendukung.class));

                return;
            case R.id.layout_five:
                startActivity(new Intent(getContext(), LogistikActivity.class));

                return;
            case R.id.layout_six:
                startActivity(new Intent(getContext(), ProgramKerja_TimPemenangan.class));

                return;
            case R.id.layout_seven:
                startActivity(new Intent(getContext(), DaftarRelawan.class));

                return;
            case R.id.layout_eight:
                startActivity(new Intent(getContext(), DaftarPendukung.class));

                return;
        }
    }

}
