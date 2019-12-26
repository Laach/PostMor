package com.mdhgroup2.postmor.database.db;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Bitmap fromBase64(String base64Str) throws IllegalArgumentException
    {
        if(base64Str == null){
            return null;
        }
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @TypeConverter
    public static String bitmapToBase64(Bitmap bitmap)
    {
        if(bitmap == null){
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    @TypeConverter
    public List<Bitmap> bitmapListFromString(String data) {
        List<Bitmap> list = new ArrayList<>();

        String[] array = data.split(",");

        for (String s : array) {
            if (!s.isEmpty()) {
                list.add(fromBase64(s));
            }
        }
        return list;
    }

    @TypeConverter
    public String writingStringFromList(List<Bitmap> list) {
        String bitmaps = "";
        for (Bitmap bm : list) {
            bitmaps += "," + bitmapToBase64(bm);
        }
        return bitmaps;
    }


}


