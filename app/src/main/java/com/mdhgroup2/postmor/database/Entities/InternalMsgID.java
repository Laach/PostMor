package com.mdhgroup2.postmor.database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class InternalMsgID {

    public InternalMsgID(int Num){
        this.Num = Num;
    }

    @PrimaryKey
    public int Num;
}
