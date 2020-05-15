package com.coolweather.android;

import java.util.Objects;

/**
 * Created by yzz on 2020/5/13.
 */

public class County {
    private String countyname;
    private String weatherid;
    private int cityid;

    public County(String countyname,String weatherid){
        this.countyname=countyname;
        this.weatherid=weatherid;
    }

    public String getCountyname() {
        return countyname;
    }

    public void setCountyname(String countyname) {
        this.countyname = countyname;
    }

    public String getWeatherid() {
        return weatherid;
    }

    public void setWeatherid(String weatherid) {
        this.weatherid = weatherid;
    }

    public int getCityid() {
        return cityid;
    }

    public void setCityid(int cityid) {
        this.cityid = cityid;
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj)return true;
        if(obj==null||getClass()!=obj.getClass())return false;

        County user=(County) obj;
        return Objects.equals(weatherid,user.weatherid) && Objects.equals(countyname,user.countyname);

    }
    @Override
    public int hashCode() {
        // TODO 自动生成的方法存根
        return Objects.hash(countyname,weatherid);
    }

}
