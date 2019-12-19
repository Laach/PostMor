package com.mdhgroup2.postmor.database.db;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.mdhgroup2.postmor.R;
import com.mdhgroup2.postmor.database.DTO.Contact;
import com.mdhgroup2.postmor.database.DTO.UserCard;
import com.mdhgroup2.postmor.database.interfaces.IContactManager;

import java.util.ArrayList;
import java.util.List;

public class ContactManagerMock implements IContactManager {

    @Override
    public UserCard findByAddress(String address) {
        UserCard u3 = new UserCard();
        u3.Name = "Swedish Chef";
        u3.Address = "Muppetgatan 14";
        u3.Picture = null;
        u3.UserID = 3;
        u3.IsFriend = true;
        
        return u3;
    }

    @Override
    public List<Contact> getContacts() {
        Contact u1 = new Contact();
        u1.Name = "Ann-Marie Josefsson";
        u1.Address = "Isterbarnsgatan 12";
        u1.Picture = null;
        u1.UserID = 1;

        Contact u2 = new Contact();
        u2.Name = "Arne Askersund";
        u2.Address = "Mastrostvägen 13";
        u2.Picture = null;
        u2.UserID = 2;

        Contact u3 = new Contact();
        u3.Name = "Swedish Chef";
        u3.Address = "Muppetgatan 14";
        u3.Picture = null;
        u3.UserID = 3;
        List<Contact> l = new ArrayList<>();

        l.add(u1);
        l.add(u2);
        l.add(u3);

        return l;
    }

    @Override
    public void addContact(int ID) {

    }

    @Override
    public void deleteContact(int ID) {

    }

    @Override
    public UserCard getUserCard(int ID) {
        UserCard u2 = new UserCard();
        u2.Name = "Arne Askersund";
        u2.Address = "Mastrostvägen 13";
        u2.Picture = null;
        u2.UserID = 2;
        u2.IsFriend = false;

        return u2;
    }
}
