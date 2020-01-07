package com.mdhgroup2.postmor.database.db;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;

import androidx.lifecycle.LiveData;

import com.mdhgroup2.postmor.R;
import com.mdhgroup2.postmor.database.DTO.MessageContent;
import com.mdhgroup2.postmor.database.DTO.MsgCard;
import com.mdhgroup2.postmor.database.interfaces.IBoxRepository;
import com.mdhgroup2.postmor.database.repository.DatabaseClient;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class BoxRepositoryMock implements IBoxRepository {
    List<MsgCard> l = new LinkedList<MsgCard>();

    List<MsgCard> l2 = new LinkedList<MsgCard>();

    List<MsgCard> l3 = new LinkedList<MsgCard>();

    List<MsgCard> l4 = new LinkedList<MsgCard>();

//    MutableLiveData<Integer> liveInt = new MutableLiveData<>();

    public BoxRepositoryMock(){
//        liveInt.setValue(10);

        MsgCard u1 = new MsgCard();
        u1.Name = "Ann-Marie Josefsson";
        u1.Address = "Isterbarnsgatan 12";
        u1.Picture = BitmapFactory.decodeResource(DatabaseClient.appContext.getResources(),
                R.drawable.stefan);

        u1.IsFriend = true;
        u1.DateStamp = Utils.makeDate(2019, 12, 19);
        u1.MsgID = 1;
        u1.IsSentByMe = true;
        u1.UserID = 5;
        u1.Text = "What the fuck did you just fucking say about me, you little bitch? I'll have you know I graduated top of my class in the Navy Seals, and I've been involved in numerous secret raids on Al-Quaeda, and I have over 300 confirmed kills. I am trained in gorilla warfare and I'm the top sniper in the entire US armed forces. You are nothing to me but just another target. I will wipe you the fuck out with precision the likes of which has never been seen before on this Earth, mark my fucking words. You think you can get away with saying that shit to me over the Internet? Think again, fucker. As we speak I am contacting my secret network of spies across the USA and your IP is being traced right now so you better prepare for the storm, maggot. The storm that wipes out the pathetic little thing you call your life. You're fucking dead, kid. I can be anywhere, anytime, and I can kill you in over seven hundred ways, and that's just with my bare hands. Not only am I extensively trained in unarmed combat, but I have access to the entire arsenal of the United States Marine Corps and I will use it to its full extent to wipe your miserable ass off the face of the continent, you little shit. If only you could have known what unholy retribution your little &quot;clever&quot; comment was about to bring down upon you, maybe you would have held your fucking tongue. But you couldn't, you didn't, and now you're paying the price, you goddamn idiot. I will shit fury all over you and you will drown in it. You're fucking dead, kiddo.";

        MsgCard u2 = new MsgCard();
        u2.Name = "Arne Askersund";
        u2.Address = "Mastrostvägen 13";
        u2.Picture = null;
        u2.IsFriend = true;
        u2.DateStamp = Utils.makeDate(2019, 12, 11);
        u2.MsgID = 2;
        u2.IsSentByMe = true;
        u2.UserID = 2;
        u2.Text = "This is also a message";

        MsgCard u3 = new MsgCard();
        u3.Name = "Swedish Chef";
        u3.Address = "Muppetgatan 14";
        u3.Picture = null;
        u3.IsFriend = false;
        u3.DateStamp = Utils.makeDate(2019, 11, 23);
        u3.MsgID = 3;
        u3.IsSentByMe = false;
        u3.UserID = 3;

        u3.Images = new ArrayList<>();
        InputStream myObj = DatabaseClient.appContext.getResources().openRawResource(R.raw.letter);

        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
//            System.out.println(data);
             u3.Images.add(Converters.fromBase64(data));
             u3.Images.add(u1.Picture);
        }
        myReader.close();

        MsgCard u4 = new MsgCard();
        u4.Name = "Brittish Chef";
        u4.Address = "Fish'n chipsstreet 9";
        u4.Picture = null;
        u4.IsFriend = false;
        u4.DateStamp = Utils.makeDate(2019, 3, 2);
        u4.MsgID = 4;
        u4.IsSentByMe = false;
        u4.UserID = 4;
        u4.Text = "I'm a navy squeal";

        l.add(u1);
        l.add(u2);
        l.add(u3);

        l2.add(u1);
        l2.add(u2);

        l3.add(u2);
        l3.add(u3);

        l4.add(u1);
        l4.add(u3);
        l4.add(u4);
    }


    @Override
    public List<MsgCard> getAllMessages() {
        return l;
    }

    @Override
    public List<MsgCard> getAllMessages(int ID) {
        return l2;
    }

    @Override
    public List<MsgCard> getInboxMessages() {
        return l3;
    }

    @Override
    public List<MsgCard> getInboxMessages(int ID) {
        return l;
    }

    @Override
    public List<MsgCard> getOutboxMessages() {
        return l4;
    }

    @Override
    public List<MsgCard> getOutboxMessages(int ID) {
        return l3;
    }

    @Override
    public int getNewMessageCount() {



        return 14;
    }

    @Override
    public int outgoingLetterCount() {
        return 3;
    }

    @Override
    public MessageContent getMsgContent(int MsgID) {
        MessageContent msg = new MessageContent();
        msg.Images = null;
        msg.Text = "Hello Philip!\n Last time I went to the market I bought some potatoes."
                + "I sent them in a package to you, and hopefully you will receive them before"
                + "you get this letter.\n\n Love, Grandma";
        return msg;
    }

    @Override
    public int fetchNewMessages(){
        return 0;
    }
}
