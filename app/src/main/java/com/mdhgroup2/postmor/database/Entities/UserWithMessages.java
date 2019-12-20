package com.mdhgroup2.postmor.database.Entities;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import java.util.List;

public class UserWithMessages {
    @Embedded public User User;

    @Relation(
            parentColumn = "ID",
            entityColumn = "UserID"
    )
    public List<Message> Msg;
}
