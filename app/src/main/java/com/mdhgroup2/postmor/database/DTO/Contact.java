package com.mdhgroup2.postmor.database.DTO;

import android.graphics.Bitmap;

import androidx.room.DatabaseView;

@DatabaseView("SELECT " +
        "Name, " +
        "Address, " +
        "ProfilePicture as Picture, " +
        "IsFriend " +
        "FROM Users")

public class Contact {
    public String Name;
    public String Address;
    public Bitmap Picture;
    public int UserID;
    public boolean IsFriend;
}
