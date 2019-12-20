package com.mdhgroup2.postmor.database.db;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.DatabaseView;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.mdhgroup2.postmor.database.DTO.MsgCard;
import com.mdhgroup2.postmor.database.Entities.Message;
import com.mdhgroup2.postmor.database.Entities.User;
import com.mdhgroup2.postmor.database.Entities.UserWithMessages;
import com.mdhgroup2.postmor.database.interfaces.IBoxManager;

import java.util.Date;
import java.util.List;

@Dao
public interface BoxDao {

    @Query("SELECT Num FROM InternalMsgID")
    int getNextID();

    @Query("Update InternalMsgID SET Num = Num + 1")
    void incrementID();

    @Query("SELECT * FROM MsgCard")
    List<MsgCard> getAllMessages();

    @Query("SELECT * FROM MsgCard WHERE UserID = :id")
    List<MsgCard> getAllMessages(int id);

    @Query("SELECT * FROM MsgCard WHERE SenderID != :clientID")
    List<MsgCard> getInboxMessages(int clientID);

    @Query("SELECT * FROM MsgCard WHERE SenderID != :clientID AND UserID = :id")
    List<MsgCard> getInboxMessages(int clientID, int id);

    @Query("SELECT * FROM MsgCard WHERE SenderID = :clientID")
    List<MsgCard> getOutboxMessages(int clientID);

    @Query("SELECT * FROM MsgCard WHERE SenderID = :clientID AND UserID = :id")
    List<MsgCard> getOutboxMessages(int clientID, int id);

    @Query("SELECT COUNT(*) FROM Messages WHERE IsRead = 0 AND IsDraft = 0 AND IsOutgoing = 0")
//    @Query("SELECT COUNT(*) FROM Users")
//    int getNewMessageCount();
    LiveData<Integer> getNewMessageCount();

    @Query("SELECT * FROM Users")
    List<User> getUsers();
//    @Transaction
//    @Query("SELECT * FROM Users")
//    List<UserWithMessages> getAllUsers();

}
