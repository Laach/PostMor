package com.mdhgroup2.postmor.Settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

        builder.setView(dialogView)
                .setMessage("Change Password")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mViewModel.checkOldPassword(oldPassword.getText().toString())){

                        }
                        Toast toast = Toast.makeText(getContext(),"Old password was incorrect.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PasswordDialogFragment.this.getDialog().cancel();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();

        return dialog;
    }
}
