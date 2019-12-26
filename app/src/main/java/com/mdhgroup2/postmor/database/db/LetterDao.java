package com.mdhgroup2.postmor.database.db;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface LetterDao {

    @Query("UPDATE Settings " +
            "SET OutgoingLetterCount = OutgoingLetterCount + 1")
    void incrementOutoing();

    @Query("UPDATE Settings " +
            "SET OutgoingLetterCount = 0")
    void resetOutgoing();
}
