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

import java.util.ArrayList;
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
    public boolean isValidPassword(String pass) {
        // Check locally
        return true;
    }

    @Override
    public boolean registerAccount(Account acc) {
        // Query server and, on success, save to database.


        String data = String.format("{" +
                "\"username\" : \"%s\", " +
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
            JSONObject json = Utils.APIPost("url", new JSONObject(data));

            Settings s = new Settings();
            s.ID = -1; // Server response
            s.Address = acc.Address;
            s.ProfilePicture = acc.Picture;
            s.Password = acc.Password; // Maybe get pass hash from server.
            s.Email = acc.Email;
            s.OutgoingLetterCount = 0;
            s.IsLoggedIn = true;
            s.PickupTime = null; // Server response
            s.PublicKey = null; // Server response
            s.PrivateKey = null; // Server response
            s.AuthToken = null; // Server response

            accountDb.registerAccount(s);
        }
        catch(JSONException j){
            // Return an object with more descriptive errors.
            return false;
        }





        // Attempt login

        return false; // Server success or fail
    }

    @Override
    public boolean signIn(String email, String pass) {
        // Query server for login and, on success, log in locally.
        // If account is not the current in Settings, clear database.

        return false;
    }

    @Override
    public void signOut() {
        // Query server for logout, and then log out either way.

        accountDb.setSignedOut();
    }
}
