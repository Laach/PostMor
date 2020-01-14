package com.mdhgroup2.postmor.database.interfaces;

import android.graphics.Bitmap;

import com.mdhgroup2.postmor.database.DTO.Account;
import com.mdhgroup2.postmor.database.repository.AccountRepository;

import java.io.IOException;
import java.util.List;

public interface IAccountRepository {
    List<String> getRandomAddresses(int count) throws IOException;
    List<AccountRepository.PasswordStatus> isValidPassword(String pass);
    List<String> registerAccount(Account account); // Use an AccountBuilder
    List<String> signIn(String email, String pass);
    void signOut();

    String getMyName();
    String getMyEmail();
    String getMyAddress();
    Bitmap getMyProfilePicture();
    boolean isLoggedIn();



}
