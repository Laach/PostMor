package com.mdhgroup2.postmor.database.DTO;

import android.graphics.Bitmap;

import androidx.room.DatabaseView;
import androidx.room.Ignore;

import java.util.List;


@DatabaseView("SELECT " +
        "Text, " +
        "Images, " +
        "InternalMessageID " +
        "FROM Messages")

public class MessageContent {
    public String Text;
    public List<Bitmap> Images;
    public int InternalMessageID;
}
