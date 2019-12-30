package com.mdhgroup2.postmor.database.interfaces;

import androidx.lifecycle.LiveData;

import com.mdhgroup2.postmor.database.DTO.MessageContent;
import com.mdhgroup2.postmor.database.DTO.MsgCard;

import java.util.List;

public interface IBoxRepository {


    List<MsgCard> getAllMessages();  // All messages
    List<MsgCard> getAllMessages(int ID); // All messages to one person

    List<MsgCard> getInboxMessages();
    List<MsgCard> getInboxMessages(int ID);

    List<MsgCard> getOutboxMessages();
    List<MsgCard> getOutboxMessages(int ID);

    // Using Integer because LiveData can't use primitive int type.
    int getNewMessageCount();
    LiveData<Integer> outgoingLetterCount();

    MessageContent getMsgContent(int MsgID);

    int fetchNewMessages();


}
