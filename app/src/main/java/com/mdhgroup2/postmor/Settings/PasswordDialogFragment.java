package com.mdhgroup2.postmor.Settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.mdhgroup2.postmor.MainActivityViewModel;
import com.mdhgroup2.postmor.R;

public class PasswordDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.change_password_fragment,null);
        final MainActivityViewModel mViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);

        final EditText oldPassword = dialogView.findViewById(R.id.oldPassword);
        final EditText newPassword = dialogView.findViewById(R.id.newPassword);
        final EditText confirmNewPassword = dialogView.findViewById(R.id.confirmNewPassword);
        final TextView passwordHint = dialogView.findViewById(R.id.passwordHint);
        final Button confirmButton = dialogView.findViewById(R.id.change_pass_confirm_button);
        final Button cancelButton = dialogView.findViewById(R.id.change_pass_cancel_button);

        builder.setView(dialogView);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(mViewModel.checkOldPassword(oldPassword.getText().toString()){
//                            if(newPassword.getText().toString().equals(confirmNewPassword.getText().toString())){
//                                mViewModel.setNewPassword(confirmNewPassword.getText().toString());
//                                Toast toast = Toast.makeText(getContext(),"Your password has been updated.", Toast.LENGTH_SHORT);
//                                toast.show();
//                                dialog.cancel();
//                            }
//                        }
                    if(newPassword.getText().toString().equals(confirmNewPassword.getText().toString()) && newPassword != null){
                        Toast toast = Toast.makeText(getContext(),"Your password has been updated.", Toast.LENGTH_SHORT);
                        toast.show();
                        PasswordDialogFragment.this.getDialog().cancel();
                    }
                    else {
                        Toast toast = Toast.makeText(getContext(), "Old password was incorrect.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordDialogFragment.this.getDialog().cancel();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();

        return dialog;
    }
}
