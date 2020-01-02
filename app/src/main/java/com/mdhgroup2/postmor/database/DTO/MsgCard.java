package com.mdhgroup2.postmor.database.DTO;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.List;

public class MsgCard {
    public String UserID;
    public String Name;
    public String Address;
    public Bitmap Picture;
    public boolean IsSentByMe;
    public boolean IsFriend;
    public Date DateStamp; // Delivery date if received. Sent date if written by you
    public int MsgID;
    public String Text;
    public List<Bitmap> Images;
}
