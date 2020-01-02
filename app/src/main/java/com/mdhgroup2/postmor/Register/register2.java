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

public class register2 extends Fragment {

    private static final String KEY_POSITION = "position";

    public static register2 newInstance(int position) {
        register2 reg = new register2();
        Bundle args = new Bundle();

        args.putInt(KEY_POSITION, position);
        reg.setArguments(args);

        return(reg);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register2_fragment, container, false);
        final RegisterViewModel mViewModel = ViewModelProviders.of(getActivity()).get(RegisterViewModel.class);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
