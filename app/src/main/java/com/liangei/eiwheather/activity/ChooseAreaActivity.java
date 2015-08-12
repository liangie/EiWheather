package com.liangei.eiwheather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.liangei.eiwheather.R;
import com.liangei.eiwheather.model.City;
import com.liangei.eiwheather.model.County;
import com.liangei.eiwheather.model.EiWheatherDB;
import com.liangei.eiwheather.model.Province;
import com.liangei.eiwheather.util.HttpCallbackListener;
import com.liangei.eiwheather.util.HttpUtil;
import com.liangei.eiwheather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LIANG_000 on 2015/4/1.
 */
public class ChooseAreaActivity extends Activity {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private EiWheatherDB eiWheatherDB;
    private List<String> dataList = new ArrayList<String>();
    private boolean isFromWeatherActivity;

    /**
     * province list
     */
    private List<Province> provinceList;

    /**
     * city list
     */
    private List<City> cityList;

    /**
     * county list
     */
    private List<County> countyList;

    /**
     * choose province
     */
    private Province selectedProvince;

    /**
     * selected city
     */
    private City selectedCity;

    /**
     * choosed level
     */
    private int currentLevel ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity",false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean("city_selected", false) && !isFromWeatherActivity){
            Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listView = (ListView)findViewById(R.id.list_view);
        titleText = (TextView)findViewById(R.id.title_text);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        eiWheatherDB = EiWheatherDB.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounties();
                }else if(currentLevel == LEVEL_COUNTY){
                    String countyCode = countyList.get(position).getCountyCode();
                    String countyName = countyList.get(position).getCountyName();
                    Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
                    intent.putExtra("county_code",countyCode);
                    intent.putExtra("county_name",countyName);
                    startActivity(intent);
                    finish();
                }
            }
        });
        queryProvinces();//加载省级数据
    }

    /**
     * 查询全国所有省，优先从数据库中查询，没有查到再到服务器上查找
     */
    private void queryProvinces(){
        provinceList = eiWheatherDB.loadProvince();
        if(provinceList.size()>0){
            dataList.clear();
            for(Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        }else {
            queryFromServer(null,"province");
        }
    }

    /**
     * 查询选中省份所有市，优先查数据库，没有查到则连服务器查找
     */
    private void queryCities(){
        cityList = eiWheatherDB.loadCity(selectedProvince.getId());
        if(cityList.size()>0){
            dataList.clear();
            for(City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        }else{
            queryFromServer(selectedProvince.getProvinceNamePY(),"city");
        }
    }

    /**
     * 查询选中城市所有的县，优先查询数据库，没查到则链接服务器查找
     */
    private void queryCounties(){
        countyList = eiWheatherDB.loadCounty(selectedCity.getId());
        if(countyList.size()>0){
            dataList.clear();
            for(County county : countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);;
            titleText.setText(selectedCity.getCityName());
            currentLevel = LEVEL_COUNTY;
        }else{
            queryFromServer(selectedCity.getCityNamePY(),"county");
        }
    }

    private void queryFromServer(final String code, final String type){
        String address = "";
        if(!TextUtils.isEmpty(code)){
            address = "http://flash.weather.com.cn/wmaps/xml/"+code+".xml";
        }else{
            address = "http://flash.weather.com.cn/wmaps/xml/china.xml";
        }
        showProgressDialog();
        Toast.makeText(ChooseAreaActivity.this,address,Toast.LENGTH_SHORT).show();
        HttpUtil.sendHttpRequest(address,new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.parseXMLWithPullForProvince(eiWheatherDB,response);
                }else if("city".equals(type)){
                    result = Utility.parseXMLWithPullForCity(eiWheatherDB,response,selectedProvince.getId());
                }else if("county".equals(type)){
                    result = Utility.parseXMLWithPullForCounty(eiWheatherDB,response,selectedCity.getId());
                }

//                Toast.makeText(ChooseAreaActivity.this,"result",Toast.LENGTH_LONG).show();

                if(result){
                    Log.d("choo", "reulst is true!");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                               queryCities();
                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }else {
                    Log.d("choo","result is false！！");
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,"load failed ！",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }


    /**
     * 显示进度对话框
     */
    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("loading...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    /**
     * 捕获back键，根据当前级别判断应该返回的列表或则直接退出
     */
    @Override
    public void onBackPressed() {
        if(currentLevel == LEVEL_COUNTY){
            queryCities();
        }else if (currentLevel == LEVEL_CITY){
            queryProvinces();
        }else {
            if(isFromWeatherActivity){
                Intent intent = new Intent(this, WeatherActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        eiWheatherDB.clearFeedTable("province");
//        eiWheatherDB.clearFeedTable("city");
//        eiWheatherDB.clearFeedTable("county");
    }


}
