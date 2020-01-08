package com.mdhgroup2.postmor.Contacts;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.mdhgroup2.postmor.R;
import com.mdhgroup2.postmor.database.DTO.Contact;

import java.util.List;

class ContactsAdapterTablet extends RecyclerView.Adapter {

    static List<Contact> contacts;
    int screenWidthDp;
    private int highlightedPosition = 0;
    View contactCard;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactsAdapterTablet(List<Contact> list, int screenWidthDp, View contactCard) {
        contacts = list;
        this.screenWidthDp = screenWidthDp;
        this.contactCard = contactCard;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ContactsViewHolderTablet extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public View contactItem;
        public TextView name;
        public TextView address;
        public ImageView profilePicture;


        public View contactCard = null;

        public int id;
        public int screenWidthDp;

        public ContactsViewHolderTablet(View ci, int screenWidthDp) {
            super(ci);
            this.screenWidthDp = screenWidthDp;
            contactItem = ci;
            name = contactItem.findViewById(R.id.nameTextView);
            address = contactItem.findViewById(R.id.addressTextView);
            profilePicture = contactItem.findViewById(R.id.profilePictureCardView);
            ci.setOnClickListener(this);
        }

        public ContactsViewHolderTablet(View ci, int screenWidthDp, View contactCard) {
            this(ci, screenWidthDp);
            this.contactCard = contactCard;
        }

        @Override
        public void onClick(View view) {
            final Contact contact = contacts.get(getAdapterPosition());
            //should simply change which user is highlighted on the one side.
            TextView tv = contactCard.findViewById(R.id.cardName);
            tv.setText(contact.Name);

            tv = contactCard.findViewById(R.id.cardAddress);
            tv.setText(contact.Address);

            ImageView iv = contactCard.findViewById(R.id.cardImageView);
            iv.setImageBitmap(contact.Picture);
            if (contact.Picture == null) {
                profilePicture.setImageResource(R.mipmap.ic_launcher);
            }
            else {
                profilePicture.setImageBitmap(contact.Picture);
            }

            //Selected contact should have some visual indiccation that it is selected.
            CardView card = (CardView) view.findViewById(R.id.outerCard);
            card.setCardElevation(0);
            card.setCardBackgroundColor(Color.rgb(236, 236, 236));

        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ContactsViewHolderTablet onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        ContactsViewHolderTablet vh = new ContactsViewHolderTablet(v, screenWidthDp, contactCard);
        return vh;
    }

    private int selectedItem = 0;
    private int lastSelected = 0;
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final ContactsViewHolderTablet cvHolder = ((ContactsViewHolderTablet) holder);

        int backgroundColor = (position == selectedItem) ? R.color.colorPressedCard : R.color.colorUnpressedCard;
        CardView card = (CardView) holder.itemView.findViewById(R.id.outerCard);
        card.setBackgroundColor(ContextCompat.getColor(card.getContext(), backgroundColor));
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvHolder.onClick(view);
                lastSelected = selectedItem;
                selectedItem = position;
                notifyItemChanged(lastSelected);
                notifyItemChanged(selectedItem);

            }
        });

        cvHolder.name.setText(contacts.get(position).Name);
        cvHolder.address.setText(contacts.get(position).Address);
        cvHolder.id = contacts.get(position).UserID;

        if (contacts.get(position).Picture == null) {
            cvHolder.profilePicture.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            cvHolder.profilePicture.setImageBitmap(contacts.get(position).Picture);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public int getSelectedItem(){
        return selectedItem;
    }

    public void setSelectedItem(int si){
        selectedItem = si;
    }
}
