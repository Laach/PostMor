package com.mdhgroup2.postmor.database.repository;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.provider.ContactsContract;

import androidx.room.Room;

import com.mdhgroup2.postmor.database.DTO.Account;
import com.mdhgroup2.postmor.database.Entities.InternalMsgID;
import com.mdhgroup2.postmor.database.db.AccountBuilder;
import com.mdhgroup2.postmor.database.db.AppDatabase;
import com.mdhgroup2.postmor.database.db.BoxRepositoryMock;
import com.mdhgroup2.postmor.database.db.ContactRepositoryMock;
import com.mdhgroup2.postmor.database.db.DbDefaultData;
import com.mdhgroup2.postmor.database.db.LetterRepositoryMock;
import com.mdhgroup2.postmor.database.interfaces.IAccountRepository;
import com.mdhgroup2.postmor.database.interfaces.IBoxRepository;
import com.mdhgroup2.postmor.database.interfaces.IContactRepository;
import com.mdhgroup2.postmor.database.interfaces.ILetterRepository;

public class DatabaseClient {
    private static AppDatabase db;


    public static void initDb(Context c){
        appContext = c;
//        c.deleteDatabase("client-db");
        db = Room.databaseBuilder(c, AppDatabase.class, "client-db")
                .build();
//        db = DbDefaultData.DB(c);

        // ---------------------------------------------------------
//         This is required the first time setting up the db.
        try {
            // This try will only succeed the first time when
            // setting up the database.
            db.manageDao().initInternalID(new InternalMsgID(100));
        }
        catch (SQLiteConstraintException ignore){
        }

        if(getAccountRepository().isLoggedIn()){
            getBoxRepository().fetchNewMessages();
        }
        // ---------------------------------------------------------

//        IAccountRepository repo = DatabaseClient.getAccountRepository();
//        boolean b = repo.signIn("nick.grannas@gmail.com", "String123!");
//        repo.signOut();
//        b = repo.signIn("nick.grannas@gmail.com", "String123!");

//        int i = 5000;
//        Account nick = new AccountBuilder()
//                .addName("Nick")
//                .addPassword("String123!")
//                .addAddress("Tittiegatan " + Integer.toString(i))
//                .addEmail("nick" + Integer.toString(i) + "@smalltitties.com")
//                .build();

//        boolean b = getAccountRepository().registerAccount(nick);
//        getContactRepository().addContact(123);

//        boolean b = getAccountRepository().registerAccount(nick);

//        getAccountRepository().signIn("nick53@smalltitties.com", "String123!");

//        boolean b = db.manageDao().refreshToken();

    }

    public static Context appContext;

    public static IBoxRepository getBoxRepository(){
        return new BoxRepository(db.boxDao(), db.manageDao());
    }

    public static IAccountRepository getAccountRepository(){
        return new AccountRepository(db.accountDao(), db.manageDao());
    }

    public static IContactRepository getContactRepository(){
        return new ContactRepository(db.contactDao(), db.manageDao());
    }

    public static ILetterRepository getLetterRepository(){
        return new LetterRepository(db.letterDao(), db.manageDao());
    }

    public static void nukeDatabase(){
        db.clearAllTables();
        db.manageDao().initInternalID(new InternalMsgID(100));
    }


    // Mock repositories
    public static IBoxRepository getMockBoxRepository(){
        return new BoxRepositoryMock();
    }

    public static IContactRepository getMockContactRepository(){
        return new ContactRepositoryMock();
    }

    public static ILetterRepository getMockLetterRepository(){
        return new LetterRepositoryMock();
    }
}
