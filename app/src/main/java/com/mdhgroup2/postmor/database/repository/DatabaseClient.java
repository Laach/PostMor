package com.mdhgroup2.postmor.database.repository;

import android.content.Context;

import androidx.room.Room;

import com.mdhgroup2.postmor.database.db.AppDatabase;
import com.mdhgroup2.postmor.database.interfaces.IBoxManager;
import com.mdhgroup2.postmor.database.interfaces.ILetterManager;
import com.mdhgroup2.postmor.database.repository.BoxRepository;

public class DatabaseClient {
    private static AppDatabase db;


    public static void initDb(Context c){
        db = Room.databaseBuilder(c, AppDatabase.class, "client-db")
                 .build();
    }

//    public static ILetterManager getLetterRepository(){
//
//    }
    public static IBoxManager getBoxRepository(){
        return new BoxRepository(db.boxDao());
    }
}
