package com.mdhgroup2.postmor.database.db;

import android.graphics.Bitmap;
import android.util.Base64;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.mdhgroup2.postmor.database.Entities.InternalMsgID;
import com.mdhgroup2.postmor.database.Entities.Message;
import com.mdhgroup2.postmor.database.Entities.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Dao
public abstract class ManageDao {

    @Insert
    public abstract void addUser(User user);

    @Insert
    public abstract void addMessage(Message msg);

    @Query("SELECT ID FROM Settings LIMIT 1")
    public abstract int getUserId();

    @Query("SELECT Password FROM Settings LIMIT 1")
    public abstract String getUserPassword();

    @Query("SELECT Email FROM Settings LIMIT 1")
    public abstract String getUserEmail();

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

    @Insert
    public abstract void initInternalID(InternalMsgID i);

    @Query("SELECT AuthToken FROM Settings LIMIT 1")
    public abstract String getAuthToken();

    @Query("UPDATE Settings SET AuthToken = :token")
    public abstract void setAuthToken(String token);

    @Query("SELECT RefreshToken FROM Settings LIMIT 1")
    public abstract String getRefreshToken();

    @Query("UPDATE Settings SET RefreshToken = :token")
    public abstract void setRefreshToken(String token);

    @Transaction
    public int getNewMsgId(){
        incrementInternalMsgID();
        return getInternalMsgID();
    }

    public boolean refresh(){
        String data = String.format("{" +
                        "\"Token\" : \"%s\", " +
                        "\"RefreshToken\" : \"%s\", " +
                        "}",
                getAuthToken(),
                getRefreshToken());
        JSONObject json;

        try {
            json = Utils.APIPost("/identity/refresh", new JSONObject(data));
            String refreshToken = json.getString("refreshToken");
            String authToken    = json.getString("token");
            setRefreshToken(refreshToken);
            setAuthToken(authToken);
            return true;
        }
        catch (IOException e){
            return false;
        }
        catch (JSONException j){
            return false;
        }
    }

    public boolean tokenIsValid(){
        String token = getAuthToken();
        byte[] decoded = Base64.decode(token, Base64.DEFAULT);
        try {
            String data = new String(decoded, StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(data);
            int tokenTime = json.getInt("exp");


            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.clear();
            calendar.set(2011, Calendar.OCTOBER, 1);
            int currentTime = (int)(calendar.getTimeInMillis() / 1000L);

            return tokenTime > currentTime;
        }
        catch (JSONException e){
            return false;
        }

    }

}
