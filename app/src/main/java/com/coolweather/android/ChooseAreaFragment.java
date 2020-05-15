package com.coolweather.android;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by yzz on 2020/5/13.
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_CONTY=2;
    private TextView title;
    private Button backbutton;
    private ListView listView;                              //滚动页面
    private ArrayAdapter<String> adapter;                   //滚动页面适配器
    private List<String> dataList=new ArrayList<>();        //页面里的内容

    private ProgressDialog progressDialog;

    private HashSet<Province> provinceHashSet=new HashSet<>();           //从数据库不重复的提取内容
    private List<Province> provinceList=new ArrayList<>();                //从数据库提取出省的内容
    private HashSet<City> cityHashSet=new HashSet<>();
    private List<City> cityList=new ArrayList<>();
    private HashSet<County> countyHashSet=new HashSet<>();
    private List<County> countyList=new ArrayList<>();




    private Province selectedprovince;                      //省类的类
    private City selectedCity;                              //城市的类
    private County selectedCounty;                          //县的类

    private int currentLevel;                               //当前选中的级别


//    MyData myData=new MyData(getContext(),"cool_weather.db",null,1);
        MyData myData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.choose_area,container,false);
        title=(TextView)view.findViewById(R.id.title_text);
        backbutton=(Button)view.findViewById(R.id.back_button);
        listView=(ListView)view.findViewById(R.id.list_view);
        adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel==LEVEL_PROVINCE){
                    selectedprovince=provinceList.get(position);
                    Log.d("Demo","按钮选项"+position+"");
                    queryCity();
                }else if (currentLevel==LEVEL_CITY){
                    selectedCity=cityList.get(position);
                    queryCounty();
                }
            }
        });


        backbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(currentLevel==LEVEL_CONTY){
                    countyHashSet.clear();
                    countyList.clear();
                    queryCity();
                }else if(currentLevel==LEVEL_CITY){
                    cityHashSet.clear();
                    cityList.clear();
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces(){
        myData=new MyData(getContext(),"cool_weather.db",null,1);
        title.setText("中国");
        backbutton.setVisibility(View.GONE);
        //查询省数据库
        SQLiteDatabase db=myData.getWritableDatabase();
        Cursor cursor=db.query("Provite",null,null,null,null,null,null,null);
        provinceList.clear();                                                                       //先将省列表清理空
        provinceHashSet.clear();
        if(cursor.moveToFirst()) {
            do {
                String provinceName = cursor.getString(cursor.getColumnIndex("provinceName"));
                int provinceCode = cursor.getInt(cursor.getColumnIndex("provinceCode"));
                provinceHashSet.add(new Province(provinceName, provinceCode));                       //不重复的放进set集合
            } while (cursor.moveToNext());
        }
            provinceList.addAll(provinceHashSet);                                                 //再放进省list集合
                if(provinceHashSet.size()>0){
                    dataList.clear();                                                                  //将listview列表的集合清空
                        for (Province province : provinceList) {
                            dataList.add(province.getProvinceName());                                   //省lsit集合里的省的名称添加到listview列表里
                        }

                    adapter.notifyDataSetChanged();
                    listView.setSelection(0);
                    currentLevel=LEVEL_PROVINCE;
                }else{
                    String address="http://guolin.tech/api/china";              //中国各省的名称和id
                    queryFromServer(address,"province");
                }


    }

    private void queryCity(){
        myData=new MyData(getContext(),"cool_weather.db",null,1);
        title.setText(selectedprovince.getProvinceName());
        backbutton.setVisibility(View.VISIBLE);

        SQLiteDatabase db=myData.getWritableDatabase();
        Cursor cursor=db.query("City",null,"provinceid=?",new String[]{ String.valueOf(selectedprovince.getProvinceCode())},null,null,null);

        if(cursor.moveToFirst()) {
            cityList.clear();
            cityHashSet.clear();
            do {
                String cityName = cursor.getString(cursor.getColumnIndex("cityName"));

                int citycode = cursor.getInt(cursor.getColumnIndex("citycode"));

                cityHashSet.add(new City(cityName, citycode));
            } while (cursor.moveToNext());
        }
            cityList.addAll(cityHashSet);
            if(cityHashSet.size()>0) {
                dataList.clear();
                    for (City city : cityList) {
                        dataList.add(city.getCityName());
                    }

                adapter.notifyDataSetChanged();
                listView.setSelection(0);
                currentLevel=LEVEL_CITY;
            }else {
                int provinceCode=selectedprovince.getProvinceCode();
                String address="http://guolin.tech/api/china/"+provinceCode;
                queryFromServer(address,"City");
            }

    }

    private void queryCounty() {
        myData=new MyData(getContext(),"cool_weather.db",null,1);
        title.setText(selectedCity.getCityName());
        backbutton.setVisibility(View.VISIBLE);

        SQLiteDatabase db = myData.getWritableDatabase();
        Cursor cursor = db.query("County", null, "cityid=?", new String[]{String.valueOf(selectedCity.getCitycode())}, null, null, null);
        countyList.clear();
        countyHashSet.clear();
        if (cursor.moveToFirst()) {
            do {
                String countyname = cursor.getString(cursor.getColumnIndex("countyname"));
                String weatherid = cursor.getString(cursor.getColumnIndex("weatherid"));
                countyHashSet.add(new County(countyname, weatherid));
            } while (cursor.moveToNext());
        }


            if (countyHashSet.size() > 0) {
                countyList.addAll(countyHashSet);
                dataList.clear();
                for(County county:countyList){
                    dataList.add(county.getCountyname());
                }

                adapter.notifyDataSetChanged();
                listView.setSelection(0);
                currentLevel = LEVEL_CONTY;

            } else {
                int provinceCode = selectedprovince.getProvinceCode();
                int CityCode = selectedCity.getCitycode();
                String address = "http://guolin.tech/api/china/" + provinceCode + "/" + CityCode;
                queryFromServer(address, "County");
            }


    }








    private void queryFromServer(String address,final String type){
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {                           //这个方法用来得到服务器返回的内容
            String responseText=response.body().string();               //获得json数据，及各省名称和id
                boolean result=false;
                if ("province".equals(type)){
                    result=Utility.handleProvinceResponse(responseText);        //传入，json数据
                }else if ("City".equals(type)) {
                    result = Utility.handleCityResponse(responseText, selectedprovince.getProvinceCode());         //传入json数据和目前选中的省的id
                }else if("County".equals(type)){
                    result=Utility.handleCountyResponse(responseText,selectedCity.getCitycode());
                }
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("City".equals(type)){
                                queryCity();
                            }else if("County".equals(type)){
                                queryCounty();
                            }

                        }
                    });
                }


            }
        });

    }

    private void showProgressDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载……");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }



    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }


}

