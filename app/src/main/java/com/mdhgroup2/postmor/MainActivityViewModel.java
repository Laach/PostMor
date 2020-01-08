package com.mdhgroup2.postmor;

import com.mdhgroup2.postmor.Compose.Compose2HandwrittenViewModel;
import com.mdhgroup2.postmor.database.DTO.Contact;
import com.mdhgroup2.postmor.database.DTO.EditMsg;
import com.mdhgroup2.postmor.database.DTO.MsgCard;
import com.mdhgroup2.postmor.database.interfaces.IContactRepository;
import com.mdhgroup2.postmor.database.interfaces.IBoxRepository;
import com.mdhgroup2.postmor.database.repository.DatabaseClient;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    private final List<Contact> contacts;
    private final IContactRepository contactRepo;
    private final IBoxRepository boxRepo;
    private Contact chosenRecipient;

    public MainActivityViewModel(){
        contactRepo = (IContactRepository) DatabaseClient.getMockContactRepository();
        boxRepo = (IBoxRepository) DatabaseClient.getMockBoxRepository();
        contacts = contactRepo.getContacts();
        chosenRecipient = null;
        chosenRec.setValue(null);
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


    private MutableLiveData<Contact> chosenRec = new MutableLiveData<>();

    public MutableLiveData<Contact> getChosenRec(){
        if(chosenRec == null) {
            chosenRec = new MutableLiveData<>();
        }
        return chosenRec;
    }


    public void chooseRecipient(int index){
        chosenRecipient = getContact(index);
        chosenRec.postValue(chosenRecipient);
    }

    public Contact getChosenRecipient(){
        return chosenRecipient;
    }

    public void removeRecipient(){
        chosenRecipient = null;
        chosenRec.postValue(chosenRecipient);
    }
}