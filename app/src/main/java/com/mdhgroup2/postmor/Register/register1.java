package com.mdhgroup2.postmor.Register;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mdhgroup2.postmor.R;

import java.util.List;

public class register1 extends Fragment {

//    private static final String KEY_POSITION = "position";

    public static register1 newInstance() {
//        register1 reg = new register1();
//        Bundle args = new Bundle();
//        args.putInt(KEY_POSITION, position);
//        reg.setArguments(args);
//
//        return(reg);
        return new register1();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.register1_fragment, container, false);
        final RegisterViewModel mViewModel = ViewModelProviders.of(getActivity()).get(RegisterViewModel.class);

        final TextView address = view.findViewById(R.id.register_address);
        String choosenAddress = mViewModel.getAddress();
        if(choosenAddress != null){
            address.setText(choosenAddress);
        }
        Button regenerateAddress = view.findViewById(R.id.register_regenerate_button);
        regenerateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.generateAddresses();
                String addresses = mViewModel.getAddress();
                mViewModel.choosenAddress = addresses;
                address.setText(addresses);
            }
        });

        EditText emailInput = view.findViewById(R.id.register_email_input);
        String email = mViewModel.getAccountEmail();
        if(email != null){
            emailInput.setText(email);
        }
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mViewModel.setAccountEmail(editable.toString());
            }
        });

        EditText nameInput = view.findViewById(R.id.register_name_input);
        String name = mViewModel.getAccountName();
        if(name != null){
            nameInput.setText(name);
        }
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mViewModel.setAccoutName(editable.toString());
            }
        });

        Button next = view.findViewById(R.id.register_next_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View onView) {
                Navigation.findNavController(view).navigate(register1Directions.actionRegister1ToRegister2());
            }
        });

        Button back = view.findViewById(R.id.register_back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View onView) {
                Navigation.findNavController(view).navigate(register1Directions.actionRegister1ToSignInFragment());
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
