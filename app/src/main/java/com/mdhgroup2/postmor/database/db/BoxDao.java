package com.mdhgroup2.postmor.database.db;

import androidx.room.Dao;
import androidx.room.Query;

import com.mdhgroup2.postmor.database.DTO.MessageContent;
import com.mdhgroup2.postmor.database.DTO.MsgCard;
import com.mdhgroup2.postmor.database.Entities.User;

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
    int getNewMessageCount();

    @Query("SELECT * FROM Users")
    List<User> getUsers();

    @Query("SELECT Text, Images, InternalmessageID FROM MessageContent WHERE InternalmessageID = :internalMsgId")
    MessageContent getMsgContent(int internalMsgId);

//    @Transaction
//    @Query("SELECT * FROM Users")
//    List<UserWithMessages> getAllUsers();

}
