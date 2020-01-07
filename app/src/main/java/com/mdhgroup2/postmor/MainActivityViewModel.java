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
    private final IContactRepository contactRepo;
    private final IBoxRepository boxRepo;

    public MainActivityViewModel(){
        contactRepo = DatabaseClient.getMockContactRepository();
        boxRepo = DatabaseClient.getMockBoxRepository();
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

    public Contact getContactById(int id){ return contactRepo.getUserCard(id);}

    public Contact getContact(int index){
        return contacts.get(index);
    }

    public boolean removeContact(Contact c){
        if(contactRepo.deleteContact(c.UserID)){
            contacts.remove(c);
            return true;
        }
        return false;
    }

    public Contact findUserByAddress(String address){
        return contactRepo.findByAddress(address);
    }

    public void addUserToContacts (Contact friend){
        contacts.add(friend);
        contactRepo.addContact(friend.UserID);
    }
}