package com.mdhgroup2.postmor.database.db;

import androidx.room.Dao;
import androidx.room.Query;

import com.mdhgroup2.postmor.database.DTO.BoxMessage;
import com.mdhgroup2.postmor.database.DTO.MessageContent;
import com.mdhgroup2.postmor.database.Entities.Message;
import com.mdhgroup2.postmor.database.Entities.User;

import java.util.List;

@Dao
public interface BoxDao {

    @Query("SELECT * FROM Messages")
    List<Message> getAllMessagesFull();

    @Query("SELECT MAX(InternalMessageID) FROM Messages")
    int getLatestId();

    @Query("SELECT * FROM BoxMessage")
    List<BoxMessage> getAllMessages();

    @Query("SELECT * FROM BoxMessage WHERE UserID = :userID")
    List<BoxMessage> getAllMessages(int userID);

    @Query("SELECT * FROM BoxMessage WHERE SenderID != :clientID")
    List<BoxMessage> getInboxMessages(int clientID);

    @Query("SELECT * FROM BoxMessage WHERE SenderID != :clientID AND UserID = :id")
    List<BoxMessage> getInboxMessages(int clientID, int id);

    @Query("SELECT * FROM BoxMessage WHERE SenderID = :clientID")
    List<BoxMessage> getOutboxMessages(int clientID);

    @Query("SELECT * FROM BoxMessage WHERE SenderID = :clientID AND UserID = :id")
    List<BoxMessage> getOutboxMessages(int clientID, int id);

    @Query("SELECT COUNT(*) FROM Messages WHERE IsRead = 0 AND IsDraft = 0 AND IsOutgoing = 0")
    int getNewMessageCount();

    @Query("SELECT * FROM Users")
    List<User> getUsers();

    @Query("SELECT Text, Images, InternalmessageID FROM MessageContent WHERE InternalmessageID = :internalMsgId")
    MessageContent getMsgContent(int internalMsgId);

}
