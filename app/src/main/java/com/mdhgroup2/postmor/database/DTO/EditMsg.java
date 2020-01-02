package com.mdhgroup2.postmor.database.DTO;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;
import androidx.room.DatabaseView;

import java.util.List;

@DatabaseView("SELECT " +
        "InternalMessageID, " +
        "Text, " +
        "Images, " +
        "UserID as RecipientID," +
        "IsDraft " +
        "FROM Messages")

public class EditMsg {
    public int InternalMessageID;
    public String Text;
    public List<Bitmap> Images;
    @Nullable
    public Integer RecipientID;
    public boolean IsDraft;
}
