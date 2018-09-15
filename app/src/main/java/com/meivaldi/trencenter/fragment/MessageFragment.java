package com.meivaldi.trencenter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.activity.KirimPesan;
import com.meivaldi.trencenter.helper.TabAdapter;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdapter adapter;

    private FloatingActionButton button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_message, container, false);
        button = rootView.findViewById(R.id.create_message);

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);

        adapter = new TabAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new InboxFragment(), "Masuk");
        adapter.addFragment(new OutboxFragment(), "Keluar");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), KirimPesan.class));
            }
        });

        return rootView;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}