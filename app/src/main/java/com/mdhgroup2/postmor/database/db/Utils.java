package com.mdhgroup2.postmor.database.db;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Utils {

    public static final String baseURL = "https://postmorwebserver20191230083106.azurewebsites.net";


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

    public static Date parseDate(String s){
        try {
//            DateFormat dateFormat = new SimpleDateFormat("hh:mm dd/MM/yy");
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
//            "2019-12-30T14:10:44.2522414Z"
            dateFormat.setLenient(false);
            return dateFormat.parse(s);
        }
        catch (ParseException e){
            return null;
        }
    }

    public static JSONObject APIPost(String url, JSONObject json, ManageDao managedao) throws IOException {
        try{
            return new JSONObject(APIPostBody(url, json, managedao));
        }
        catch (JSONException j){
            return null;
        }
    }

    public static JSONArray APIPostArray(String url, JSONObject json, ManageDao managedao) throws IOException {
        try{
            return new JSONArray(APIPostBody(url, json, managedao));
        }
        catch (JSONException j){
            return null;
        }
    }

    private static String APIPostBody(String url, JSONObject json, ManageDao managedao) throws IOException{

        String token = "";
        if(managedao.refreshToken()){
            token = managedao.getAuthToken();
        }

        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(json.toString(), JSON);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("authorization", "Bearer " + token)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String b = response.body().string();
            return b;
        }
    }


//    public class APIWorker extends Worker {
//
//        public APIWorker(Context c, WorkerParameters params){
//            super(c, params);
//        }
//
//        @NonNull
//        @Override
//        public Result doWork() {
//            // Use Data.Builder() to pass in the json string.
//            String url = getInputData().getString("url");
//            String data = getInputData().getString("json");
//            try {
//                Utils.APIPost(url, new JSONObject(data));
//            }
//            catch (JSONException | IOException e){
//                return Result.failure();
//            }
//            return Result.success();
//        }
//    }


}
