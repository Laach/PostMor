package com.mdhgroup2.postmor.UserToUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mdhgroup2.postmor.MainActivityViewModel;
import com.mdhgroup2.postmor.R;
import com.mdhgroup2.postmor.database.DTO.Contact;

public class UserToUserFragmentTablet extends Fragment {

    public int id;
    public static UserToUserFragmentTablet newInstance() {
        return new UserToUserFragmentTablet();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        View view = inflater.inflate(R.layout.user_to_user_fragment, container, false);
        final MainActivityViewModel viewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);

        id = viewModel.selectedContact;

        Contact contact = viewModel.getContact(id);

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
