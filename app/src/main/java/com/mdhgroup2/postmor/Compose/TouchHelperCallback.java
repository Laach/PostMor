package com.mdhgroup2.postmor.Compose;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.mdhgroup2.postmor.R;

public class TouchHelperCallback extends ItemTouchHelper.Callback {
    private Compose2HandRecyclerViewAdapter adapter;
    private Compose2Handwritten c2h;
    private Drawable icon;
    private final ColorDrawable background;

    public TouchHelperCallback(Compose2HandRecyclerViewAdapter adapter, Context c, Compose2Handwritten c2h) {
        super();
        this.adapter = adapter;
        this.c2h = c2h;
        //Specify background color and delete icon to be used
        icon = ContextCompat.getDrawable(c, R.drawable.ic_delete_white_24dp);
        background = new ColorDrawable(Color.RED);
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START;

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        //This is used for drag and drop, return false for no drag and drop functionality

        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        adapter.swapItems(fromPosition, toPosition);

        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        //This triggers when a swipe is fully completed (threshold is reached)
        int position = viewHolder.getAdapterPosition();

        //Remove the actual image from internal storage
        String filename = adapter.getFileName(position);
        c2h.removeFile(filename);

        //Remove PhotoItem from adapter
        adapter.removeItem(position);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //This method is for drawing the background and trashcan icon underneath the recyclerview item
        //This method is drawn EVERY FRAME while swiping, keep it simple!!!

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;

        //Offset is for drawing background slightly underneath the recyclerview item
        //The offset is only relevant if rounded corners are used, it's set to 0 here because we use rectangles
        int backgroundCornerOffset = 0;

        //Variables used for setting the bounds for drawing background and icon as the correct size
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if(dX > 0){//Swiping right
            int iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            int iconRight = itemView.getLeft() + iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                    itemView.getBottom());
        }
        else if(dX < 0){//Swiping left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        }else{//No swipe
            //Set the bounds to 0, makes icon and background disappear
            icon.setBounds(0, 0, 0, 0);
            background.setBounds(0, 0, 0, 0);
        }

        //Draw the icon and background (if bounds are 0 they will be invisible)
        background.draw(c);
        icon.draw(c);
    }
}
