package com.mdhgroup2.postmor.database.repository;

import android.accounts.NetworkErrorException;
import android.graphics.Bitmap;

import com.mdhgroup2.postmor.database.DTO.Account;
import com.mdhgroup2.postmor.database.Entities.Settings;
import com.mdhgroup2.postmor.database.db.AccountDao;
import com.mdhgroup2.postmor.database.db.Converters;
import com.mdhgroup2.postmor.database.db.ManageDao;
import com.mdhgroup2.postmor.database.db.Utils;
import com.mdhgroup2.postmor.database.interfaces.IAccountRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AccountRepository implements IAccountRepository {

    private AccountDao accountDb;
    private ManageDao manageDb;

    AccountRepository(AccountDao accountDao, ManageDao manageDao){
        accountDb = accountDao;
        manageDb = manageDao;
    }

    @Override
    public List<String> getRandomAddresses(int count) throws IOException {
        List<String> ls = new ArrayList<>();
//        ls.add("Bajskastargatan 55");
//        ls.add("Kattpissgatan 42  ");

        String data = String.format(Locale.US, "{" +
                "\"amount\": : %d" +
                "}", count);

        try {
            JSONArray arr = Utils.APIPostArray(Utils.baseURL + "/identity/generateaddresses", new JSONObject(data), manageDb);

            for(int i = 0; i < arr.length(); i++){
                ls.add(arr.getString(i));
            }
        }
        catch (JSONException e){
            throw new IOException("Invalid data received");
        }

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
    public List<String> registerAccount(Account acc) {
        // If returned list is empty, everything went well;

        List<String> errors = new ArrayList<>();

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

        JSONObject json = null;
        try {
            json = Utils.APIPost(Utils.baseURL + "/identity/register", new JSONObject(data), manageDb);

            Settings s = new Settings();
            s.ID = json.getInt("id");
            s.Address = acc.Address;
            s.Name = acc.Name;
            s.ProfilePicture = acc.Picture;
            // 16:00
            String pickup = json.getString("pickupTime");
            int hour = Integer.parseInt(pickup.split(":")[0]);
            int minute = Integer.parseInt(pickup.split(":")[1]);
            s.PickupTime = Utils.makeTime(hour, minute, 0);

            String delivery = json.getString("deliveryTime");
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
            errors.add("Network error");
        }
        catch (NullPointerException e){
            errors.add("NullPointerException: invalid data received from server");
        }
        catch(JSONException j){
            // Return an object with more descriptive errors.
            if(json == null){
                errors.add("JSON error: invalid input data");
                return errors;
            }
            try {
                JSONArray arr = json.getJSONArray("errors");
                for(int i = 0; i < arr.length(); i++){
                    errors.add(arr.getString(i));
                }
            }
            catch (JSONException e){
                errors.add("Server error: expected errors but received none");
            }
        }

        if(errors.size() == 0){
            errors.add("Ok");
        }

        return errors;
    }

    @Override
    public boolean signIn(String email, String password) {
        // Query server for login and, on success, log in locally.
        // If account is not the current in Settings, clear database.
        if(accountDb.getMyEmail().equals(email) && accountDb.getMyPassword().equals(password)){
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
                  // Save tokens before fetchall and after. So it validates the correct user.
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


    private void insertFromFetchAll(JSONObject json) throws JSONException {


    }



    @Override
    public void signOut() {
        accountDb.setSignedOut();
        manageDb.setAuthToken(null);
        manageDb.setRefreshToken(null);
    }

    @Override
    public String getMyName() {
        return accountDb.getMyName();
    }

    @Override
    public String getMyEmail() {
        return accountDb.getMyEmail();
    }

    @Override
    public String getMyAddress() {
        return accountDb.getMyAddress();
    }

    @Override
    public Bitmap getMyProfilePicture() {
        return accountDb.getMyProfilePicture();
    }

    @Override
    public boolean isLoggedIn() {
        return accountDb.isLoggedIn();
    }
}
