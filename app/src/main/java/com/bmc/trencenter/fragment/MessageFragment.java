package com.bmc.trencenter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bmc.trencenter.R;
import com.bmc.trencenter.activity.KirimPesan;
import com.bmc.trencenter.helper.TabAdapter;

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

        rootView.setAlpha(0f);
        rootView.setVisibility(View.GONE);

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

        rootView.setVisibility(View.VISIBLE);
        rootView.animate()
                .alpha(1f)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .setListener(null);

        return rootView;
    }

}