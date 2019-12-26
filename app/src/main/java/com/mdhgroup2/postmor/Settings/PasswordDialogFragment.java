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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mdhgroup2.postmor.R;

public class PasswordDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.change_password_fragment,null);

        final EditText newPassword = dialogView.findViewById(R.id.newPassword);
        final EditText confirmNewPassword = dialogView.findViewById(R.id.confirmNewPassword);
        final TextView passwordHint = dialogView.findViewById(R.id.passwordHint);

        builder.setView(dialogView)
                .setMessage("Change Password")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: check if old password is correct

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PasswordDialogFragment.this.getDialog().cancel();
            }
        });

        final AlertDialog dialog = builder.create();

        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newP = newPassword.getText().toString();
                String confP = confirmNewPassword.getText().toString();

                if (!newP.isEmpty() || !confP.isEmpty()) {
                    if(newP.equals(confP)){
                        dialog.dismiss();
                    }
                    else{
                        passwordHint.setText("Passwords are not the same!");
                    }
                }
            }
        });

        return dialog;
    }
}
