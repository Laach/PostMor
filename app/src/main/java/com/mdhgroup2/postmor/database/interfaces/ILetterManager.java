package com.mdhgroup2.postmor.database.interfaces;

import androidx.lifecycle.LiveData;

import com.mdhgroup2.postmor.database.DTO.EditMsg;

import java.time.LocalTime;
import java.util.Date;

public interface ILetterManager {
    Date getPickupTime();
    EditMsg getOrStartDraft(int recipientID);
    EditMsg getOrStartGenerictDraft();
    void saveDraft(EditMsg msg);
    void sendDraft(int recipientID);

}
