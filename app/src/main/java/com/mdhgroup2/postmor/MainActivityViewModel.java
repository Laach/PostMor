package com.mdhgroup2.postmor;

import android.os.AsyncTask;

import com.mdhgroup2.postmor.database.DTO.Contact;
import com.mdhgroup2.postmor.database.DTO.MsgCard;
import com.mdhgroup2.postmor.database.interfaces.IAccountRepository;
import com.mdhgroup2.postmor.database.interfaces.IContactRepository;
import com.mdhgroup2.postmor.database.interfaces.IBoxRepository;
import com.mdhgroup2.postmor.database.repository.DatabaseClient;
import java.util.List;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    private final List<Contact> contacts;
    private final IContactRepository contactRepo;
    private final IBoxRepository boxRepo;
    private final IAccountRepository accountRepo;

    public MainActivityViewModel(){
        contactRepo = (IContactRepository) DatabaseClient.getMockContactRepository();
        boxRepo = (IBoxRepository) DatabaseClient.getMockBoxRepository();
        accountRepo = DatabaseClient.getAccountRepository();
        contacts = contactRepo.getContacts();
    }

    public List<Contact> getContactList(){
        return contacts;
    }
    public List<MsgCard> getMessageList(int index){
        if (index == 1) {
            return boxRepo.getAllMessages();
        }
        else if (index == 2){
            return boxRepo.getInboxMessages();
        }
        return boxRepo.getOutboxMessages();

    }

    public Contact getContact(int index){
        return contacts.get(index);
    }

    public boolean removeContact(Contact c){
        contacts.remove(c);
        contactRepo.deleteContact(c.UserID);
        return true;
    }

    public Contact findUserByAddress(String address){
        return contactRepo.findByAddress(address);
    }

    public void addUserToContacts (Contact friend){
        contacts.add(friend);
        contactRepo.addContact(friend.UserID);
    }

    public void logOut(){
        dbLogout logout = new dbLogout();
        logout.execute();
    }

    private class dbLogout extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... v){
            accountRepo.signOut();
            return null;
        }
    }
}