package com.mdhgroup2.postmor.database.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.mdhgroup2.postmor.database.DTO.Contact;
import com.mdhgroup2.postmor.database.DTO.UserCard;
import com.mdhgroup2.postmor.database.Entities.User;

import java.util.List;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM UserCard WHERE UserID = :userID")
    UserCard getUserCard(int userID);

    @Query("UPDATE Users SET IsFriend = 1 WHERE ID = :ID")
    void addFriend(int ID);


    @Query("UPDATE Users SET IsFriend = 0 WHERE ID = :ID")
    void deleteFriend(int ID);

    @Query("SELECT * FROM Contact WHERE IsFriend = 1")
    List<Contact> getContacts();

}
