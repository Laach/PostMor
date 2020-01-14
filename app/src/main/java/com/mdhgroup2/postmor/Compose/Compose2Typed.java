package com.mdhgroup2.postmor.Compose;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mdhgroup2.postmor.MainActivityViewModel;
import com.mdhgroup2.postmor.R;

public class Compose2Typed extends Fragment {

    private Compose2TypedViewModel mViewModel;
    private MainActivityViewModel mainVM;

    public static Compose2Typed newInstance() {
        return new Compose2Typed();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compose2_typed_fragment, container, false);
    }


    @Override
    public void onDestroy() {


        super.onDestroy();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(Compose2TypedViewModel.class);
        mainVM = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        // TODO: Use the ViewModel
    }

}
