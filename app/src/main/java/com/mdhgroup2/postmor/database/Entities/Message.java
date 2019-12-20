package com.mdhgroup2.postmor.database.Entities;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Messages")
public class Message {
    @PrimaryKey
    public int InternalMessageID;
    public int ExternalMessageID;
    @Nullable
    public int UserID; // Reference User. This is the person the message is associated with.
    public int WrittenBy;
    public boolean IsDraft;
    public boolean IsOutgoing;
    public boolean IsRead;
    public Date TimeStamp;
    public Date DeliveryTime;
    public String SenderPublicKey;
}
