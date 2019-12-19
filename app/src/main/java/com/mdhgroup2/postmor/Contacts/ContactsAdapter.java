package com.mdhgroup2.postmor.Contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mdhgroup2.postmor.R;

import java.lang.reflect.Array;

class ContactsAdapter extends RecyclerView.Adapter {

    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactsAdapter() {
    }

    private String[] mDataset = {"Emil", "Alexander", "Philip", "Nick", "Casper", "Emil", "Alexander", "Philip", "Nick", "Casper", "Emil", "Alexander", "Philip", "Nick", "Casper", "Emil", "Alexander", "Philip", "Nick", "Casper", "Emil", "Alexander", "Philip", "Nick", "Casper", "Emil", "Alexander", "Philip", "Nick", "Casper"};;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ContactsViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View contactItem;
        public TextView name;
        public TextView address;
        public ImageView profilePicture;
        public ContactsViewHolder(View ci) {
            super(ci);
            contactItem = ci;
            name = contactItem.findViewById(R.id.nameTextView);
            address = contactItem.findViewById(R.id.addressTextView);
            profilePicture = contactItem.findViewById(R.id.profilePictureImageView);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ContactsAdapter.ContactsViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);

        ContactsViewHolder vh = new ContactsViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ContactsViewHolder cvHolder = ((ContactsViewHolder) holder);
        cvHolder.name.setText(mDataset[position]);
        cvHolder.profilePicture.setImageResource(R.mipmap.ic_launcher);
        return;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
