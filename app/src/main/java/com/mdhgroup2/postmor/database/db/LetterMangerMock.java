package com.mdhgroup2.postmor.database.db;

import androidx.lifecycle.LiveData;

import com.mdhgroup2.postmor.database.DTO.EditMsg;
import com.mdhgroup2.postmor.database.interfaces.ILetterManager;

import java.util.Date;


public class LetterMangerMock implements ILetterManager {

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
