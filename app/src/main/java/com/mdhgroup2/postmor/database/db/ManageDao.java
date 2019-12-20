package com.mdhgroup2.postmor.database.db;

import androidx.room.Dao;
import androidx.room.Insert;

import com.mdhgroup2.postmor.database.Entities.Message;
import com.mdhgroup2.postmor.database.Entities.User;

@Dao
public interface ManageDao {
    @Insert
    void addMessage(Message msg);

    @Insert
    void addUser(User user);
}
