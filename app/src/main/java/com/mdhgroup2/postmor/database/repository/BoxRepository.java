package com.mdhgroup2.postmor.database.repository;

import androidx.lifecycle.LiveData;

import com.mdhgroup2.postmor.database.DTO.MessageContent;
import com.mdhgroup2.postmor.database.DTO.MsgCard;
import com.mdhgroup2.postmor.database.Entities.Message;
import com.mdhgroup2.postmor.database.db.BoxDao;
import com.mdhgroup2.postmor.database.db.ManageDao;
import com.mdhgroup2.postmor.database.db.Utils;
import com.mdhgroup2.postmor.database.interfaces.IBoxRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
        return boxdb.getAllMessages();
    }

    @Override
    public List<MsgCard> getAllMessages(int ID) {
        return boxdb.getAllMessages(ID);
    }

    @Override
    public List<MsgCard> getInboxMessages() {
        int uid = managedb.getUserId();
        return boxdb.getInboxMessages(uid);
    }

    @Override
    public List<MsgCard> getInboxMessages(int ID) {
        int uid = managedb.getUserId();
        return boxdb.getInboxMessages(uid, ID);
    }

    @Override
    public List<MsgCard> getOutboxMessages() {
        int uid = managedb.getUserId();
        return boxdb.getOutboxMessages(uid);
    }

    @Override
    public List<MsgCard> getOutboxMessages(int ID) {
        int uid = managedb.getUserId();
        return boxdb.getOutboxMessages(uid, ID);
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
//        List<Message> msgs = boxdb.getAllMessagesFull();
//        int latestId = -1;
//        for (Message m : msgs) {
//            if(m.ExternalMessageID > latestId){
//                latestId = m.ExternalMessageID;
//            }
//        }
//
//
//
//
//        String token = managedb.getAuthToken();
//        String data  = String.format(Locale.US, "{" +
//                "\"token\" : \"%s\", " +
//                "\"latestId\" : %d" +
//                "}", token, latestId);
//
//        List<JSONObject> messages = new ArrayList<>();
//
//        try {
//            JSONObject json = Utils.APIPost(Utils.baseURL + "/user/fetchnewmessages", new JSONObject(data));
//            JSONArray arr = json.getJSONArray("newmessages");
//            for(int i = 0; i < arr.length(); i++){
//                messages.add(arr.getJSONObject(i));
//            }
//        }
//        catch (JSONException e){
//            try {
//                managedb.refresh();
//                JSONObject json2 = Utils.APIPost(Utils.baseURL + "/user/fetchnewmessages", new JSONObject(data));
//            }
//            catch (JSONException | IOException e2){
//                return 0;
//            }
//            return 0;
//
//        }
//        catch (IOException e){
//           return 0;
//        }
//
//
        return 0;
    }
}
