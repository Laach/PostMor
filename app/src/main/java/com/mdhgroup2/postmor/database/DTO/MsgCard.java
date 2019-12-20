package com.mdhgroup2.postmor.database.DTO;

import android.graphics.Bitmap;

import java.util.Date;

public class MsgCard {
    public String Name;
    public String Address;
    public Bitmap Picture;
    public boolean IsFriend;
    public Date DateStamp; // Delivery date if received. Sent date if written by you
    public int MsgID;

}
