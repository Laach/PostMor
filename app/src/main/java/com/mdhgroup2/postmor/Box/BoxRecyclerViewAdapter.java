package com.mdhgroup2.postmor.Box;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.mdhgroup2.postmor.database.DTO.MsgCard;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.mdhgroup2.postmor.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Date;


class BoxRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<MsgCard> messageDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public BoxRecyclerViewAdapter(List<MsgCard> messages) {

        messageDataset = messages;
        Collections.sort(messageDataset, new Comparator<MsgCard>() {
            @Override
            public int compare(MsgCard u1, MsgCard u2) {
                return u2.DateStamp.compareTo(u1.DateStamp);
            }
        });
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class BoxItemsViewHolder extends RecyclerView.ViewHolder {
        public View messageItem;
        public TextView name;
        public TextView address;
        public TextView date;
        public TextView toOrFrom;
        public ImageView toOrFromPicture;
        public ImageView profilePicture;
        public Button sendButton;
        public ImageButton expandButton;
        public View expandableContent;

        public TextView contentText;
        public ImageView contentImage1;
        public ImageView contentImage2;
        public ImageView contentImage3;


        public BoxItemsViewHolder(View mi) {
            super(mi);
            messageItem = mi;
            name = messageItem.findViewById(R.id.addressTextView);
            address = messageItem.findViewById(R.id.nameTextView);
            date = messageItem.findViewById(R.id.dateTextView);
            toOrFrom = messageItem.findViewById(R.id.toOrFrom);
            toOrFromPicture = messageItem.findViewById(R.id.toOrFromImageView);
            profilePicture = messageItem.findViewById(R.id.profilePictureImageView);
            sendButton = messageItem.findViewById(R.id.sendButton);
            expandButton = messageItem.findViewById(R.id.expandButton);
            expandableContent = messageItem.findViewById(R.id.messageContents);
            contentText = messageItem.findViewById(R.id.contentsText);
            contentImage1 = messageItem.findViewById(R.id.contentsImage1);
            contentImage2 = messageItem.findViewById(R.id.contentsImage2);
            contentImage3 = messageItem.findViewById(R.id.contentsImage3);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BoxRecyclerViewAdapter.BoxItemsViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.box_item, parent, false);

        final BoxItemsViewHolder vh = new BoxItemsViewHolder(v);


        vh.expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View contents = vh.expandableContent;

                if(contents.getVisibility() == View.VISIBLE){
                    contents.setVisibility(View.GONE);
                    vh.expandButton.setImageResource(R.drawable.ic_expand_more_black_24dp);
                }else{
                    contents.setVisibility(View.VISIBLE);
                    vh.expandButton.setImageResource(R.drawable.ic_expand_less_black_24dp);
                }
            }
        });

        vh.sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Bundle bundle = new Bundle();
                int id = messageDataset.get(vh.getAdapterPosition()).UserID;
                bundle.putInt("id", id);
                Navigation.findNavController(view).navigate(R.id.composeFragment, bundle);
            }
        });

        vh.profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                int id = messageDataset.get(vh.getAdapterPosition()).UserID;
                bundle.putInt("id", id);
                Navigation.findNavController(view).navigate(R.id.action_boxFragment_to_userToUserFragment, bundle);
            }
        });



        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final BoxItemsViewHolder cvHolder = ((BoxItemsViewHolder) holder);
        MsgCard message = messageDataset.get(position);
        cvHolder.name.setText(message.Name);

        if(message.Picture == null)
        {
            cvHolder.profilePicture.setImageResource(R.drawable.anon_profile);
        } else
        {
            Bitmap scaledPicture = Bitmap.createScaledBitmap(message.Picture, 400, 400, true);
            cvHolder.profilePicture.setImageBitmap(scaledPicture);
        }

        cvHolder.address.setText(message.Address);

        //format the date
        Date todayDate = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(message.DateStamp);
        cal.add(Calendar.HOUR, 1);
//        Date messageDate = message.DateStamp;
        Date messageDate = cal.getTime();
        long oneDay = 1000 * 60 * 60 * 24; //milliseconds in a day
        SimpleDateFormat x;
        if(todayDate.getDay() == messageDate.getDay())
        {
            x =  new SimpleDateFormat("'Today' hh:mm");
        } else if(todayDate.getYear() == messageDate.getYear())
        {
            x =  new SimpleDateFormat("E MMM d hh:mm");
        } else
        {
            x =  new SimpleDateFormat("yyyy E MMM d hh:mm");
        }
        String formattedDate = x.format(messageDate);

        cvHolder.date.setText(formattedDate);

        if (message.IsSentByMe) {
            cvHolder.toOrFrom.setText(R.string.box_to_text);
            cvHolder.toOrFromPicture.setImageResource(R.drawable.ic_sent_letter);
            cvHolder.sendButton.setVisibility(View.GONE);
        } else {
            cvHolder.toOrFrom.setText(R.string.box_from_text);
            cvHolder.toOrFromPicture.setImageResource(R.drawable.ic_recieved_letter);
            cvHolder.sendButton.setVisibility(View.VISIBLE);
            cvHolder.sendButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Bundle bundle = new Bundle();
                    int id = messageDataset.get(position).UserID;
                    bundle.putInt("id", id);
                    Navigation.findNavController(view).navigate(R.id.composeFragment, bundle);

                }
            });
        }

        // Populate the expandable message contents
        cvHolder.expandableContent.setVisibility(View.GONE);
        cvHolder.expandButton.setImageResource(R.drawable.ic_expand_more_black_24dp);
        if(message.Text == null || message.Text.isEmpty())
        {
            //This is a picture message
            cvHolder.contentText.setVisibility(View.GONE);
            if(message.Images.size() >= 1)
            {
                cvHolder.contentImage1.setImageBitmap(message.Images.get(0));
                cvHolder.contentImage1.setVisibility(View.VISIBLE);

            }
            if(message.Images.size() >= 2)
            {
                cvHolder.contentImage2.setImageBitmap(message.Images.get(1));
                cvHolder.contentImage2.setVisibility(View.VISIBLE);

            }
            if(message.Images.size() >= 3)
            {
                cvHolder.contentImage3.setImageBitmap(message.Images.get(2));
                cvHolder.contentImage3.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            //This is a text message
            cvHolder.contentText.setVisibility(View.VISIBLE);
            cvHolder.contentText.setText(message.Text);
            cvHolder.contentImage1.setVisibility(View.GONE);
            cvHolder.contentImage2.setVisibility(View.GONE);
            cvHolder.contentImage3.setVisibility(View.GONE);
        }


        cvHolder.expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View contents = cvHolder.expandableContent;
                if(contents.getVisibility() == View.VISIBLE)
                {
                    contents.setVisibility(View.GONE);
                    cvHolder.expandButton.setImageResource(R.drawable.ic_expand_more_black_24dp);
                } else
                {
                    contents.setVisibility(View.VISIBLE);
                    cvHolder.expandButton.setImageResource(R.drawable.ic_expand_less_black_24dp);
                }
            }
        });

        cvHolder.profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                int id = messageDataset.get(position).UserID;
                bundle.putInt("id", id);
                Navigation.findNavController(view).navigate(R.id.action_boxFragment_to_userToUserFragment, bundle);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return messageDataset.size();
    }
}
