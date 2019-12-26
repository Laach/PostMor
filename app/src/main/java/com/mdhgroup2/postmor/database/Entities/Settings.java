package com.mdhgroup2.postmor.database.Entities;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Settings {
    @PrimaryKey
    public int ID;
    public String Password;
    public Bitmap ProfilePicture;
    public String PublicKey;
    public String PrivateKey;
    public Date PickupTime;
    public Integer OutgoingLetterCount;

}
