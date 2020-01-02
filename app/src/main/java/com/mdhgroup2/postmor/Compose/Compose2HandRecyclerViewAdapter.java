package com.mdhgroup2.postmor.Compose;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mdhgroup2.postmor.R;

import java.util.ArrayList;

public class Compose2HandRecyclerViewAdapter extends RecyclerView.Adapter<Compose2HandRecyclerViewAdapter.ComposeViewHolder> {
    private ArrayList<PhotoItem> data;

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
        data = new ArrayList<>();
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
        Bitmap photo = data.get(position).image;
        holder.image.setImageBitmap(photo);
        String text = data.get(position).text;
        holder.text.setText(text);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }

    //Add an item to the data set
    public void addItem(Bitmap image, String text, String fileName){
        data.add(new PhotoItem(text, image, fileName));
        notifyDataSetChanged();
    }

    //Remove an item from the data set
    public void removeItem(int position){
        data.remove(position);
        notifyDataSetChanged();
    }

    //Get filename from item (used by removeFile in Compose2Handwritten)
    public String getFileName(int position){
        return data.get(position).fileName;
    }
}

//Custom datatype for the data set containing the image and the image name
class PhotoItem{
    public String text;
    public Bitmap image;
    public String fileName;

    public PhotoItem(String s, Bitmap b, String f){
        this.text = s;
        this.image = b;
        this.fileName = s;
    }
}