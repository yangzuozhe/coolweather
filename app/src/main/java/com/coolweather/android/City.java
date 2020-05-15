package com.coolweather.android;

import android.support.annotation.NonNull;

import java.util.Comparator;
import java.util.Objects;

/**
 * Created by yzz on 2020/5/13.
 */

public class City {
    private String cityName;
    private int citycode;
    private int provinceid;


    public City(String cityName,int citycode){
        this.citycode=citycode;
        this.cityName=cityName;
    }

    public void setCitycode(int citycode) {
        this.citycode = citycode;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setProvinceid(int provinceid) {
        this.provinceid = provinceid;
    }

    public int getCitycode() {
        return citycode;
    }

    public int getProvinceid() {
        return provinceid;
    }

    public String getCityName() {
        return cityName;
    }




    @Override
    public boolean equals(Object obj) {
        if(this==obj)return true;
        if(obj==null||getClass()!=obj.getClass())return false;

        City user=(City) obj;
        return citycode==user.citycode && Objects.equals(cityName,user.cityName);

    }
    @Override
    public int hashCode() {
        // TODO 自动生成的方法存根
        return Objects.hash(cityName,citycode);
    }



}
