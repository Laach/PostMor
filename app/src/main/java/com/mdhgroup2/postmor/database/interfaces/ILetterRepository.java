package com.mdhgroup2.postmor.database.interfaces;

import androidx.lifecycle.LiveData;

import com.mdhgroup2.postmor.database.DTO.EditMsg;

import java.time.LocalTime;
import java.util.Date;

public interface ILetterRepository {
    Date getPickupTime();
    EditMsg getOrStartDraft(int recipientID);
    EditMsg getOrStartGenericDraft();
    void saveDraft(EditMsg msg);
    boolean sendDraft(EditMsg msg);

}
