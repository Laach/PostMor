package com.mdhgroup2.postmor.database.interfaces;

import com.mdhgroup2.postmor.database.DTO.Account;

import java.util.List;

public interface IAccountManager {
    List<String> getRandomAddresses(int count);
    boolean isValidPassword(String pass);
    boolean register(Account account); // Use an AccountBuilder
    boolean signIn(String email, String pass);
    void signOut();

}
