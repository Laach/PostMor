package com.mdhgroup2.postmor;

import android.os.AsyncTask;
import android.util.Log;

import com.mdhgroup2.postmor.database.DTO.Contact;
import com.mdhgroup2.postmor.database.DTO.EditMsg;
import com.mdhgroup2.postmor.database.DTO.MsgCard;
import com.mdhgroup2.postmor.database.interfaces.IAccountRepository;
import com.mdhgroup2.postmor.database.interfaces.IContactRepository;
import com.mdhgroup2.postmor.database.interfaces.IBoxRepository;
import com.mdhgroup2.postmor.database.repository.DatabaseClient;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    private final IContactRepository contactRepo;
    private final IBoxRepository boxRepo;
    private Contact chosenRecipient;
    private final IAccountRepository accountRepo;
    private MutableLiveData<Boolean> alreadyLoggedIn;

    public MainActivityViewModel(){
        contactRepo = DatabaseClient.getContactRepository();
        boxRepo = DatabaseClient.getBoxRepository();
        accountRepo = DatabaseClient.getAccountRepository();
        chosenRecipient = null;
        chosenRec.setValue(null);
    }

    public List<Contact> getContactList(){
        try {

            return new GetContactsTask().execute().get();
        }
        catch(Exception e){

        }
        return new ArrayList<>();
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


    public int checkForNewMessages(){
        return boxRepo.fetchNewMessages();
    }

    public boolean removeContact(Contact contact){
        return contactRepo.deleteContact(contact.UserID);
    }

    public Contact findUserByAddress(String address){
        FindContactByAddressTask task = new FindContactByAddressTask();
        task.execute(address);
        try {
            return task.get();
        }
        catch(Exception e){
            return null;
        }
    }

    public boolean addUserToContacts (Contact friend){
        if(contactRepo.addContact(friend.UserID)){
//            contacts.add(friend);
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


//    public void chooseRecipient(int id){
//        try{
//
//            chosenRecipient = new GetContactByIdAsync().execute(id).get();
//        }
//        catch (ExecutionException | InterruptedException e){
//            chosenRecipient = null;
//        }
//        chosenRec.postValue(chosenRecipient);
//    }

    public void chooseRecipientById(int id){
        try{

            chosenRecipient = new GetContactByIdAsync().execute(id).get();
        }
        catch (ExecutionException | InterruptedException e){
            chosenRecipient = null;
        }
        chosenRec.postValue(chosenRecipient);
    }

    public Contact getChosenRecipient(){
        return chosenRecipient;
    }

    public void removeRecipient(){
        chosenRecipient = null;
        chosenRec.postValue(chosenRecipient);
    }

    private class GetContactsTask extends AsyncTask<Void, Void, List<Contact>>{

        @Override
        protected List<Contact> doInBackground(Void... voids) {
            return contactRepo.getContacts();
        }
    }

    private class FindContactByAddressTask extends AsyncTask<String, Void, Contact>{

        @Override
        protected Contact doInBackground(String... address ) {
            return contactRepo.findByAddress(address[0]);
        }
    }

    public class GetContactByIdAsync extends AsyncTask<Integer, Void, Contact>{

        @Override
        protected Contact doInBackground(Integer... id) {
            return getContactById(id[0]);
        }
    }

}