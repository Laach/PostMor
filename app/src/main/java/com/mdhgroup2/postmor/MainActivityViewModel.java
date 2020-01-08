package com.mdhgroup2.postmor;

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
    public int screenWidthDp = 0;
    public int selectedContact = 0;

    public MainActivityViewModel(){
        contactRepo = (IContactRepository) DatabaseClient.getMockContactRepository();
        boxRepo = (IBoxRepository) DatabaseClient.getMockBoxRepository();
        accountRepo = (IAccountRepository) DatabaseClient.getAccountRepository();
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
        try{
            return contacts.get(index);
        } catch(IndexOutOfBoundsException e)
        {
            Contact noContact = new Contact();
            noContact.Address = "";
            noContact.IsFriend = true;
            noContact.Name = "No contact selected";
            noContact.Picture = null;
            noContact.UserID = -1;
            return noContact;
        }
    }

    public boolean removeContact(Contact contact){
        if(contactRepo.deleteContact(contact.UserID)){
            for(Contact c : contacts){
                if(c.UserID == contact.UserID){
                    contacts.remove(c);
                    break;
                }
            }
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