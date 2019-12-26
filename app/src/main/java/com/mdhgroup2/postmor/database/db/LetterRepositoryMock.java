package com.mdhgroup2.postmor.database.db;

import com.mdhgroup2.postmor.database.DTO.EditMsg;
import com.mdhgroup2.postmor.database.interfaces.ILetterRepository;

import java.util.Date;


public class LetterRepositoryMock implements ILetterRepository{

    @Override
    public Date getPickupTime() {
        return Utils.makeTime(16, 15, 0);
    }

    @Override
    public EditMsg getOrStartDraft(int recipientID) {
        return null;
    }

    @Override
    public EditMsg getOrStartGenerictDraft() {
        return null;
    }

    @Override
    public void saveDraft(EditMsg msg) {

    }

    @Override
    public void sendDraft(int recipientID) {

    }
}
