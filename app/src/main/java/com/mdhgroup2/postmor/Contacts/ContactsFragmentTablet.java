package com.mdhgroup2.postmor.Contacts;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mdhgroup2.postmor.MainActivityViewModel;
import com.mdhgroup2.postmor.R;
import com.mdhgroup2.postmor.UserToUser.UserToUserFragment;
import com.mdhgroup2.postmor.database.DTO.Contact;


public class ContactsFragmentTablet extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Contact friend = null;

    // Below are all that exist in the add popup dialog view.
    private View popupAddInputDialogView = null;
    private EditText userSearch = null;
    private Button searchUserbutton = null;
    private Button cancelUserAddButton = null;

    // Below are all that exist in the found popup dialog view.
    private View popupFoundUserView = null;
    private TextView userFoundName = null;
    private TextView userFoundAddress = null;
    private ImageView userProfilePic = null;
    private Button addUserbutton = null;
    private Button cancelUserFoundButton = null;

    public static ContactsFragmentTablet newInstance() {
        return new ContactsFragmentTablet();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        View view = inflater.inflate(R.layout.contacts_fragment, container, false);

        recyclerView = view.findViewById(R.id.contactsRecyclerView);

        layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        final MainActivityViewModel mViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);

        mViewModel.selectedContact = 0;
        final View contactCard = view.findViewById(R.id.u2ufragment);

        mAdapter = new ContactsAdapterTablet(mViewModel.getContactList(), mViewModel.screenWidthDp, contactCard);
        recyclerView.setAdapter(mAdapter);

        Button remove = view.findViewById(R.id.removeImageButton);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactsAdapterTablet ca = ((ContactsAdapterTablet)recyclerView.getAdapter());
                int selectedContact = ca.getSelectedItem();
                Contact contact = mViewModel.getContact(selectedContact);

                if (mViewModel.removeContact(contact)){
                    Toast.makeText(view.getContext(),String.format("%s was successfully removed", contact.Name), Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.action_contactsFragment_self);
                    ca.setSelectedItem(0);
                }
                else {
                    Toast.makeText(view.getContext(),String.format("%s was NOT removed", contact.Name), Toast.LENGTH_SHORT).show();
                }

            }
        });


        FloatingActionButton addContact = view.findViewById(R.id.floatingAddButton);
        addContact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Search for a user by typing in their address");
                alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
                alertDialogBuilder.setCancelable(true);

                // Init popup dialog view and it's ui controls.
                initAddPopupViewControls();

                // Set the inflated layout view object to the AlertDialog builder.
                alertDialogBuilder.setView(popupAddInputDialogView);

                // Create AlertDialog and show.
                final AlertDialog addUserDialog = alertDialogBuilder.create();
                addUserDialog.show();

                // When user click the save user data button in the popup dialog.
                searchUserbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userAddress = userSearch.getText().toString();
                        if(userAddress.isEmpty()){
                            Toast toast = Toast.makeText(getContext(), "You need to enter an address.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        if (!userAddress.isEmpty()){
                            friend = mViewModel.findUserByAddress(userAddress);
                            if (friend != null && !friend.IsFriend) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
//                                alertDialogBuilder.setTitle("User found");
//                                alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
                                alertDialogBuilder.setCancelable(true);

                                // Init popup dialog view and it's ui controls.
                                initFoundPopupViewControls(friend);

                                // Set the inflated layout view object to the AlertDialog builder.
                                alertDialogBuilder.setView(popupFoundUserView);

                                // Create the second AlertDialog and show.
                                final AlertDialog foundUserDialog = alertDialogBuilder.create();
                                foundUserDialog.show();

                                addUserbutton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mViewModel.addUserToContacts(friend);
                                        mAdapter.notifyDataSetChanged();
                                        Toast toast = Toast.makeText(getContext(), friend.Name + " has been added to your contacts.", Toast.LENGTH_SHORT);
                                        toast.show();
                                        foundUserDialog.cancel();
                                        addUserDialog.cancel();
                                    }
                                });
                                cancelUserFoundButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        foundUserDialog.cancel();
                                    }
                                });
                            }
                            else if(friend != null && friend.IsFriend){
                                Toast toast = Toast.makeText(getContext(), friend.Name + " is already in your contacts.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else{
                                Toast toast = Toast.makeText(getContext(), "That user could NOT be found.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }
                });

                cancelUserAddButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addUserDialog.cancel();
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initAddPopupViewControls()
    {
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(getParentFragment().getContext());

        // Inflate the popup dialog from a layout xml file.
        popupAddInputDialogView = layoutInflater.inflate(R.layout.add_contact_popup, null);

        // Get user input edittext and button ui controls in the popup dialog.
        userSearch = popupAddInputDialogView.findViewById(R.id.userName);
        searchUserbutton = popupAddInputDialogView.findViewById(R.id.button_search_user);
        cancelUserAddButton = popupAddInputDialogView.findViewById(R.id.button_cancel_user_add);
    }

    private void initFoundPopupViewControls(Contact uc){
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(getParentFragment().getContext());

        // Inflate the popup dialog from a layout xml file.
        popupFoundUserView = layoutInflater.inflate(R.layout.found_contact_popup, null);

        // Get user input edittext and button ui controls in the popup dialog.
        userFoundName = popupFoundUserView.findViewById(R.id.found_user_name);
        userFoundName.setText(uc.Name);
        userFoundAddress = popupFoundUserView.findViewById(R.id.found_user_address);
        userFoundAddress.setText(uc.Address);
        userProfilePic = popupFoundUserView.findViewById(R.id.found_user_profile_pic);
        userProfilePic.setImageBitmap(uc.Picture);

        addUserbutton = popupFoundUserView.findViewById(R.id.button_add_user);
        cancelUserFoundButton = popupFoundUserView.findViewById(R.id.button_cancel_user_found);
    }

}
