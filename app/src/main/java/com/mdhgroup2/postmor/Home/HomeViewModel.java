package com.mdhgroup2.postmor.Home;

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


    public HomeViewModel(){
        letterRepo = (ILetterRepository) DatabaseClient.getMockLetterRepository();
        boxRepo = (IBoxRepository) DatabaseClient.getMockBoxRepository();
    }

    public String getOutgoingLetterCount(){
        return String.valueOf(boxRepo.outgoingLetterCount());
    }

    public String getEmptyTime(){
        Date d = letterRepo.getPickupTime();
        SimpleDateFormat x =  new SimpleDateFormat("hh:mm");
        return x.format(d);
    }




}
