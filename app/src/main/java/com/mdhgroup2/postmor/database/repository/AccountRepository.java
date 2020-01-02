package com.mdhgroup2.postmor.database.repository;

import com.mdhgroup2.postmor.database.DTO.Account;
import com.mdhgroup2.postmor.database.Entities.Settings;
import com.mdhgroup2.postmor.database.db.AccountDao;
import com.mdhgroup2.postmor.database.db.Converters;
import com.mdhgroup2.postmor.database.db.ManageDao;
import com.mdhgroup2.postmor.database.db.Utils;
import com.mdhgroup2.postmor.database.interfaces.IAccountRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountRepository implements IAccountRepository {

    private AccountDao accountDb;
    private ManageDao manageDb;

    public AccountRepository(AccountDao accountDao, ManageDao manageDao){
        accountDb = accountDao;
        manageDb = manageDao;
    }

    @Override
    public List<String> getRandomAddresses(int count) {
        List<String> ls = new ArrayList<>();
        ls.add("Bajskastargatan 55");
        ls.add("Kattpissgatan 42  ");
        return ls;
    }

    @Override
    public PasswordStatus isValidPassword(String pass) {
        // Shorter than 6 letters
        if(pass.length() < 6){
            return PasswordStatus.ShorterThan6;
        }

        // One Lowercase
        if(pass.equals(pass.toUpperCase())){
            return PasswordStatus.NeedsLowerCase;
        }

        // One Uppercase
        if(pass.equals(pass.toLowerCase())){
            return PasswordStatus.NeedsUppercase;
        }

        // One numeric
        boolean hasdigit = false;
        for(int i = 0; i < pass.length(); i++){
            if(Character.isDigit(pass.charAt(i))){
               hasdigit = true;
            }
        }
        if(!hasdigit){
            return PasswordStatus.NeedsNumeric;
        }

        // One non alpha-numeric
        boolean hasNonAlNum = false;
        for(int i = 0; i < pass.length(); i++){
            if(!Character.isLetterOrDigit(pass.charAt(i))){
                hasNonAlNum = true;
            }
        }
        if(!hasNonAlNum){
            return PasswordStatus.NeedsNonAlphaNumeric;
        }

        return PasswordStatus.Ok;
    }

    public enum PasswordStatus{
        Ok,
        NeedsLowerCase,
        NeedsUppercase,
        NeedsNumeric,
        NeedsNonAlphaNumeric,
        ShorterThan6
    }

    @Override
    public boolean registerAccount(Account acc) {
        // Query server and, on success, save to database.


        String data = String.format("{" +
                "\"name\" : \"%s\", " +
                "\"password\" : \"%s\", " +
                "\"email\" : \"%s\", " +
                "\"address\" : \"%s\", " +
                "\"picture\" : \"%s\"" +
                "}",
                acc.Name,
                acc.Password,
                acc.Email,
                acc.Address,
                Converters.bitmapToBase64(acc.Picture));

        try {
            JSONObject json = Utils.APIPost(Utils.baseURL + "/identity/register", new JSONObject(data), manageDb);

            Settings s = new Settings();
            s.ID = json.getInt("id");
            s.Address = acc.Address;
            s.ProfilePicture = acc.Picture;
            // 16:00
            String pickup = json.getString("pickupTime");
            int hour = Integer.parseInt(pickup.split(":")[0]);
            int minute = Integer.parseInt(pickup.split(":")[1]);
            s.PickupTime = Utils.makeTime(hour, minute, 0);

            String delivery = json.getString("pickupTime");
            int hour2 = Integer.parseInt(delivery.split(":")[0]);
            int minute2 = Integer.parseInt(delivery.split(":")[1]);

            s.DeliveryTime = Utils.makeTime(hour2, minute2, 0);
            s.PublicKey = json.getString("publicKey");
            s.PrivateKey = json.getString("privateKey");
            s.AuthToken = json.getString("token");
            s.RefreshToken = json.getString("refreshToken");

            s.Email = acc.Email;
            s.Password = acc.Password;
            s.OutgoingLetterCount = 0;
            s.IsLoggedIn = true;

            DatabaseClient.nukeDatabase();

            accountDb.registerAccount(s);
        }
        catch (IOException e){
            return false;
        }
        catch(JSONException j){
            // Return an object with more descriptive errors.
            return false;
        }






        return false; // Server success or fail
    }

    @Override
    public boolean signIn(String email, String password) {
        // Query server for login and, on success, log in locally.
        // If account is not the current in Settings, clear database.
        if(manageDb.getUserEmail() == email && manageDb.getUserPassword() == password){
            // Query server
            accountDb.setSignedIn();
            return manageDb.refreshToken();
        }
        else{
            String data = String.format("{" +
                    "\"email\" : \"%s\", " +
                    "\"password\" : \"%s\"" +
                    "}", email, password);
//            String data = "{}";

            String authToken;
            String refreshToken;

            try{
                JSONObject json = Utils.APIPost(Utils.baseURL + "/identity/login", new JSONObject(data), manageDb);

                authToken = json.getString("token");

                refreshToken = json.getString("refreshToken");


            }
            catch (IOException e){
                return false;
            }
            catch (JSONException j){
                return false;
                // Failed to update key. Possibly offline.
            }

            // Query server
            //
            // On success, empty all tables, and fetch all data.

//            String data2 = String.format("{}");
//            try {
//                JSONObject json = Utils.APIPost(Utils.baseURL + "/user/fetchalldata", new JSONObject(data2), manageDb);
//                // Prepare all data before nuking database and inserting the data.
//                DatabaseClient.nukeDatabase();
//            }
//            catch (JSONException | IOException e){
//                return false;
//            }
            // API "/user/fetchalldata"
//             Save all data
//            manageDb.setAuthToken(authToken);
//            manageDb.setRefreshToken(refreshToken);

            return true;
        }
    }



    @Override
    public void signOut() {
        accountDb.setSignedOut();
        manageDb.setAuthToken("");
        manageDb.setRefreshToken("");
    }
}
