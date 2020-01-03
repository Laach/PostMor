package com.mdhgroup2.postmor.database.db;

import android.graphics.Bitmap;

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

   @Query("SELECT Name FROM Settings LIMIT 1")
   String getMyName();

   @Query("SELECT Email FROM Settings LIMIT 1")
   String getMyEmail();

   @Query("SELECT Address FROM Settings LIMIT 1")
   String getMyAddress();

   @Query("SELECT Password FROM Settings LIMIT 1")
   String getMyPassword();

   @Query("SELECT ProfilePicture FROM Settings LIMIT 1")
   Bitmap getMyProfilePicture();

   @Query("SELECT IsLoggedIn FROM Settings LIMIT 1")
   boolean isLoggedIn();
}
