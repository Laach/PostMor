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
import com.mdhgroup2.postmor.database.DTO.UserCard;
import com.mdhgroup2.postmor.database.db.ContactDao;
import com.mdhgroup2.postmor.database.db.ManageDao;
import com.mdhgroup2.postmor.database.db.Utils;
import com.mdhgroup2.postmor.database.interfaces.IContactRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class ContactRepository implements IContactRepository {

    private ContactDao contactdao;
    private ManageDao managedao;

    public ContactRepository(ContactDao contactDao, ManageDao manageDao){
        this.contactdao = contactDao;
        this.managedao = manageDao;
    }

    @Override
    public UserCard findByAddress(String address) {
        UserCard u = new UserCard();
        u.IsFriend = false;
        u.Name = "Bob Bobsson";
        u.Address = "Bobsledgatan 3";
        u.UserID = 666;

        // Mock data. Query server

        return u;
    }

    @Override
    public List<Contact> getContacts() {
        return contactdao.getContacts();
    }

    @Override
    public void addContact(int ID) {
        // Query server
        contactdao.addFriend(ID);


        class AddFriendWorker extends Worker {

            public AddFriendWorker(Context c, WorkerParameters params){
                super(c, params);
            }

            @NonNull
            @Override
            public Result doWork() {
                String data = String.format(""); // Add input data
                try {
                    Utils.APIPost("/user/addfriend", new JSONObject(data));
                }
                catch (JSONException j){
                    return Result.failure();
                }
                catch (IOException e){
                    return Result.failure();
                }
                return Result.success();
            }
        }

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest addFriend =
                new OneTimeWorkRequest.
                        Builder(AddFriendWorker.class)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(DatabaseClient.appContext).enqueue(addFriend);
    }

    @Override
    public void deleteContact(int ID) {
        // Query server
        contactdao.deleteFriend(ID);
    }

    @Override
    public UserCard getUserCard(int ID) {
        return contactdao.getUserCard(ID);
    }
}