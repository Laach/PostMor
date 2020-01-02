package com.mdhgroup2.postmor.Register;
import android.graphics.Bitmap;

import com.mdhgroup2.postmor.database.DTO.Account;
import com.mdhgroup2.postmor.database.interfaces.IAccountRepository;
import com.mdhgroup2.postmor.database.repository.DatabaseClient;

import java.util.List;

import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {
    private final IAccountRepository accountDB;
    private Account myAccount;

    public RegisterViewModel(){
        accountDB = DatabaseClient.getAccountRepository();
        myAccount = new Account();
    }

    public List<String> getAddresses(){
        return accountDB.getRandomAddresses(10);
    }

    public void register(){
        accountDB.registerAccount(myAccount);
    }

    public void setAccoutName(String name){
        myAccount.Name = name;
    }

    public void setAccountEmail(String email){
        myAccount.Email = email;
    }

    public void setAccountAddress(String address){
        myAccount.Address = address;
    }

    public void setAccountImage(Bitmap image){
        myAccount.Picture = image;
    }

    public void setAccountPassword(String password){
        myAccount.Password = password;
    }
}
