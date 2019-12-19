package com.mdhgroup2.postmor.database.db;

import androidx.lifecycle.LiveData;

import com.mdhgroup2.postmor.database.DTO.EditMsg;
import com.mdhgroup2.postmor.database.interfaces.ILetterManager;

import java.time.LocalTime;

public class LetterMangerMock implements ILetterManager {

    @Override
    public String getPickupTime() {
        return "16:00";
    }

    @Override
    public LiveData<EditMsg> getOrStartDraft(int recipientID) {
        return null;
    }

    @Override
    public LiveData<EditMsg> getOrStartGenerictDraft() {
        return null;
    }

    @Override
    public void sendDraft(int recipientID) {

    }
}
