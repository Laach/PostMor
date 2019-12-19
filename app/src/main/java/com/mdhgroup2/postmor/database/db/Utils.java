package com.mdhgroup2.postmor.database.db;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utils {
    public static Date makeDate(int year, int month, int day){
        Calendar c = new GregorianCalendar();
        c.set(year, month, day);
        return c.getTime();
    }

    public static Date makeTime(int hour, int minute, int second){
        Calendar c = new GregorianCalendar();
        c.set(2019, 12, 19, hour, minute, second);
        return c.getTime();
    }
}
