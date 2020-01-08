package com.mdhgroup2.postmor.Compose;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.mdhgroup2.postmor.MainActivityViewModel;
import com.mdhgroup2.postmor.R;
import com.mdhgroup2.postmor.database.DTO.Contact;
import com.mdhgroup2.postmor.database.DTO.EditMsg;

public class Compose2Typed extends Fragment {

    private Compose2TypedViewModel mViewModel;
    private MainActivityViewModel mainVM;
    private TextInputEditText inputEditText;
    private Button sendButton;

    public static Compose2Typed newInstance() {
        return new Compose2Typed();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.compose2_typed_fragment, container, false);

        mViewModel = ViewModelProviders.of(this).get(Compose2TypedViewModel.class);
        mainVM = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);

        inputEditText = view.findViewById(R.id.c2typed_textInputEditText);
        sendButton = view.findViewById(R.id.c2hand_Send);


        // Find the recipient ID from the recipient fragment
        final Integer recipientID = null;
        final Contact recipient = mainVM.getChosenRecipient();

        // Observe livedata for draft
        mViewModel.getDraftMsg().observe(this, new Observer<EditMsg>() {
            @Override
            public void onChanged(EditMsg editMsg) {
                // Update the ui
                if(editMsg.Text != null){
                    inputEditText.setText(editMsg.Text);
                }
            }
        });

        // If compose is reopened from choosing contact,this will return false
        // and not get a new draft.
        if(mViewModel.isFirstTimeOpened){
            if(recipient == null) {// If no recipient has been chosen, send null
                mViewModel.getDraft(0);
                mViewModel.isFirstTimeOpened = false;
            }else{// Else send id
                mViewModel.getDraft(recipient.UserID);
                mViewModel.isFirstTimeOpened = false;
            }
        }

        mainVM.getChosenRec().observe(this, new Observer<Contact>() {
            @Override
            public void onChanged(Contact contact) {
                if(contact != null) {
                    mViewModel.changeRecipient(contact.UserID);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        mViewModel.addText(inputEditText.getText().toString());
        mViewModel.saveDraft();
        mainVM.removeRecipient();
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

}
