package com.meivaldi.trencenter.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.helper.SQLiteHandler;
import com.meivaldi.trencenter.helper.SessionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private SQLiteHandler db;
    private SessionManager session;
    private TextView hari, jam, menit, detik;
    private int[] layouts;
    private TextView[] dots;
    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private MyViewPagerAdapter myViewPagerAdapter;
    private RelativeLayout programKerja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        hari = (TextView) findViewById(R.id.hari);
        jam = (TextView) findViewById(R.id.jam);
        menit = (TextView) findViewById(R.id.menit);
        detik = (TextView) findViewById(R.id.detik);
        programKerja = (RelativeLayout) findViewById(R.id.programKerja);

        layouts = new int[]{
                R.layout.iklan1,
                R.layout.iklan2,
                R.layout.iklan3
        };

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        programKerja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProgramKerja.class));
                finish();
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        db = new SQLiteHandler(getApplicationContext());

        session = new SessionManager(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();

        String tipe = user.get("type");

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        if(tipe.equals("super_admin")){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        countDown();
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_logout){
            logoutUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
