package com.mdhgroup2.postmor;

import com.mdhgroup2.postmor.database.DTO.Contact;
import com.mdhgroup2.postmor.database.interfaces.IContactRepository;
import com.mdhgroup2.postmor.database.repository.DatabaseClient;

import java.util.List;

import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    private final List<Contact> contacts;
    private final IContactRepository repo;

    public MainActivityViewModel(){
        repo = DatabaseClient.getMockContactRepository();
        contacts = repo.getContacts();
    }

    public List<Contact> getContactList(){
        return contacts;
    }

    public Contact getContact(int index){
        return contacts.get(index);
    }

    public boolean removeContact(Contact c){
        contacts.remove(c);
        repo.deleteContact(c.UserID);
        return true;
    }
}