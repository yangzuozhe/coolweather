package com.coolweather.android;

import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * Created by yzz on 2020/5/13.
 */

public class Province {
    private String provinceName;
    private int provinceCode;

    public Province(String provinceName,int provinceCode){
        this.provinceName=provinceName;
        this.provinceCode=provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceName() {
        return provinceName;
    }
    @Override
    public boolean equals(Object obj) {
        if(this==obj)return true;
        if(obj==null||getClass()!=obj.getClass())return false;

        Province user=(Province)obj;
        return provinceCode==user.provinceCode && Objects.equals(provinceName,user.provinceName);

    }
    @Override
    public int hashCode() {
        // TODO 自动生成的方法存根
        return Objects.hash(provinceName,provinceCode);
    }



}
