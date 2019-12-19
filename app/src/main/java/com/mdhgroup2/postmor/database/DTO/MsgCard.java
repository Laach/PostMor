package com.mdhgroup2.postmor.database.DTO;

import android.graphics.Bitmap;

import java.time.LocalDate;

public class MsgCard {
    public String Name;
    public String Address;
    public Bitmap Picture;
    public boolean IsFriend;
    public LocalDate TimeStamp; // Delivery date if received. Sent date if written by you
    public int MsgID;

}
