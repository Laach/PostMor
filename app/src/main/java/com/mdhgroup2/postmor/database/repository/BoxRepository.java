package com.mdhgroup2.postmor.database.repository;

import androidx.lifecycle.LiveData;

import com.mdhgroup2.postmor.database.DTO.MessageContent;
import com.mdhgroup2.postmor.database.DTO.MsgCard;
import com.mdhgroup2.postmor.database.db.BoxDao;
import com.mdhgroup2.postmor.database.interfaces.IBoxManager;

import java.util.List;

public class BoxRepository implements IBoxManager {

    private BoxDao db;

    BoxRepository(BoxDao boxdao){
        db = boxdao;
    }

    @Override
    public List<MsgCard> getAllMessages() {
        return null;
    }

    @Override
    public List<MsgCard> getAllMessages(int ID) {
        return null;
    }

    @Override
    public List<MsgCard> getInboxMessages() {
        return null;
    }

    @Override
    public List<MsgCard> getInboxMessages(int ID) {
        return null;
    }

    @Override
    public List<MsgCard> getOutboxMessages() {
        return null;
    }

    @Override
    public List<MsgCard> getOutboxMessages(int ID) {
        return null;
    }

    @Override
    public LiveData<Integer> getNewMessageCount() {
        return null;
    }

    @Override
    public LiveData<Integer> outgoingLetterCount() {
        return null;
    }

    @Override
    public MessageContent getMsgContent(int MsgID) {
        return null;
    }
}
