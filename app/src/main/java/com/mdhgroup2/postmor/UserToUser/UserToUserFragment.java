package com.mdhgroup2.postmor.UserToUser;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mdhgroup2.postmor.Contacts.ContactsViewModel;
import com.mdhgroup2.postmor.MainActivityViewModel;
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
        final MainActivityViewModel viewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);

        index = getArguments().getInt("index");
        Contact contact = viewModel.getContact(index);

        TextView tv = view.findViewById(R.id.cardName);
        tv.setText(contact.Name);

        tv = view.findViewById(R.id.cardAddress);
        tv.setText(contact.Address);

        ImageView iv = view.findViewById(R.id.cardImageView);
        iv.setImageBitmap(contact.Picture);

        ImageButton remove = view.findViewById(R.id.imageButton);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = viewModel.getContact(index);

                if (viewModel.removeContact(contact)){
                    Toast.makeText(view.getContext(),String.format("%s was successfully removed", contact.Name), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(view.getContext(),String.format("%s was NOT removed", contact.Name), Toast.LENGTH_SHORT).show();
                }

                Navigation.findNavController(view).navigateUp();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
