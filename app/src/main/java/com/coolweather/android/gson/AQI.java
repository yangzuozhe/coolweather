package com.coolweather.android.gson;

/**
 * Created by yzz on 2020/5/15.
 */

public class AQI {
    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
