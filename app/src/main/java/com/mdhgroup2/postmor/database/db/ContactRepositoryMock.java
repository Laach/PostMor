package com.mdhgroup2.postmor.database.db;


import com.mdhgroup2.postmor.R;
import com.mdhgroup2.postmor.database.DTO.Contact;
import com.mdhgroup2.postmor.database.interfaces.IContactRepository;
import com.mdhgroup2.postmor.database.repository.DatabaseClient;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ContactRepositoryMock implements IContactRepository {

    @Override
    public Contact findByAddress(String address) {
        if (address.equals("1")){Contact u3 = new Contact();
            u3.Name = "A";
            u3.Address = "1";
            u3.Picture = Converters.fromBase64("R0lGODlhAQABAIAAAMLCwgAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw");
            u3.UserID = 4;
            u3.IsFriend = false;
            return u3;}
        if (address.equals("2")){Contact u3 = new Contact();
            u3.Name = "B";
            u3.Address = "2";
            u3.Picture = null;
            u3.UserID = 5;
            u3.IsFriend = false;
            return u3;}
        if (address.equals("3")){Contact u3 = new Contact();
            u3.Name = "C";
            u3.Address = "3";
            u3.Picture = null;
            u3.UserID = 6;
            u3.IsFriend = false;
            return u3;}
        if (address.equals("4")){Contact u3 = new Contact();
            u3.Name = "D";
            u3.Address = "4";
            u3.Picture = null;
            u3.UserID = 7;
            u3.IsFriend = true;
            return u3;}
        if (address.equals("5")){Contact u3 = new Contact();
            u3.Name = "E";
            u3.Address = "5";
            u3.Picture = null;
            u3.UserID = 8;
            u3.IsFriend = false;
            return u3;}
        else {
            return null;}

    }

    @Override
    public List<Contact> getContacts() {
        Contact u1 = new Contact();
        u1.Name = "Ann-Marie Josefsson";
        u1.Address = "Isterbarnsgatan 12";
        u1.Picture = Converters.fromBase64("R0lGODlhAQABAIAAAMLCwgAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw");
        u1.UserID = 1;

        Contact u2 = new Contact();
        u2.Name = "Arne Askersund";
        u2.Address = "Mastrostvägen 13";

        InputStream myObj = DatabaseClient.appContext.getResources().openRawResource(R.raw.image);

        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
//            System.out.println(data);
            u2.Picture = Converters.fromBase64(data);
        }
        myReader.close();

        u2.UserID = 2;

        Contact u3 = new Contact();
        u3.Name = "Swedish Chef";
        u3.Address = "Muppetgatan 14";
        u3.Picture = Converters.fromBase64("R0lGODlhAQABAIAAAAUEBAAAACwAAAAAAQABAAACAkQBADs");
        u3.UserID = 3;
        List<Contact> l = new ArrayList<>();

        l.add(u1);
        l.add(u2);
        l.add(u3);

        return l;
    }

    @Override
    public boolean addContact(int ID) {
        return true;
    }

    @Override
    public boolean deleteContact(int ID) {
        return true;
    }

    @Override
    public Contact getUserCard(int ID) {
        Contact u2 = new Contact();
        u2.Name = "[User with ID " + ID + "]";
        u2.Address = "Mastrostvägen 13";
        u2.Picture = null;
        u2.UserID = ID;
        u2.IsFriend = false;

        return u2;
    }
}
