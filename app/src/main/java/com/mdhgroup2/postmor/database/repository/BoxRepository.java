package com.mdhgroup2.postmor.database.repository;

import androidx.lifecycle.LiveData;

import com.mdhgroup2.postmor.database.DTO.MessageContent;
import com.mdhgroup2.postmor.database.DTO.MsgCard;
import com.mdhgroup2.postmor.database.db.BoxDao;
import com.mdhgroup2.postmor.database.db.ManageDao;
import com.mdhgroup2.postmor.database.interfaces.IBoxRepository;

import java.util.List;

public class BoxRepository implements IBoxRepository {

    private BoxDao boxdb;
    private ManageDao managedb;

    BoxRepository(BoxDao boxdao, ManageDao managedao){
        boxdb = boxdao;
        managedb = managedao;
    }

    @Override
    public List<MsgCard> getAllMessages() {
        return boxdb.getAllMessages();
    }

    @Override
    public List<MsgCard> getAllMessages(int ID) {
        return boxdb.getAllMessages(ID);
    }

    @Override
    public List<MsgCard> getInboxMessages() {
        int uid = managedb.getUserId();
        return boxdb.getInboxMessages(uid);
    }

    @Override
    public List<MsgCard> getInboxMessages(int ID) {
        int uid = managedb.getUserId();
        return boxdb.getInboxMessages(uid, ID);
    }

    @Override
    public List<MsgCard> getOutboxMessages() {
        int uid = managedb.getUserId();
        return boxdb.getOutboxMessages(uid);
    }

    @Override
    public List<MsgCard> getOutboxMessages(int ID) {
        int uid = managedb.getUserId();
        return boxdb.getOutboxMessages(uid, ID);
    }

    @Override
    public int getNewMessageCount() {
        return boxdb.getNewMessageCount();
    }

    @Override
    public LiveData<Integer> outgoingLetterCount() {
        return managedb.getOutgoingLetterCount();
    }

    @Override
    public MessageContent getMsgContent(int MsgID) {
        return boxdb.getMsgContent(MsgID);
    }
}
