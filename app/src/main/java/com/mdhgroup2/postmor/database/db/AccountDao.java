package com.mdhgroup2.postmor.database.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mdhgroup2.postmor.database.Entities.Settings;

@Dao
public interface AccountDao {

   @Insert
   void registerAccount(Settings s);

   @Query("DELETE FROM Settings")
   void removeAccount();

   @Query("UPDATE Settings SET IsLoggedIn = 1")
   void setSignedIn();

   @Query("UPDATE Settings SET IsLoggedIn = 0")
   void setSignedOut();
}
