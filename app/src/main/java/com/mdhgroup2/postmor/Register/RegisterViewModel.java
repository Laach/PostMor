package com.mdhgroup2.postmor.Register;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;

import com.mdhgroup2.postmor.R;
import com.mdhgroup2.postmor.database.DTO.Account;
import com.mdhgroup2.postmor.database.interfaces.IAccountRepository;
import com.mdhgroup2.postmor.database.repository.AccountRepository;
import com.mdhgroup2.postmor.database.repository.DatabaseClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {
    private final IAccountRepository accountDB;
    private Account myAccount;
    private String confirmPassword;
    private MutableLiveData<List<String>> myAddresses;
    private List<String> recievedAddresses;
    private Random rand = new Random();
    private MutableLiveData<List<String>> callResults;
    public String choosenAddress;
    private int numberOfAddress = 40;

    public MutableLiveData<List<String>> getResults(){
        if(callResults == null) {
            callResults = new MutableLiveData<>();
        }
        return callResults;
    }

    public MutableLiveData<List<String>> updateAddresses(){
        if(myAddresses == null){
            myAddresses = new MutableLiveData<>();
        }
        return  myAddresses;
    }

    public RegisterViewModel(){
        accountDB = DatabaseClient.getAccountRepository();
        myAccount = new Account();
        getAddressesFromDB(numberOfAddress);
    }

    public void generateAddresses(){
        myAccount.Address = recievedAddresses.get(rand.nextInt(numberOfAddress));
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

    public AccountRepository.PasswordStatus checkPasswordValidity(List<TextView> passwordhints) {

        List<AccountRepository.PasswordStatus> ErrorList = accountDB.isValidPassword(myAccount.Password);
        passwordhints.get(0).setText("");
        passwordhints.get(1).setText("");
        passwordhints.get(2).setText("");
        passwordhints.get(3).setText("");
        passwordhints.get(4).setText("");
        if (ErrorList.isEmpty()) {
            if(!myAccount.Password.equals(confirmPassword))
            {
                passwordhints.get(0).setText(R.string.password_validity_feedback_match);

                return AccountRepository.PasswordStatus.NotEqual;
            }
            return AccountRepository.PasswordStatus.Ok;
        }
        for (int i = 0, j = 0; i < ErrorList.size(); i++)
        {
            switch (ErrorList.get(i))
            {
                case ShorterThan6:passwordhints.get(j).setText(R.string.password_validity_feedback_length);
                    j++;
                    break;
                case NeedsLowerCase: passwordhints.get(j).setText(R.string.password_validity_feedback_lowercase);
                    j++;
                    break;
                case NeedsUppercase: passwordhints.get(j).setText(R.string.password_validity_feedback_uppercase);
                    j++;
                    break;
                case NeedsNumeric: passwordhints.get(j).setText(R.string.password_validity_feedback_numeric);
                    j++;
                    break;
                case NeedsNonAlphaNumeric: passwordhints.get(j).setText(R.string.password_validity_feedback_char);
                    j++;
                    break;
                default:
            }
        }
        return AccountRepository.PasswordStatus.NotEqual;
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
            myAddresses.setValue(addresses);
            recievedAddresses = addresses;
        }
    }
}
