package com.mdhgroup2.postmor.database.interfaces;

import androidx.lifecycle.LiveData;

import com.mdhgroup2.postmor.database.DTO.EditMsg;

import java.time.LocalTime;

public interface ILetterManager {
    LocalTime getPickupTime();
    LiveData<EditMsg> getOrStartDraft(int recipientID);
    LiveData<EditMsg> getOrStartGenerictDraft();
    void sendDraft(int recipientID);

}
