package com.mdhgroup2.postmor.database.repository;

import android.accounts.NetworkErrorException;
import android.graphics.Bitmap;

import com.mdhgroup2.postmor.database.DTO.Account;
import com.mdhgroup2.postmor.database.Entities.Message;
import com.mdhgroup2.postmor.database.Entities.Settings;
import com.mdhgroup2.postmor.database.Entities.User;
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
                "\"amount\": %d" +
                "}", count);

        try {
            JSONObject json = Utils.APIPost(Utils.baseURL + "/identity/generateaddresses", new JSONObject(data), manageDb);

            JSONArray arr = json.getJSONArray("addresses");
            for(int i = 0; i < arr.length(); i++){
                ls.add(arr.getString(i));
            }
        }
        catch (JSONException e){
            throw new IOException("Invalid data received");
        }
        catch (NullPointerException e){
            throw new IOException("Object was null");
        }

        return ls;
    }

    @Override
    public List<PasswordStatus> isValidPassword(String pass) {
        List<PasswordStatus> passwordStatuses = new ArrayList<PasswordStatus>();
        if(pass == null){
            passwordStatuses.add(PasswordStatus.Null);
            return passwordStatuses;
        }
        // Shorter than 6 letters
        if(pass.length() < 6){
            passwordStatuses.add(PasswordStatus.ShorterThan6);
        }

        // One Lowercase
        if(pass.equals(pass.toUpperCase())){
            passwordStatuses.add(PasswordStatus.NeedsLowerCase);
        }

        // One Uppercase
        if(pass.equals(pass.toLowerCase())){
            passwordStatuses.add(PasswordStatus.NeedsUppercase);
        }

        // One numeric
        boolean hasdigit = false;
        for(int i = 0; i < pass.length(); i++){
            if(Character.isDigit(pass.charAt(i))){
               hasdigit = true;
            }
        }
        if(!hasdigit){
            passwordStatuses.add(PasswordStatus.NeedsNumeric);
        }

        // One non alpha-numeric
        boolean hasNonAlNum = false;
        for(int i = 0; i < pass.length(); i++){
            if(!Character.isLetterOrDigit(pass.charAt(i))){
                hasNonAlNum = true;
            }
        }
        if(!hasNonAlNum){
            passwordStatuses.add(PasswordStatus.NeedsNonAlphaNumeric);
        }
        return passwordStatuses;
    }

    public enum PasswordStatus{
        Ok,
        NeedsLowerCase,
        NeedsUppercase,
        NeedsNumeric,
        NeedsNonAlphaNumeric,
        ShorterThan6,
        NotEqual,
        Null
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
            catch (JSONException | NullPointerException e){
                errors.add("Server error: expected errors but received none");
            }
        }

        if(errors.size() == 0){
            errors.add("Ok");
        }

        return errors;
    }

    @Override
    public List<String> signIn(String email, String password) {
        List<String> errors = new ArrayList<>();
        // Query server for login and, on success, log in locally.
        // If account is not the current in Settings, clear database.
//        String prevEmail = accountDb.getMyEmail();
//        String prevPass  = accountDb.getMyPassword();
//        if(prevEmail != null && prevEmail.equals(email) && prevPass != null && prevPass.equals(password)){
//            // Query server
//            accountDb.setSignedIn();
//            if(manageDb.refreshToken()){
//                errors.add("Ok");
//            }
//            else{
//                errors.add("Failed to validate tokens. No internet?");
//            }
//            return errors;
//        }
//        else{
            String data = String.format("{" +
                    "\"email\" : \"%s\", " +
                    "\"password\" : \"%s\"" +
                    "}", email, password);
//            String data = "{}";

            String authToken;
            String refreshToken;
            JSONObject json = null;
            try{
                json = Utils.APIPost(Utils.baseURL + "/identity/login", new JSONObject(data), manageDb);

                authToken = json.getString("token");

                refreshToken = json.getString("refreshToken");


            }
            catch (IOException e){
                errors.add("IOException 1: possibly no connection");
                return errors;
            }
            catch (JSONException j){
                try {
                    JSONArray arr = json.getJSONArray("errors");
                    for(int i = 0; i < arr.length(); i++){
                        errors.add(arr.getString(i));
                    }
                }
                catch (JSONException | NullPointerException e){
                    errors.add("Server error: expected errors but received none");
                }

                return errors;
                // Failed to update key. Possibly offline.
            }

            // Query server
            //
            // On success, empty all tables, and fetch all data.

            String data2 = String.format("{}");
            try {

                // Fix this. They can't be saved because there's no account
                json = Utils.APIPostWithToken(Utils.baseURL + "/identity/fetchalldata", new JSONObject(data2), manageDb, authToken);


                JSONArray contactsjson  = json.getJSONArray ("contacts");
                JSONArray messagesjson  = json.getJSONArray ("messages");
                JSONObject userdata     = json.getJSONObject("userdata");

                Settings      user     = parseUserdata(userdata    );
                List<User>    contacts = parseContacts(contactsjson);
                List<Message> messages = parseMessages(messagesjson);

                user.Email = email;
                user.Password = password;
                user.RefreshToken = refreshToken;
                user.AuthToken = authToken;



                manageDb.nukeDbAndInsertNewData(user, contacts, messages, accountDb);
                // Prepare all data before nuking database and inserting the data.
                // Save tokens before fetchall and after. So it validates the correct user.
//                DatabaseClient.nukeDatabase();
            }
            catch (IOException e){
                errors.add("IOException 2: possibly no connection");
                return errors;
            }
            catch (JSONException e){
                errors.add("Failed parsing fetch-all response");
                return errors;
            }

            if(errors.size() == 0){
                errors.add("Ok");
            }
            return errors;
//        }
    }


    private Settings parseUserdata(JSONObject json) throws JSONException {
        Settings s = new Settings();
        s.ID = json.getInt("id");
        s.Address = json.getString("address");
        s.Name = json.getString("name");
        s.ProfilePicture = Converters.fromBase64(json.getString("picture"));
        // 16:00
        String pickup   = json.getString("pickupTime");
        int hour        = Integer.parseInt(pickup.split(":")[0]);
        int minute      = Integer.parseInt(pickup.split(":")[1]);
        s.PickupTime    = Utils.makeTime(hour, minute, 0);

        String delivery = json.getString("deliveryTime");
        int hour2       = Integer.parseInt(delivery.split(":")[0]);
        int minute2     = Integer.parseInt(delivery.split(":")[1]);

        s.DeliveryTime  = Utils.makeTime(hour2, minute2, 0);
        s.PublicKey     = json.getString("publicKey");
        s.PrivateKey    = json.getString("privateKey");
//        s.AuthToken = json.getString("token");
//        s.RefreshToken = json.getString("refreshToken");

//        s.Email = acc.Email;
//        s.Password = acc.Password;
        s.OutgoingLetterCount = 0;
        s.IsLoggedIn = true;
        return s;
    }

    private List<User> parseContacts(JSONArray arr) throws JSONException{
        List<User> contacts = new ArrayList<>();
        for(int i = 0; i < arr.length(); i++){
            JSONObject contact = arr.getJSONObject(i);
            User u = new User();
            u.ID             = contact.getInt    ("contactId"     );
            u.Name           = contact.getString ("contactName"   );
            u.IsFriend       = contact.getBoolean("isFriend"      );
            u.Address        = contact.getString ("contactAddress");
            u.PublicKey      = contact.getString ("publicKey"     );
            u.ProfilePicture = Converters.fromBase64(contact.getString("picture"));
            contacts.add(u);
        }

        return contacts;
    }

    private List<Message> parseMessages(JSONArray arr) throws JSONException{
        int myId = manageDb.getUserId();
        List<Message> messages = new ArrayList<>();
        for(int i = 0; i < arr.length(); i++){
            JSONObject message = arr.getJSONObject(i);
            Message m = new Message();
            int senderId   = message.getInt("senderId");
            int receiverId = message.getInt("receiverId");
            if(senderId == myId){
                m.UserID        = receiverId;
                m.WrittenBy     = myId;
                m.DeliveryTime  = null;
            }
            else{
                m.UserID        = senderId;
                m.WrittenBy     = senderId;
                m.DeliveryTime  = Utils.parseDate(message.getString("deliveryTime"));
            }
            m.ExternalMessageID = message.getInt("messageId");
            m.TimeStamp = Utils.parseDate(message.getString("timestamp"));

            String type = message.getString("type");
            if(type.equals("text")){
                m.Text = message.getJSONArray("content").getString(0);
            }
            else{
                m.Images = new ArrayList<>();
                JSONArray content = message.getJSONArray("content");
                for(int j = 0; j < content.length(); j++){
                    m.Images.add(Converters.fromBase64(content.getString(j)));
                }
            }

            m.IsDraft = false;
            m.IsRead = false;
            m.IsOutgoing = false;

            messages.add(m);
        }

        return messages;
    }





    @Override
    public void signOut() {
        accountDb.setSignedOut();
//        manageDb.setAuthToken("");
//        manageDb.setRefreshToken("");
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
        if(accountDb.isLoggedIn()){
            manageDb.refreshToken();
            return true;
        }
        return false;
    }


    // Server has not implemented this yet.
    public boolean changePassword(String oldpass, String newpass){
        String data = String.format("{" +
                "\"oldPassword\" : \"%s\", " +
                "\"newPassword\" : \"%s\"" +
                "}", oldpass, newpass);

        try {
            JSONObject json = Utils.APIPost(Utils.baseURL + "/user/update/password", new JSONObject(data), manageDb);
            if(json.getBoolean("success")){
                manageDb.updatePassword(newpass);
                return true;
            }
        }
        catch (IOException | JSONException e){

        }
        return false;
    }
}
