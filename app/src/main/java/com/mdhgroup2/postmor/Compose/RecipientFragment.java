package com.mdhgroup2.postmor.Compose;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Bitmap;
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

public class RecipientFragment extends Fragment {

    private MainActivityViewModel mViewModel;
    private Button recipientButton;
    private TextView recipientAddress;
    private TextView recipientName;
    private ImageView recipientPicture;
    private ConstraintLayout chosenRecipientLayout;

    public static RecipientFragment newInstance() {
        return new RecipientFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.recipient_fragment, container, false);
        recipientButton = view.findViewById(R.id.recipientButton);

        recipientAddress = view.findViewById(R.id.recipientAddress);
        recipientName = view.findViewById(R.id.recpientName);
        recipientPicture = view.findViewById(R.id.recipientPicture);
        chosenRecipientLayout = view.findViewById(R.id.chosenRecipientLayout);

        recipientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromCompose", true);
                Navigation.findNavController(getView()).navigate(R.id.contactsFragment, bundle);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mViewModel.getChosenRec().observe(this, new Observer<Contact>() {
            @Override
            public void onChanged(Contact contact) {
                if(contact != null){
                    /*recipientButton.setVisibility(View.GONE);
                    recipientAddress.setVisibility(View.VISIBLE);
                    recipientName.setVisibility(View.VISIBLE);
                    recipientPicture.setVisibility(View.VISIBLE);*/

                    if(contact.Address != null){
                        recipientAddress.setText(contact.Address);
                    }else{
                        recipientAddress.setText("NULL");
                    }
                    if(contact.Name != null) {
                        recipientName.setText(contact.Name);
                    }else{
                        recipientName.setText("NULL");
                    }
                    if(contact.Picture != null){
                        recipientPicture.setImageBitmap(contact.Picture);
                    }else{

                    }

                    chosenRecipientLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("isFromCompose", true);
                            Navigation.findNavController(getView()).navigate(R.id.contactsFragment, bundle);
                        }
                    });
                }else{
                    /*
                    recipientButton.setVisibility(View.VISIBLE);
                    recipientAddress.setVisibility(View.GONE);
                    recipientName.setVisibility(View.GONE);
                    recipientPicture.setVisibility(View.GONE);*/
                }
            }
        });

        Contact recipient = mViewModel.getChosenRecipient();
        if(recipient != null){
            recipientButton.setVisibility(View.GONE);
            recipientAddress.setVisibility(View.VISIBLE);
            recipientName.setVisibility(View.VISIBLE);
            recipientPicture.setVisibility(View.VISIBLE);

            /*recipientAddress.setText(recipient.Address);
            recipientName.setText(recipient.Name);
            recipientPicture.setImageBitmap(recipient.Picture);*/

            chosenRecipientLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isFromCompose", true);
                    Navigation.findNavController(getView()).navigate(R.id.contactsFragment, bundle);
                }
            });
        }else{
            recipientButton.setVisibility(View.VISIBLE);
            recipientAddress.setVisibility(View.GONE);
            recipientName.setVisibility(View.GONE);
            recipientPicture.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);

        // TODO: Use the ViewModel
    }

}
