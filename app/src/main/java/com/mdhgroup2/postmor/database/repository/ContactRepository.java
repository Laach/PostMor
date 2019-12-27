package com.mdhgroup2.postmor.database.repository;

import com.mdhgroup2.postmor.database.DTO.Contact;
import com.mdhgroup2.postmor.database.DTO.UserCard;
import com.mdhgroup2.postmor.database.db.ContactDao;
import com.mdhgroup2.postmor.database.db.ManageDao;
import com.mdhgroup2.postmor.database.interfaces.IContactRepository;

import java.util.List;

public class ContactRepository implements IContactRepository {

    private ContactDao contactdao;
    private ManageDao managedao;

    public ContactRepository(ContactDao contactDao, ManageDao manageDao){
        this.contactdao = contactDao;
        this.managedao = manageDao;
    }

    @Override
    public UserCard findByAddress(String address) {
        UserCard u = new UserCard();

        return null;
    }

    @Override
    public List<Contact> getContacts() {
        return contactdao.getContacts();
    }

    @Override
    public void addContact(int ID) {
        // Query server
        contactdao.addFriend(ID);
    }

    @Override
    public void deleteContact(int ID) {
        // Query server
        contactdao.deleteFriend(ID);
    }

    @Override
    public UserCard getUserCard(int ID) {
        return contactdao.getUserCard(ID);
    }
}
