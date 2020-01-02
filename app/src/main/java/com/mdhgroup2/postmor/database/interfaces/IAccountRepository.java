package com.mdhgroup2.postmor.database.interfaces;

import com.mdhgroup2.postmor.database.DTO.Account;
import com.mdhgroup2.postmor.database.repository.AccountRepository;

import java.util.List;

public interface IAccountRepository {
    List<String> getRandomAddresses(int count);
    AccountRepository.PasswordStatus isValidPassword(String pass);
    boolean registerAccount(Account account); // Use an AccountBuilder
    boolean signIn(String email, String pass);
    void signOut();

}
