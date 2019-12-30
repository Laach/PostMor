package com.mdhgroup2.postmor.Compose;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mdhgroup2.postmor.R;

public class Compose2HandRecyclerViewAdapter extends RecyclerView.Adapter<Compose2HandRecyclerViewAdapter.ComposeViewHolder> {
    private PhotoItem[] mDataset = new PhotoItem[0];

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ComposeViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View imageItem;
        public TextView text;
        public ImageView image;
        public ComposeViewHolder(View v) {
            super(v);
            imageItem = v;
            text = imageItem.findViewById(R.id.imageItemText);
            image = imageItem.findViewById(R.id.imageItemImage);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Compose2HandRecyclerViewAdapter() {

    }

    // Create new views (invoked by the layout manager)
    @Override
    public Compose2HandRecyclerViewAdapter.ComposeViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.compose2_image_item, parent, false);

        ComposeViewHolder vh = new ComposeViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ComposeViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.image.setImageBitmap(mDataset[position].image);
        holder.text.setText(mDataset[position].text);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public void addItem(Bitmap image, String text){
        //add a new item to the dataset (its an array, need to recreate every time)
        PhotoItem[] newDataSet = new PhotoItem[mDataset.length+1];
        for(int i = 0; i<mDataset.length; i++){
            newDataSet[i] = mDataset[i];
        }
        PhotoItem photoItem = new PhotoItem(text,image);
        newDataSet[mDataset.length] = photoItem;
        mDataset = newDataSet;

        notifyDataSetChanged();
    }
}

class PhotoItem{
    public String text;
    public Bitmap image;

    public PhotoItem(String s, Bitmap b){
        this.text = s;
        this.image = b;
    }
}