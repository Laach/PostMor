package com.mdhgroup2.postmor.database.interfaces;

import com.mdhgroup2.postmor.database.DTO.Contact;
import com.mdhgroup2.postmor.database.DTO.UserCard;

import java.util.List;

public interface IContactManager {
    UserCard findByAddress(String address);
    List<Contact> getContacts();
    void deleteContact(int ID);
    UserCard getUserCard(int ID);

}
