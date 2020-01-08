package com.mdhgroup2.postmor.Register;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.mdhgroup2.postmor.database.DTO.Account;
import com.mdhgroup2.postmor.database.interfaces.IAccountRepository;
import com.mdhgroup2.postmor.database.repository.AccountRepository;
import com.mdhgroup2.postmor.database.repository.DatabaseClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {
    private final IAccountRepository accountDB;
    private Account myAccount;
    private String confirmPassword;
    private List<String> myAddresses = new ArrayList<>();
    private Random rand = new Random();
    private MutableLiveData<List<String>> callResults;
    public String choosenAddress;
    private int numberOfAddress = 20;

    public MutableLiveData<List<String>> getResults(){
        if(callResults == null) {
            callResults = new MutableLiveData<>();
        }
        return callResults;
    }

    public RegisterViewModel(){
        accountDB = DatabaseClient.getAccountRepository();
        myAccount = new Account();
        getAddressesFromDB(numberOfAddress);
    }

    public void generateAddresses(){
        myAccount.Address = myAddresses.get(rand.nextInt(numberOfAddress));
    }

    public void getAddressesFromDB(int amount){
        dbAddresses get = new dbAddresses();
        get.execute(amount);
    }

    public void setAddress(String address){
        choosenAddress = address;
    }

    public String getAddress(){return myAccount.Address;}

    public void register(){
        dbRegister reg = new dbRegister();
        reg.execute(myAccount);
    }

    public void setAccoutName(String name){
        myAccount.Name = name;
    }

    public String getAccountName(){return myAccount.Name;}

    public void setAccountEmail(String email){
        myAccount.Email = email;
    }

    public String getAccountEmail(){return myAccount.Email;}

    public void setAccountImage(Bitmap image){
        myAccount.Picture = image;
    }

    public Bitmap getAccountImage(){
        return myAccount.Picture;
    }

    public void setAccountPassword(String password){
        myAccount.Password = password;
    }

    public void setAccountConfirmPassword(String password){confirmPassword = password;}

    public AccountRepository.PasswordStatus checkPasswordValidity(){
        if(myAccount.Password == null || confirmPassword == null){
            return AccountRepository.PasswordStatus.ShorterThan6;
        }
        AccountRepository.PasswordStatus valid = accountDB.isValidPassword(myAccount.Password);
        if(valid == AccountRepository.PasswordStatus.Ok);
        {
            if(!myAccount.Password.equals(confirmPassword)) {
                return AccountRepository.PasswordStatus.NotEqual;
            }
            return AccountRepository.PasswordStatus.Ok;
        }
    }

    public String validateAccountInformation(){
        if(myAccount.Email == null || myAccount.Email.equals(""))
            return "Email field cannot be empty";
        else if (myAccount.Address == null)
            return "Address field cannot be empty, press the regenerate button.";
        else if (myAccount.Name == null || myAccount.Name.equals(""))
            return "Name field cannot be empty";
        return "True";
    }

    //Async task for registering
    private class dbRegister extends AsyncTask<Account, Void, List<String>>{
        @Override
        protected List<String> doInBackground(Account... accounts) {
            List<String> result = new ArrayList<>();
            try {
                result = accountDB.registerAccount(accounts[0]);
            }catch(Exception e){
                result.add(e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<String> result){
            callResults.postValue(result);
        }
    }

    //Async task for getting the addresses
    private class dbAddresses extends AsyncTask<Integer, Void, List<String>>{
        @Override
        protected List<String> doInBackground(Integer... integer){
            List<String> result = new ArrayList<>();
            try {
                result = accountDB.getRandomAddresses(integer[0]);
            }
            catch (IOException e){
                for(int i = 0; i < integer[0]; i++){
                    result.add("No addresses found, are you connected to the internet?");
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<String> addresses){
            myAddresses = addresses;
        }
    }
}
