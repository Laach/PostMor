package com.mdhgroup2.postmor.database.repository;

import androidx.lifecycle.LiveData;

import com.mdhgroup2.postmor.database.DTO.BoxMessage;
import com.mdhgroup2.postmor.database.DTO.MessageContent;
import com.mdhgroup2.postmor.database.DTO.MsgCard;
import com.mdhgroup2.postmor.database.Entities.Message;
import com.mdhgroup2.postmor.database.db.BoxDao;
import com.mdhgroup2.postmor.database.db.Converters;
import com.mdhgroup2.postmor.database.db.ManageDao;
import com.mdhgroup2.postmor.database.db.Utils;
import com.mdhgroup2.postmor.database.interfaces.IBoxRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

        Date now = new Date(System.currentTimeMillis());

        List<MsgCard> ms = new ArrayList<>();
        for (BoxMessage bm : bs) {
            if(bm.DeliveryTime == null || bm.DeliveryTime.before(now)){
                ms.add(boxMessageToCard(bm));
            }
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
            m.IsSentByMe = true;
        }
        return m;
    }

    @Override
    public int getNewMessageCount() {
        return boxdb.getNewMessageCount();
    }

    @Override
    public int outgoingLetterCount() {
        return managedb.getOutgoingLetterCount();
    }

    @Override
    public MessageContent getMsgContent(int MsgID) {
        return boxdb.getMsgContent(MsgID);
    }

    @Override
    public int fetchNewMessages(){
//        List<Message> msgs = boxdb.getAllMessagesFull();
//        int latestMessageId = -1;
//        for (Message m : msgs) {
//            if(m.ExternalMessageID > latestMessageId){
//                latestMessageId = m.ExternalMessageID;
//            }
//        }
        int latestMessageId = boxdb.getLatestId();

        String data  = String.format(Locale.US, "{" +
                "\"latestMessageId\" : %d" +
                "}", latestMessageId);

        try {
            int count = 0;
            JSONObject json = Utils.APIPost(Utils.baseURL + "/message/fetch/new", new JSONObject(data), managedb);
            if(json == null){
                return 0;
            }
            JSONArray arr = json.getJSONArray("messages");
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
//        msg.InternalMessageID;
        msg.IsDraft = false;
        msg.IsRead = false;
        msg.IsOutgoing = false;
        msg.TimeStamp = null;


        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        dateFormat.setLenient(false);
        Date d = Utils.parseDate(json.getString("deliveryTime"));
        if(d == null){
            return null;
        }
        msg.DeliveryTime = d;

        return msg;
    }
}
