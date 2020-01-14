package com.mdhgroup2.postmor.Home;

import android.graphics.Bitmap;
import android.provider.ContactsContract;

import androidx.lifecycle.ViewModel;

import com.mdhgroup2.postmor.database.interfaces.IAccountRepository;
import com.mdhgroup2.postmor.database.interfaces.IBoxRepository;
import com.mdhgroup2.postmor.database.interfaces.ILetterRepository;
import com.mdhgroup2.postmor.database.repository.DatabaseClient;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private final ILetterRepository letterRepo;
    private final IBoxRepository boxRepo;
    private final IAccountRepository accountRepo;


    public HomeViewModel(){
        letterRepo = DatabaseClient.getLetterRepository();
        boxRepo = DatabaseClient.getBoxRepository();
        accountRepo = DatabaseClient.getAccountRepository();
    }

    public Bitmap getOwnProfilePicture()
    {
        return accountRepo.getMyProfilePicture();
    }
    public String getOwnName(){
        return  accountRepo.getMyName();
    }
    public String getOwnAddress(){
        return  accountRepo.getMyAddress();
    }

    public String getOutgoingLetterCount(){
        return String.valueOf(boxRepo.outgoingLetterCount());
    }

    public String getEmptyTime(){
        Date d = letterRepo.getPickupTime();
        SimpleDateFormat x =  new SimpleDateFormat("HH:mm");
        return x.format(d);
    }




}
