package com.mdhgroup2.postmor.Contacts;

import com.mdhgroup2.postmor.database.DTO.Contact;
import com.mdhgroup2.postmor.database.interfaces.IContactRepository;
import com.mdhgroup2.postmor.database.repository.DatabaseClient;

import java.util.List;

import androidx.lifecycle.ViewModel;

public class ContactsViewModel extends ViewModel {

    private List<Contact> contacts;

    public ContactsViewModel(){
        contacts = DatabaseClient.getMockContactRepository().getContacts();
    }

    public List<Contact> getContactList(){
        return contacts;
    }

    public Contact getContact(int index){
        return contacts.get(index);
    }
}
