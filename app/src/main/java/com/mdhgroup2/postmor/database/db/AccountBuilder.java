package com.mdhgroup2.postmor.database.db;

import android.graphics.Bitmap;

import com.mdhgroup2.postmor.database.DTO.Account;

public class AccountBuilder {

    // Picture is not necessary. If any of the others are null/empty it throws an exception.


    /*
    Usage example

    AccountBuilder builder = new AccountBuilder();

    Account = builder.addName("Bob")
              .addEmail("BobsBurgers@bob.com")
              .addPassword("tinasBurgers")
              .addAddress("Bobstreet 5")
              .build();

     */

    String Name;
    String Email;
    String Password;
    String Address;
    Bitmap Picture;

    public AccountBuilder addName(String name){
        Name = name;
        return this;
    }

    public AccountBuilder addEmail(String email){
        Email = email;
        return this;
    }
    public AccountBuilder addAddress(String address){
        Address = address;
        return this;
    }
    public AccountBuilder addPassword(String password){
        Password = password;
        return this;
    }
    public AccountBuilder addPicture(Bitmap picture){
        Picture = picture;
        return this;
    }

    public Account build(){
        if(Name == null || Name.equals("")
                || Email    == null || Email   .equals("")
                || Password == null || Password.equals("")
                || Address  == null || Address .equals("")){
            throw new IllegalArgumentException("One or more arguments were null or empty, or not set.");
        }

        Account acc = new Account();
        acc.Name = Name;
        acc.Address = Address;
        acc.Password = Password;
        acc.Email = Email;
        acc.Picture = Picture;

        return acc;

    }


}
