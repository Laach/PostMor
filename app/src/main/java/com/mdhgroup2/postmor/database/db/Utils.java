package com.mdhgroup2.postmor.database.db;

import android.os.AsyncTask;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

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

    public static Date makeDateTime(int year, int month, int day, int hour, int minute, int second){
        Calendar c = new GregorianCalendar();
        c.set(year, month, day, hour, minute, second);
        return c.getTime();
    }


    public static String getAuthToken(ManageDao dao){
        // Fetches and sets new auth token

        return null; // return new token
    }

    public static JSONObject APIPost(String url, JSONObject json) {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, json.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String b = response.body().string();
            return new JSONObject(b).getJSONObject("json");
        }
        catch (IOException e){
            return null;
        }
        catch (JSONException j){
            return null;
        }
    }

}
