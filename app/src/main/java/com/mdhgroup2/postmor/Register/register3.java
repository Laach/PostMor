package com.mdhgroup2.postmor.Register;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mdhgroup2.postmor.R;
import com.mdhgroup2.postmor.database.interfaces.IAccountRepository;
import com.mdhgroup2.postmor.database.repository.AccountRepository;
import com.mdhgroup2.postmor.database.repository.DatabaseClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class register3 extends Fragment {

//    private static final String KEY_POSITION = "position";
public static register3 newInstance() {
//        register1 reg = new register3();
//        Bundle args = new Bundle();
//        args.putInt(KEY_POSITION, position);
//        reg.setArguments(args);
//
//        return(reg);
    return new register3();
}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.register3_fragment, container, false);
        final RegisterViewModel mViewModel = ViewModelProviders.of(getActivity()).get(RegisterViewModel.class);
        final List<TextView> register_password_hints = new ArrayList<TextView>();
        register_password_hints.add((TextView)(view.findViewById(R.id.register_password_hint1)));
        register_password_hints.add((TextView)(view.findViewById(R.id.register_password_hint2)));
        register_password_hints.add((TextView)(view.findViewById(R.id.register_password_hint3)));
        register_password_hints.add((TextView)(view.findViewById(R.id.register_password_hint4)));
        register_password_hints.add((TextView)(view.findViewById(R.id.register_password_hint5)));
        /* Checks if a submit has gone through as intended. */
        mViewModel.getResults().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                String temp = strings.get(0);
                if(temp.equals("Ok")){
                    Navigation.findNavController(view).navigate(register3Directions.actionRegister3ToHomeFragment());
                }
                else {
                    Toast toast = Toast.makeText(getContext(), temp, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        final EditText passwordInput = view.findViewById(R.id.register_password_input);
        final EditText passwordConfirm = view.findViewById(R.id.register_password_confirm_input);
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mViewModel.setAccountPassword(editable.toString());
                mViewModel.checkPasswordValidity(register_password_hints);

            }
        });

        passwordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mViewModel.setAccountConfirmPassword(editable.toString());
                mViewModel.checkPasswordValidity(register_password_hints);
            }
        });

        Button previous = view.findViewById(R.id.register_prev_button);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View onView) {
                Navigation.findNavController(view).navigate(register3Directions.actionRegister3ToRegister2());
            }
        });

        Button submit = view.findViewById(R.id.register_submit_button);
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View onView){
                AccountRepository.PasswordStatus status = mViewModel.checkPasswordValidity(register_password_hints);
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
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
