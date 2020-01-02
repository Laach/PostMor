package com.mdhgroup2.postmor.database.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.mdhgroup2.postmor.database.DTO.Contact;
import com.mdhgroup2.postmor.database.Entities.User;
import com.mdhgroup2.postmor.database.db.ContactDao;
import com.mdhgroup2.postmor.database.db.Converters;
import com.mdhgroup2.postmor.database.db.ManageDao;
import com.mdhgroup2.postmor.database.db.Utils;
import com.mdhgroup2.postmor.database.interfaces.IContactRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ContactRepository implements IContactRepository {

    private ContactDao contactdao;
    private ManageDao managedao;

    public ContactRepository(ContactDao contactDao, ManageDao manageDao){
        this.contactdao = contactDao;
        this.managedao = manageDao;
    }

    @Override
    public Contact findByAddress(String address) {

        String token = managedao.getAuthToken();
        String data = String.format("{" +
                "\"token\" : \"%s\", " +
                "\"address\" : \"%s\" " +
                "}", token, address);

        try {
            JSONObject json = Utils.APIPost(Utils.baseURL + "/contact/search", new JSONObject(data));

            User u = new User();
            u.ID = json.getInt("contactId");
            u.Name = json.getString("name");
            u.IsFriend = json.getBoolean("isFriend");
            u.Address = json.getString("address");
            u.ProfilePicture = Converters.fromBase64(json.getString("picture"));
            u.PublicKey = json.getString("publicKey");

            managedao.addUser(u);

            Contact c = new Contact();
            c.UserID = u.ID;
            c.Name = u.Name;
            c.IsFriend = u.IsFriend;
            c.Address = u.Address;
            c.Picture = u.ProfilePicture;
            return c;
        }
        catch (JSONException | IOException | NullPointerException e){
            return null;
        }
    }

    @Override
    public List<Contact> getContacts() {
        return contactdao.getContacts();
    }

    @Override
    public boolean addContact(final int ID) {

        String token = managedao.getAuthToken();
        String data = String.format(Locale.US, "{" +
                "\"token\" : \"%s\", " +
                "\"contactId\" : %d", token, ID);

        try {
            JSONObject json = Utils.APIPost(Utils.baseURL + "/contact/add", new JSONObject(data));
            if(json.getBoolean("success")){
                contactdao.addFriend(ID);
                return true;
            }

        }
        catch (JSONException | IOException | NullPointerException e){
            return false;
        }

        return false;
    }

    @Override
    public boolean deleteContact(int ID) {

        String token = managedao.getAuthToken();
        String data = String.format(Locale.US, "{" +
                "\"token\" : \"%s\", " +
                "\"contactId\" : %d", token, ID);

        try {
            JSONObject json = Utils.APIPost(Utils.baseURL + "/contact/add", new JSONObject(data));
            if(json.getBoolean("success")){
                contactdao.deleteFriend(ID);
                return true;
            }

        }
        catch (JSONException | IOException | NullPointerException e){
            return false;
        }

        return false;
    }

    @Override
    public Contact getUserCard(int ID) {
        return contactdao.getUserCard(ID);
    }
}
