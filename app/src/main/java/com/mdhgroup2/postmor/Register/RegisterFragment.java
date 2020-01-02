package com.mdhgroup2.postmor.Register;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mdhgroup2.postmor.R;

public class RegisterFragment extends Fragment {

    private RegisterViewModel mViewModel;
    private ViewPager viewPager;
    private int currentPage = 0;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new RegisterAdapter(this.getFragmentManager()));

        final Button nextFragment = view.findViewById(R.id.register_next_button);
        final Button previousFragment = view.findViewById(R.id.register_prev_button);
        previousFragment.setVisibility(View.INVISIBLE);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                if(currentPage > 0 && currentPage < 2){
                    nextFragment.setVisibility(View.VISIBLE);
                    previousFragment.setVisibility(View.VISIBLE);
                }
                if(currentPage == 2) {
                    nextFragment.setVisibility(View.INVISIBLE);
                }
                if(currentPage == 0) {
                    previousFragment.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        nextFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage++;
                previousFragment.setVisibility(View.VISIBLE);
                if(currentPage == 2) {
                    nextFragment.setVisibility(View.INVISIBLE);
                }
                viewPager.setCurrentItem(currentPage,true);
            }
        });

        previousFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage--;
                nextFragment.setVisibility(View.VISIBLE);
                if(currentPage == 0) {
                    previousFragment.setVisibility(View.INVISIBLE);
                }
                viewPager.setCurrentItem(currentPage, true);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
    }

}
