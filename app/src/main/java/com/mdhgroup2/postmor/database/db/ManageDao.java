package com.mdhgroup2.postmor.database.db;

import android.graphics.Bitmap;
import android.util.Base64;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.mdhgroup2.postmor.database.Entities.InternalMsgID;
import com.mdhgroup2.postmor.database.Entities.Message;
import com.mdhgroup2.postmor.database.Entities.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Dao
public abstract class ManageDao {

    @Insert
    public abstract void addUser(User user);

    @Insert
    public abstract void addMessage(Message msg);

    @Query("SELECT ID FROM Settings LIMIT 1")
    public abstract int getUserId();

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


    @Query("SELECT * FROM Users WHERE ID = :userID")
    public abstract User findUser(int userID);
    
    @Transaction
    public int getNewMsgId(){
        incrementInternalMsgID();
        return getInternalMsgID();
    }

    private boolean refresh(){
        String token = getAuthToken();
        String refresh = getRefreshToken();
        if(token == null || refresh == null){
            return false;
        }
        String data = String.format("{" +
                        "\"token\" : \"%s\", " +
                        "\"refreshToken\" : \"%s\", " +
                        "}",
                getAuthToken(),
                getRefreshToken());
        JSONObject json;

        try {
            json = Utils.APIPost("/identity/refresh", new JSONObject(data), this);
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

    private boolean tokenIsValid(){
        String token = getAuthToken();
        if (token == null || token == "") {
            return false;
        }
//        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJOaWNrIiwianRpIjoiOGZiZGU4NjktZTdlYi00ZDQ3LTgxOWItMDZkOGE5MjUxZGRjIiwiZW1haWwiOiJuaWNrQGFuaW1ldGl0dGllcy5jb20iLCJpZCI6IjEiLCJuYmYiOjE1Nzc2OTkyODgsImV4cCI6MTU3NzY5OTMzMywiaWF0IjoxNTc3Njk5Mjg4fQ.MhFc_5KddDRj77VinASwaMbhNHS1-KyVZQ0oKr7NH7w";
        token = token.split("\\.")[1];

        byte[] decoded = Base64.decode(token, Base64.NO_CLOSE);
        try {
            String data = new String(decoded, StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(data);
            int tokenTime = json.getInt("exp");


            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            int currentTime = (int)(calendar.getTimeInMillis() / 1000L);

            return tokenTime > currentTime;
        }
        catch (JSONException e){
            return false;
        }

    }

    public boolean refreshToken(){
        if(tokenIsValid()){
            return true;
        }
        else{
            return refresh();
        }
    }

    public void downloadUserInfo(int ID){
        if(findUser(ID) != null){
            return;
        }

        String data = String.format(Locale.US, "{" +
                "\"contactId\" : %d " +
                "}", ID);

        try {
            JSONObject json = Utils.APIPost(Utils.baseURL + "/contact/get", new JSONObject(data), this);

            User u = new User();
            u.PublicKey = json.getString("publicKey");
            u.ProfilePicture = Converters.fromBase64(json.getString("picture"));
            u.Address = json.getString("address");
            u.ID = ID;
            u.IsFriend = json.getBoolean("isFriend");
            u.Name = json.getString("name");

            addUser(u);
        }
        catch (JSONException | IOException e){
            return;
        }
        return;
    }

}
