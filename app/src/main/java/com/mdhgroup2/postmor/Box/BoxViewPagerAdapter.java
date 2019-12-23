package com.mdhgroup2.postmor.Box;

import android.content.Context;

import com.mdhgroup2.postmor.R;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class BoxViewPagerAdapter extends FragmentStatePagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.box_tab_all, R.string.box_tab_inbox, R.string.box_tab_outbox};
    private final Context mContext;

    public BoxViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return BoxAllFragment.newInstance(position + 1);
        }
        else if (position == 1){
            return BoxInboxFragment.newInstance(position + 1);
        }
        return BoxOutboxFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
