package com.mdhgroup2.postmor.database.Entities;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Users")
public class User {
    @PrimaryKey
    public int ID;
    public String Name;
    public String Address;
    public String PublicKey;
    public boolean IsFriend;
    public Bitmap ProfilePicture;
}
