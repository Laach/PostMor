package com.mdhgroup2.postmor.database.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Database;

import com.mdhgroup2.postmor.database.DTO.BoxMessage;
import com.mdhgroup2.postmor.database.DTO.MessageContent;
import com.mdhgroup2.postmor.database.DTO.MsgCard;
import com.mdhgroup2.postmor.database.Entities.Message;
import com.mdhgroup2.postmor.database.db.BoxDao;
import com.mdhgroup2.postmor.database.db.ContactDao;
import com.mdhgroup2.postmor.database.db.Converters;
import com.mdhgroup2.postmor.database.db.ManageDao;
import com.mdhgroup2.postmor.database.db.Utils;
import com.mdhgroup2.postmor.database.interfaces.IBoxRepository;
import com.mdhgroup2.postmor.database.interfaces.IContactRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class BoxRepository implements IBoxRepository {

    private BoxDao boxdb;
    private ManageDao managedb;

    BoxRepository(BoxDao boxdao, ManageDao managedao){
        boxdb = boxdao;
        managedb = managedao;
    }

    @Override
    public List<MsgCard> getAllMessages() {
        return boxMessageListToCards(boxdb.getAllMessages());
    }

    @Override
    public List<MsgCard> getAllMessages(int ID) {
        return boxMessageListToCards(boxdb.getAllMessages(ID));
    }

    @Override
    public List<MsgCard> getInboxMessages() {
        int uid = managedb.getUserId();
        return boxMessageListToCards(boxdb.getInboxMessages(uid));
    }

    @Override
    public List<MsgCard> getInboxMessages(int ID) {
        int uid = managedb.getUserId();
        return boxMessageListToCards(boxdb.getInboxMessages(uid, ID));
    }

    @Override
    public List<MsgCard> getOutboxMessages() {
        int uid = managedb.getUserId();
        return boxMessageListToCards(boxdb.getOutboxMessages(uid));
    }

    @Override
    public List<MsgCard> getOutboxMessages(int ID) {
        int uid = managedb.getUserId();
        return boxMessageListToCards(boxdb.getOutboxMessages(uid, ID));
    }

    private List<MsgCard> boxMessageListToCards(List<BoxMessage> bs){
        if(bs == null){ return null; }

        List<MsgCard> ms = new ArrayList<>();
        for (BoxMessage b : bs) {
            ms.add(boxMessageToCard(b));
        }
        return ms;
    }

    private MsgCard boxMessageToCard(BoxMessage b){
        MsgCard m = new MsgCard();
        m.MsgID = b.MsgID;
        m.DateStamp = b.DateStamp;
        m.IsFriend = b.IsFriend;
        m.Address = b.Address;
        m.Name = b.Name;
        m.Picture = b.Picture;
        m.UserID = b.UserID;
        m.Text = b.Text;
        m.Images = b.Images;

        if(b.UserID == b.SenderID){
            m.IsSentByMe = false;
        }
        else{
            m.IsSentByMe = false;
        }
        return m;
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

    @Override
    public int fetchNewMessages(){
        List<Message> msgs = boxdb.getAllMessagesFull();
        int latestMessageId = -1;
        for (Message m : msgs) {
            if(m.ExternalMessageID > latestMessageId){
                latestMessageId = m.ExternalMessageID;
            }
        }

        String token = managedb.getAuthToken();
        String data  = String.format(Locale.US, "{" +
                "\"token\" : \"%s\", " +
                "\"latestMessageId\" : %d" +
                "}", token, latestMessageId);

        try {
            int count = 0;
            JSONArray arr = Utils.APIPostArray(Utils.baseURL + "/message/fetch/new", new JSONObject(data));
            Message msg;
            for(int i = 0; i < arr.length(); i++){
                msg = newMessageFromJson(arr.getJSONObject(i));
                if(msg != null){
                    managedb.addMessage(msg);
                    count++;
                }
            }
            return count;
        }
        catch (JSONException e){
            return 0;
        }
        catch (IOException e){
            return 0;
        }
    }

    private Message newMessageFromJson(JSONObject json) throws JSONException {
        Message msg = new Message();
        int senderId = json.getInt("senderId");
        managedb.downloadUserInfo(senderId);

        String type = json.getString("type");
        if(type.equals("text")){
            msg.Text = json.getJSONArray("content").getString(0);
        }
        else{
            msg.Images = new ArrayList<>();
            JSONArray arr = json.getJSONArray("content");
            for(int i = 0; i < arr.length(); i++){
                msg.Images.add(Converters.fromBase64(arr.getString(i)));
            }
        }

        msg.ExternalMessageID = json.getInt("messageId");
        msg.UserID = senderId;
        msg.WrittenBy = senderId;
        msg.InternalMessageID = managedb.getNewMsgId();
        msg.IsDraft = false;
        msg.IsRead = false;
        msg.IsOutgoing = false;
        msg.TimeStamp = null;


        try {
//            DateFormat dateFormat = new SimpleDateFormat("hh:mm dd/MM/yy");
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
//            "2019-12-30T14:10:44.2522414Z"
            dateFormat.setLenient(false);
            Date d = dateFormat.parse(json.getString("deliveryTime"));
            msg.DeliveryTime = d;
        }
        catch (ParseException e){
            return null;
        }

        return msg;
    }
}
