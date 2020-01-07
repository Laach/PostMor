package com.mdhgroup2.postmor.SignIn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mdhgroup2.postmor.R;

import java.util.List;

public class SignInFragment extends Fragment {

    private SignInViewModel mViewModel;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        final View view = inflater.inflate(R.layout.sign_in_fragment, container, false);
        mViewModel = ViewModelProviders.of(this).get(SignInViewModel.class);
        mViewModel.amILoggedIn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Boolean result = aBoolean;
                if (result){
                    Navigation.findNavController(view).navigate(R.id.homeFragment, null, new NavOptions.Builder().setPopUpTo(R.id.signInFragment,true).build());
                }
            }
        });

        mViewModel.checkLoginStatus();

        mViewModel.getResult().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> aBoolean) {
                List<String> result = aBoolean;
                if (result.get(0).equals("Ok")){
                    Navigation.findNavController(view).navigate(R.id.homeFragment, null, new NavOptions.Builder().setPopUpTo(R.id.signInFragment, true).build());
                }
                else{
                    Toast toast = Toast.makeText(getContext(), "Something went wrong." , Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        final Button signIn = view.findViewById(R.id.signIn_login_button);
        Button register = view.findViewById(R.id.signIn_register_button);

        EditText email = view.findViewById(R.id.signIn_email_input);
        EditText password = view.findViewById(R.id.signIn_password_input);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mViewModel.setEmail(editable.toString());
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mViewModel.setPassword(editable.toString());
            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View onView) {
                String validity = mViewModel.checkValidity();
                if(validity.equals("True")){
                    mViewModel.login();
                }
                else{
                    Toast toast = Toast.makeText(getContext(), "You need to enter your " + validity, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View onView) {
                Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_register1);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
