package com.mdhgroup2.postmor.Register;

import androidx.lifecycle.ViewModelProviders;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mdhgroup2.postmor.R;
import com.mdhgroup2.postmor.database.repository.AccountRepository;

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
        mViewModel = ViewModelProviders.of(getActivity()).get(RegisterViewModel.class);

        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new RegisterAdapter(this.getFragmentManager()));
        viewPager.setOffscreenPageLimit(3);

        final Button nextFragment = view.findViewById(R.id.register_next_button);
        final Button previousFragment = view.findViewById(R.id.register_prev_button);
        final Button submit = view.findViewById(R.id.register_submit_button);
        previousFragment.setVisibility(View.INVISIBLE);
        submit.setVisibility(View.INVISIBLE);

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
                    submit.setVisibility(View.INVISIBLE);
                }
                if(currentPage == 2) {
                    nextFragment.setVisibility(View.INVISIBLE);
                    submit.setVisibility(View.VISIBLE);
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
                    submit.setVisibility(View.VISIBLE);
                }
                viewPager.setCurrentItem(currentPage,true);
            }
        });

        previousFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage--;
                submit.setVisibility(View.INVISIBLE);
                nextFragment.setVisibility(View.VISIBLE);
                if(currentPage == 0) {
                    previousFragment.setVisibility(View.INVISIBLE);
                }
                viewPager.setCurrentItem(currentPage, true);
            }
        });

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AccountRepository.PasswordStatus status = mViewModel.checkPasswordValidity();
                if(status == AccountRepository.PasswordStatus.Ok){
                    String validity = mViewModel.validateAccountInformation();
                    if(validity.equals("True")){
                        mViewModel.register();
                    }
                    else {
                        Toast toast = Toast.makeText(getContext(), validity, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else {
                    Toast toast = Toast.makeText(getContext(), status.toString(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}

