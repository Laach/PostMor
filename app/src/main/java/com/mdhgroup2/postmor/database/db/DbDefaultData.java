package com.mdhgroup2.postmor.database.db;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mdhgroup2.postmor.database.DTO.MsgCard;
import com.mdhgroup2.postmor.database.Entities.InternalMsgID;
import com.mdhgroup2.postmor.database.Entities.Message;
import com.mdhgroup2.postmor.database.Entities.User;

public class DbDefaultData {
    public static AppDatabase initDb(Context c) {

        AppDatabase db =
                Room.databaseBuilder(c, AppDatabase.class, "client-db")
                        .build();

        initUsers(db);
        initMessages(db);

        return db;
    }

    private static void initUsers(AppDatabase db){
        User u1 = new User();
        u1.Name = "Ann-Marie Josefsson";
        u1.Address = "Isterbarnsgatan 12";
        u1.ProfilePicture = null;
        u1.IsFriend = true;
        u1.ID = 1;
        u1.PublicKey = "1111";

        User u2 = new User();
        u2.Name = "Arne Askersund";
        u2.Address = "Mastrostvägen 13";
        u2.ProfilePicture = null;
        u1.IsFriend = true;
        u2.ID = 2;
        u2.PublicKey = "2222";

        User u3 = new User();
        u3.Name = "Swedish Chef";
        u3.Address = "Muppetgatan 14";
        u3.ProfilePicture = null;
        u1.IsFriend = false;
        u3.ID = 3;
        u3.PublicKey = "3333";

        User u4 = new User();
        u4.Name = "Brittish Chef";
        u4.Address = "Fish'n chipsstreet 9";
        u4.ProfilePicture = null;
        u4.IsFriend = false;
        u4.ID = 4;
        u4.PublicKey = "4444";

        db.manageDao().addUser(u1);
        db.manageDao().addUser(u2);
        db.manageDao().addUser(u3);
        db.manageDao().addUser(u4);

    }

    private static void initMessages(AppDatabase db){
       Message m1 = new Message();
       m1.UserID = 2;
       m1.WrittenBy = 1;
       m1.InternalMessageID = 1;
       m1.ExternalMessageID = 1;
       m1.IsDraft = false;
       m1.IsOutgoing = false;
       m1.IsRead = false;
       m1.TimeStamp = Utils.makeDateTime(2019, 8, 12, 12, 0, 0);
       m1.DeliveryTime = Utils.makeDateTime(2019, 8, 14, 16, 0, 0);
//       m1.SenderPublicKey = "2222";

       db.manageDao().addMessage(m1);
    }


//    public static AppDatabase db;

    public static AppDatabase DB(Context c){
        c.deleteDatabase("client-db");
        return DbDefaultData.initDb(c);
    }
}
