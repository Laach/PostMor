package com.mdhgroup2.postmor.UserToUser;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdhgroup2.postmor.Contacts.ContactsViewModel;
import com.mdhgroup2.postmor.R;
import com.mdhgroup2.postmor.database.DTO.Contact;

import java.util.List;

public class UserToUserFragment extends Fragment {

    public int index = 0;

    public static UserToUserFragment newInstance() {
        return new UserToUserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_to_user_fragment, container, false);
        ContactsViewModel viewModel = ViewModelProviders.of(this).get(ContactsViewModel.class);
        index = getArguments().getInt("index");
        Contact contact = viewModel.getContact(index);
        TextView tv = view.findViewById(R.id.cardName);
        tv.setText(contact.Name);
        tv = view.findViewById(R.id.cardAddress);
        tv.setText(contact.Address);
        ImageView iv = view.findViewById(R.id.cardImageView);
        iv.setImageBitmap(contact.Picture);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
