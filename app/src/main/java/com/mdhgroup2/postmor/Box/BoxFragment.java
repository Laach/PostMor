package com.mdhgroup2.postmor.Box;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.mdhgroup2.postmor.R;

public class BoxFragment extends Fragment {

    private BoxViewModel mViewModel;

    public static BoxFragment newInstance() {
        return new BoxFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.box_fragment,container,false);
        ViewPager viewPager = view.findViewById(R.id.box_view_pager);
        viewPager.setAdapter(new BoxViewPagerAdapter(getContext(),this.getFragmentManager()));
        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(BoxViewModel.class);
        // TODO: Use the ViewModel
    }

}
