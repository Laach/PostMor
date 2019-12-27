package com.mdhgroup2.postmor.database.repository;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mdhgroup2.postmor.database.db.AppDatabase;
import com.mdhgroup2.postmor.database.db.BoxRepositoryMock;
import com.mdhgroup2.postmor.database.db.ContactRepositoryMock;
import com.mdhgroup2.postmor.database.db.LetterRepositoryMock;
import com.mdhgroup2.postmor.database.interfaces.IAccountRepository;
import com.mdhgroup2.postmor.database.interfaces.IBoxRepository;
import com.mdhgroup2.postmor.database.interfaces.IContactRepository;
import com.mdhgroup2.postmor.database.interfaces.ILetterRepository;

public class DatabaseClient {
    private static AppDatabase db;


    public static void initDb(Context c){
        db = Room.databaseBuilder(c, AppDatabase.class, "client-db")
                .fallbackToDestructiveMigration()
                .build();
    }

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
