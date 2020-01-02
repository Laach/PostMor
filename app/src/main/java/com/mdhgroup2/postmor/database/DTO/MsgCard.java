package com.mdhgroup2.postmor.database.DTO;

import android.graphics.Bitmap;

import androidx.room.DatabaseView;

import java.util.Date;

@DatabaseView("SELECT " +
        "Users.ID          as UserID, " +
        "WrittenBy         as SenderID, " +
        "Name              as Name, " +
        "Address           as Address, " +
        "ProfilePicture    as Picture, " +
        "IsFriend          as IsFriend, " +
        "TimeStamp         as DateStamp, " +
        "InternalMessageID as MsgID " +
        "FROM Users INNER JOIN Messages on Users.ID = Messages.UserID")

public class MsgCard {
    public String UserID;
    public String SenderID;
    public String Name;
    public String Address;
    public Bitmap Picture;
    public boolean IsFriend;
    public Date DateStamp; // Delivery date if received. Sent date if written by you
    public int MsgID;
}
