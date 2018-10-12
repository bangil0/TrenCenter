package com.bmc.trencenter.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bmc.trencenter.fragment.InboxFragment;
import com.bmc.trencenter.fragment.OutboxFragment;

/**
 * Created by root on 25/08/18.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                InboxFragment inbox = new InboxFragment();
                return inbox;
            case 1:
                OutboxFragment outbox = new OutboxFragment();
                return outbox;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
