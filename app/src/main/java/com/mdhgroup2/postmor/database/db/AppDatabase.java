package com.mdhgroup2.postmor.database.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.mdhgroup2.postmor.database.DTO.Contact;
import com.mdhgroup2.postmor.database.DTO.MessageContent;
import com.mdhgroup2.postmor.database.DTO.MsgCard;
import com.mdhgroup2.postmor.database.DTO.UserCard;
import com.mdhgroup2.postmor.database.Entities.InternalMsgID;
import com.mdhgroup2.postmor.database.Entities.Message;
import com.mdhgroup2.postmor.database.Entities.Settings;
import com.mdhgroup2.postmor.database.Entities.User;
import com.mdhgroup2.postmor.database.Entities.UserWithMessages;



@Database(
        entities = {
                Message.class,
                InternalMsgID.class,
                User.class,
                Settings.class

        },

        views = {
                MsgCard.class,
                MessageContent.class,
                UserCard.class,
                Contact.class
        },
        version = 1
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract BoxDao     boxDao    ();
    public abstract ManageDao  manageDao ();
    public abstract AccountDao accountDao();
    public abstract ContactDao contactDao();
    public abstract LetterDao  letterDao ();
}
