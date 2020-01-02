package com.mdhgroup2.postmor.database.interfaces;

import com.mdhgroup2.postmor.database.DTO.Contact;

import java.util.List;

public interface IContactRepository {
    Contact findByAddress(String address);
    List<Contact> getContacts();
    boolean addContact(int ID);
    boolean deleteContact(int ID);
    Contact getUserCard(int ID);

}
