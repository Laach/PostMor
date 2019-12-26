package com.mdhgroup2.postmor.database.db;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.mdhgroup2.postmor.database.Entities.Message;
import com.mdhgroup2.postmor.database.Entities.User;

import java.util.Date;

@Dao
public abstract class ManageDao {
    @Insert
    public abstract void addMessage(Message msg);

    @Insert
    public abstract void addUser(User user);

    @Query("SELECT ID FROM Settings LIMIT 1")
    public abstract int getUserId();

    @Query("SELECT Password FROM Settings LIMIT 1")
    public abstract String getUserPassword();

    @Query("SELECT ProfilePicture FROM Settings LIMIT 1")
    public abstract Bitmap getUserProfilePicture();

    @Query("SELECT PublicKey FROM Settings LIMIT 1")
    public abstract String getUserPublicKey();

    @Query("SELECT PrivateKey FROM Settings LIMIT 1")
    public abstract String getUserPrivateKey();

    @Query("SELECT PickupTime FROM Settings LIMIT 1")
    public abstract Date getUserPickupTime();

    @Query("SELECT OutgoingLetterCount FROM Settings LIMIT 1")
    public abstract LiveData<Integer> getOutgoingLetterCount();

    @Query("UPDATE InternalMsgID " +
            "SET Num = Num + 1")
    abstract void incrementInternalMsgID();

    @Query("SELECT Num FROM InternalMsgID LIMIT 1")
    abstract int getInternalMsgID();

    @Query("SELECT AuthToken FROM Settings LIMIT 1")
    abstract int getAuthToken();

    @Query("UPDATE Settings SET AuthToken = :token")
    abstract int setAuthToken(String token);

    @Transaction
    public int getNewMsgId(){
        incrementInternalMsgID();
        return getInternalMsgID();
    }


}
