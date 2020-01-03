package com.mdhgroup2.postmor.database.repository;

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

    ContactRepository(ContactDao contactDao, ManageDao manageDao){
        this.contactdao = contactDao;
        this.managedao = manageDao;
    }

    @Override
    public Contact findByAddress(String address) {

        String data = String.format("{" +
                "\"address\" : \"%s\" " +
                "}", address);

        try {
            JSONObject json = Utils.APIPost(Utils.baseURL + "/contact/search", new JSONObject(data), managedao);

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

        String data = String.format(Locale.US, "{" +
                "\"contactId\" : %d}", ID);

        try {
            JSONObject json = Utils.APIPost(Utils.baseURL + "/contact/add", new JSONObject(data), managedao);
            System.out.println(json.toString());
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

        String data = String.format(Locale.US, "{" +
                "\"contactId\" : %d", ID);

        try {
            JSONObject json = Utils.APIPost(Utils.baseURL + "/contact/add", new JSONObject(data), managedao);
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
        Contact local = contactdao.getUserCard(ID);
        if(local == null){
            managedao.downloadUserInfo(ID);
            return contactdao.getUserCard(ID);
        }
        return local;
    }
}
