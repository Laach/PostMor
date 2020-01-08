package com.mdhgroup2.postmor.UserToUser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
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

import com.mdhgroup2.postmor.Box.BoxContentFragment;
import com.mdhgroup2.postmor.MainActivityViewModel;
import com.mdhgroup2.postmor.R;
import com.mdhgroup2.postmor.database.DTO.Contact;

public class UserToUserFragment extends Fragment {

    public int id = 0;
    public static UserToUserFragment newInstance() {
        return new UserToUserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        final View view = inflater.inflate(R.layout.user_to_user_fragment, container, false);
        final MainActivityViewModel viewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        
        id = getArguments().getInt("id");
        Contact contact = viewModel.getContactById(id);

        TextView tv = view.findViewById(R.id.cardName);
        tv.setText(contact.Name);

        tv = view.findViewById(R.id.cardAddress);
        tv.setText(contact.Address);

        ImageView iv = view.findViewById(R.id.cardImageView);
        iv.setImageBitmap(contact.Picture);

        Button bt = view.findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                Navigation.findNavController(view).navigate(R.id.action_userToUserFragment_to_composeFragment, bundle);
            }
        });

        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Contact contact = viewModel.getContactById(id);
                        if (viewModel.removeContact(contact)){
                            Toast.makeText(view.getContext(),String.format("%s was successfully removed", contact.Name), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(view.getContext(),String.format("%s was NOT removed", contact.Name), Toast.LENGTH_SHORT).show();
                        }
                        Navigation.findNavController(view).navigateUp();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        Button remove = view.findViewById(R.id.removeImageButton);
        if(contact.IsFriend){
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Are you sure you want to remove this contact?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener )
                            .show();

                }
            });
        }
        else{
            remove.setText(R.string.found_user_add_button);
            remove.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_add_black_24dp, 0, 0, 0);
//            remove.setBackgroundResource(R.drawable.ic_add_black_24dp);
            remove.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Contact contact = viewModel.getContactById(id);
                    if (viewModel.addUserToContacts(contact)){
                        Toast.makeText(view.getContext(),String.format("%s was successfully added", contact.Name), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(view.getContext(),String.format("%s was NOT added", contact.Name), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        // Begin the transaction
       FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.boxContainer, new BoxContentFragment(1, id));
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
