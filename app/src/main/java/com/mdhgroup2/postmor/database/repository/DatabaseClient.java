package com.mdhgroup2.postmor.database.repository;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.RoomMasterTable;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomSQLiteQuery;

import com.mdhgroup2.postmor.database.DTO.Account;
import com.mdhgroup2.postmor.database.Entities.InternalMsgID;
import com.mdhgroup2.postmor.database.db.AccountBuilder;
import com.mdhgroup2.postmor.database.db.AppDatabase;
import com.mdhgroup2.postmor.database.db.BoxRepositoryMock;
import com.mdhgroup2.postmor.database.db.ContactRepositoryMock;
import com.mdhgroup2.postmor.database.db.Converters;
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
//        db = Room.databaseBuilder(c, AppDatabase.class, "client-db")
//                .build();
        db = DbDefaultData.DB(c);

        // ---------------------------------------------------------
        // This is required.
        db.manageDao().initInternalID(new InternalMsgID(100));
        // ---------------------------------------------------------

        Account nick = new AccountBuilder()
                .addName("Nick")
                .addPassword("String123!")
                .addAddress("Tittiegatan 6")
                .addEmail("nick@animetitties69-420.com")
                .addPicture(Converters.fromBase64("R0lGODlhAQABAIAAAMLCwgAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw"))
                .build();

//        boolean b = getAccountRepository().registerAccount(nick);

//        getAccountRepository().signIn("nick@animetitties.com", "String123!");

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

    static void nukeDatabase(){
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
