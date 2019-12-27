package com.mdhgroup2.postmor.database.Entities;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Settings {
    @PrimaryKey public int ID;
    @NonNull
    public String Email;
    @NonNull
    public String Password;
    @NonNull
    public String Address;
    public Bitmap ProfilePicture;
    @NonNull
    public String PublicKey;
    @NonNull
    public String PrivateKey;
    @NonNull
    public Date PickupTime;
    @NonNull
    public Integer OutgoingLetterCount;
    @NonNull
    public boolean IsLoggedIn;
    @NonNull
    public String AuthToken;
    @NonNull
    public String RefreshToken;

}
