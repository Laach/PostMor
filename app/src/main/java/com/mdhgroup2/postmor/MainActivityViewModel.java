package com.mdhgroup2.postmor;

import android.os.AsyncTask;

import com.mdhgroup2.postmor.database.DTO.Contact;
import com.mdhgroup2.postmor.database.DTO.EditMsg;
import com.mdhgroup2.postmor.database.DTO.MsgCard;
import com.mdhgroup2.postmor.database.interfaces.IAccountRepository;
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
    private final IAccountRepository accountRepo;
    public int screenWidthDp = 0;
    public int selectedContact = 0;
    private MutableLiveData<Boolean> alreadyLoggedIn;

    public MainActivityViewModel(){
        contactRepo = DatabaseClient.getMockContactRepository();
        boxRepo = DatabaseClient.getMockBoxRepository();
        accountRepo = DatabaseClient.getAccountRepository();
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

    public List<MsgCard> getMessageList(int index, int ID){
        return boxRepo.getAllMessages(ID);
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

    public int checkForNewMessages(){
        return boxRepo.fetchNewMessages();
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

    public boolean addUserToContacts (Contact friend){
        if(contactRepo.addContact(friend.UserID)){
            contacts.add(friend);
            return true;
        }
        return false;
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

    public void checkLoginStatus(){
        dbAlreadyLoggedIn status = new dbAlreadyLoggedIn();
        status.execute();
    }

    public MutableLiveData<Boolean> amILoggedIn(){
        if(alreadyLoggedIn == null){
            alreadyLoggedIn = new MutableLiveData<>();
        }
        return  alreadyLoggedIn;
    }

    private class dbAlreadyLoggedIn extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Void... v){
            Boolean result;
            try{
                result = accountRepo.isLoggedIn();
            }catch (Exception e){
                result = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            alreadyLoggedIn.postValue(result);
        }
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