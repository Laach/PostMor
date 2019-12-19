package com.mdhgroup2.postmor.Box;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mdhgroup2.postmor.R;

class BoxRecyclerViewAdapter extends RecyclerView.Adapter {

    // Provide a suitable constructor (depends on the kind of dataset)
    public BoxRecyclerViewAdapter() {
    }

    private String[] mDataset = {"Emil", "Alexander", "Philip", "Nick", "Casper", "Emil", "Alexander", "Philip", "Nick", "Casper", "Emil", "Alexander", "Philip", "Nick", "Casper", "Emil", "Alexander", "Philip", "Nick", "Casper", "Emil", "Alexander", "Philip", "Nick", "Casper", "Emil", "Alexander", "Philip", "Nick", "Casper"};;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class BoxItemsViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View messageItem;
        public TextView name;
        public TextView address;
        public TextView date;
        public TextView toOrFrom;
        public ImageView toOrFromPicture;
        public ImageView profilePicture;

        public BoxItemsViewHolder(View mi) {
            super(mi);
            messageItem = mi;
            name = messageItem.findViewById(R.id.nameTextView);
            address = messageItem.findViewById(R.id.addressTextView);
            date = messageItem.findViewById(R.id.dateTextView);
            toOrFrom = messageItem.findViewById(R.id.toOrFrom);
            toOrFromPicture = messageItem.findViewById(R.id.toOrFromImageView);
            profilePicture = messageItem.findViewById(R.id.profilePictureImageView);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BoxRecyclerViewAdapter.BoxItemsViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.box_item, parent, false);

        BoxItemsViewHolder vh = new BoxItemsViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        BoxItemsViewHolder cvHolder = ((BoxItemsViewHolder) holder);
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
