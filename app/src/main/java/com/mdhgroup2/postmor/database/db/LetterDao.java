package com.mdhgroup2.postmor.database.db;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import com.mdhgroup2.postmor.database.DTO.EditMsg;
import com.mdhgroup2.postmor.database.Entities.Message;

@Dao
public interface LetterDao {

    @Query("UPDATE Settings " +
            "SET OutgoingLetterCount = OutgoingLetterCount + 1")
    void incrementOutoing();

    @Query("UPDATE Settings " +
            "SET OutgoingLetterCount = 0")
    void resetOutgoing();

    @Update
    void updateMessage(Message m);

    @Query("SELECT * FROM EditMsg WHERE RecipientID = :recipientId AND IsDraft = 1 LIMIT 1")
    EditMsg getDraftById(int recipientId);

    @Query("SELECT * FROM EditMsg WHERE RecipientID = 0 AND IsDraft = 1 LIMIT 1")
    EditMsg getGenericDraft();

    @Query("SELECT * FROM Messages WHERE InternalMessageID = :internalId LIMIT 1")
    Message getMessageById(int internalId);

}
