package com.coolweather.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.coolweather.android.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yzz on 2020/5/12.
 */

public class Utility {
    static MyData myData=new MyData(MyApplication.getContext(),"cool_weather.db",null,1);
/*
            解析和处理服务器返回的省级数据
 */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray jsonArray=new JSONArray(response);                //将json变成数组
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object=jsonArray.getJSONObject(i);           //得到json的其中一个对象

                    //数据添加到数据库
                    SQLiteDatabase db=myData.getWritableDatabase();
                    ContentValues values=new ContentValues();
                    values.put("provinceName",object.getString("name"));
                    values.put("provinceCode",object.getInt("id"));

                    db.insert("Provite",null,values);
                    values.clear();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }


    public static boolean handleCityResponse(String reponse,int provinceId){
        if(!TextUtils.isEmpty(reponse)) {
            try {
                JSONArray jsonArray = new JSONArray(reponse);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);


                    SQLiteDatabase db = myData.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("cityName", object.getString("name"));
                    values.put("citycode", object.getInt("id"));
                    values.put("provinceid", provinceId);
                    db.insert("City",null,values);
                    values.clear();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public static boolean handleCountyResponse(String respon,int cityid){
        try {
            JSONArray jsonArray=new JSONArray(respon);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject object=jsonArray.getJSONObject(i);


                SQLiteDatabase db=myData.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put("countyname",object.getString("name"));
                values.put("weatherid",object.getString("weather_id"));
                values.put("cityid",cityid);
                db.insert("County",null,values);
                values.clear();
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
            return false;
    }


    public static Weather handleWeatherResponse(String response){

        try {
        JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");
            String weatherContent =jsonArray.getJSONObject(0).toString();
            Log.d("Demo",weatherContent);
            return new Gson().fromJson(weatherContent,Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }







}
