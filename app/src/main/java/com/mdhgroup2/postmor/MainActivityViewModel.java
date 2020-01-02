package com.mdhgroup2.postmor;

import com.mdhgroup2.postmor.database.DTO.Contact;
import com.mdhgroup2.postmor.database.DTO.MsgCard;
import com.mdhgroup2.postmor.database.interfaces.IContactRepository;
import com.mdhgroup2.postmor.database.interfaces.IBoxRepository;
import com.mdhgroup2.postmor.database.repository.DatabaseClient;

import java.util.List;

import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    private final List<Contact> contacts;
    private final List<MsgCard> messages;
    private final IContactRepository contactRepo;
    private final IBoxRepository boxRepo;

    public MainActivityViewModel(){
        contactRepo = (IContactRepository) DatabaseClient.getMockContactRepository();
        boxRepo = (IBoxRepository) DatabaseClient.getMockBoxRepository();
        contacts = contactRepo.getContacts();
        messages = boxRepo.getAllMessages();
    }

    public List<Contact> getContactList(){
        return contacts;
    }
    public List<MsgCard> getMessageList(){
        return messages;
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
}