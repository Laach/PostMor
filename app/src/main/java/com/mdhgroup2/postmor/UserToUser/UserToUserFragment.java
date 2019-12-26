package com.mdhgroup2.postmor.UserToUser;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mdhgroup2.postmor.Contacts.ContactsViewModel;
import com.mdhgroup2.postmor.R;

public class UserToUserFragment extends Fragment {



    public static UserToUserFragment newInstance() {
        return new UserToUserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_to_user_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ContactsViewModel viewModel = ViewModelProviders.of(this).get(ContactsViewModel.class);
    }

}
