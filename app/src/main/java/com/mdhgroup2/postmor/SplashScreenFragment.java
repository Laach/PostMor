package com.mdhgroup2.postmor;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mdhgroup2.postmor.SignIn.SignInViewModel;


public class SplashScreenFragment extends Fragment {

    private SignInViewModel mViewModel;

    public SplashScreenFragment() {
    }

    public static SplashScreenFragment newInstance() {return new SplashScreenFragment();}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(SignInViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_splash_screen, container, false);


        mViewModel.amILoggedIn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Boolean result = aBoolean;
                if (result) {
                    Navigation.findNavController(view).navigate(R.id.action_splashScreenFragment_to_homeFragment);
                }
                else{
                    Navigation.findNavController(view).navigate(R.id.action_splashScreenFragment_to_signInFragment);
                }
            }
        });

        mViewModel.checkLoginStatus();

        return view;
    }
}
