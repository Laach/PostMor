package com.mdhgroup2.postmor.database.repository;

import android.graphics.Bitmap;

import com.mdhgroup2.postmor.database.DTO.EditMsg;
import com.mdhgroup2.postmor.database.Entities.Message;
import com.mdhgroup2.postmor.database.db.Converters;
import com.mdhgroup2.postmor.database.db.LetterDao;
import com.mdhgroup2.postmor.database.db.ManageDao;
import com.mdhgroup2.postmor.database.db.Utils;
import com.mdhgroup2.postmor.database.interfaces.ILetterRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LetterRepository implements ILetterRepository {

    private LetterDao letterdao;
    private ManageDao managedao;

    LetterRepository(LetterDao letterDao, ManageDao manageDao){
        this.letterdao = letterDao;
        this.managedao = manageDao;
    }

    @Override
    public Date getPickupTime() {
        return managedao.getUserPickupTime();
    }

    @Override
    public EditMsg getOrStartGenericDraft() {
        EditMsg msg = letterdao.getGenericDraft();
        if(msg == null){
            msg = new EditMsg();
            msg.InternalMessageID = managedao.getNewMsgId();
            msg.RecipientID = 0;
            msg.IsDraft = true;

            createNewDraft(msg.InternalMessageID);
        }
        return msg;
    }

    @Override
    public EditMsg getOrStartDraft(int recipientID) {
        EditMsg msg = letterdao.getDraftById(recipientID);
        if(msg == null){
            msg = new EditMsg();
            msg.InternalMessageID = managedao.getNewMsgId();
            msg.RecipientID = recipientID;
            msg.IsDraft = true;

            createNewDraft(msg.InternalMessageID, recipientID);
        }
        return msg;
    }

    private void createNewDraft(int msgId){
        Message msg = draftDefaults(msgId);
        msg.UserID = null;
        managedao.addMessage(msg);
    }

    private void createNewDraft(int msgId, int recipientID){
        Message msg = draftDefaults(msgId);
        msg.UserID = recipientID;
        managedao.addMessage(msg);
    }

    private Message draftDefaults(int msgId){
        Message msg = new Message();
        msg.InternalMessageID = msgId;
        msg.ExternalMessageID = 0;
        msg.WrittenBy = managedao.getUserId();
        msg.IsDraft = true;
        msg.IsOutgoing = false;
        msg.IsRead = true;
        msg.TimeStamp = null;
        msg.DeliveryTime = null;
//        msg.SenderPublicKey = managedao.getUserPublicKey();
        msg.Images = null;
        msg.Text = null;
        return msg;
    }

    @Override
    public void saveDraft(EditMsg edit) {
        Message msg = letterdao.getMessageById(edit.InternalMessageID);
        msg.Text = edit.Text;
        msg.Images = edit.Images;
        msg.UserID = edit.RecipientID;

        letterdao.updateMessage(msg);
    }

    @Override
    public boolean sendDraft(EditMsg edit) {
        if(edit.Images != null && edit.Images.size() > 3){
            return false;
        }
        Message msg = letterdao.getMessageById(edit.InternalMessageID);
        if(msg == null){
            return false;
        }
        msg.Text = edit.Text;
        msg.Images = edit.Images;
        msg.UserID = edit.RecipientID;

        // Save the draft before sending
        letterdao.updateMessage(msg);



        String type = "text";
        if(msg.Text == null || msg.Text.equals("")){
            if(msg.Images == null || msg.Images.size() == 0){
                return false;
            }
            type = "images";
        }

        String array = "";
        if(type.equals("images")){
            for (int i = 0; i < msg.Images.size(); i++) {
                if(i == 0){
                    array = "\"" + Converters.bitmapToBase64(msg.Images.get(i)) + "\"";
                }
                else{
                    array = array + ", \"" + Converters.bitmapToBase64(msg.Images.get(i)) + "\"";
                }
            }
        }
        else {
            array = String.format("\"%s\"", msg.Text);
        }

        String data = String.format(Locale.US, "{" +
                "\"type\" : \"%s\", " +
                "\"contactId\" : %d, " +
                "\"message\" : [ %s ]"+
                "}", type, msg.UserID, array);

        try {
            JSONObject json = Utils.APIPost(Utils.baseURL + "/message/send", new JSONObject(data), managedao);

            int msgId = json.getInt("messageId");
            Date timestamp = Utils.parseDate(json.getString("timestamp"));
            msg.ExternalMessageID = msgId;
            msg.IsDraft = false;
            msg.IsOutgoing = true;
            letterdao.updateMessage(msg);
            letterdao.incrementOutoing();

        }
        catch (IOException | JSONException e){
            return false;

        }
        // Send to server

        return true;
    }
}
