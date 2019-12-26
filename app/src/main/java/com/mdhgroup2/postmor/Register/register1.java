package com.mdhgroup2.postmor.Register;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mdhgroup2.postmor.R;

public class register1 extends Fragment {

    private Register1ViewModel mViewModel;
    private static final String KEY_POSITION = "position";

    public static register1 newInstance(int position) {
        register1 reg = new register1();
        Bundle args = new Bundle();

        args.putInt(KEY_POSITION, position);
        reg.setArguments(args);

        return(reg);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register1_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(Register1ViewModel.class);
        // TODO: Use the ViewModel
    }

}
