package com.mdhgroup2.postmor.database.Entities;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Users")
public class User {
    @PrimaryKey
    public int ID;
    @NonNull
    public String Name;
    @NonNull
    public String Address;
    @NonNull
    public String PublicKey;
    public boolean IsFriend;
    public Bitmap ProfilePicture;
}
