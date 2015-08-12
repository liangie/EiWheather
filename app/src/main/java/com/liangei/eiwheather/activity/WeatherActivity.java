package com.liangei.eiwheather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liangei.eiwheather.R;
import com.liangei.eiwheather.service.MyService;
import com.liangei.eiwheather.util.HttpCallbackListener;
import com.liangei.eiwheather.util.HttpUtil;
import com.liangei.eiwheather.util.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by LIANG_000 on 2015/4/4.
 */
public class WeatherActivity extends Activity implements View.OnClickListener {

    private LinearLayout weatherInfoLayout;
    private TextView cityNameText;
    private TextView publishText;
    private TextView weatherDespText;
    private TextView temp1Text;
    private TextView temp2Text;
    private TextView currentDateText;
    private Button switchCity;
    private Button refreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        weatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
        cityNameText = (TextView)findViewById(R.id.city_name);
        publishText = (TextView)findViewById(R.id.publish_text);
        weatherDespText = (TextView)findViewById(R.id.weather_desp);
        temp1Text = (TextView)findViewById(R.id.temp1);
        temp2Text = (TextView)findViewById(R.id.temp2);
        currentDateText = (TextView)findViewById(R.id.current_date);
        switchCity = (Button)findViewById(R.id.switch_city);
        refreshWeather = (Button)findViewById(R.id.refresh_weather);
        String countyCode = getIntent().getStringExtra("county_code");
//        String countyName = getIntent().getStringExtra("county_name");
        if(!TextUtils.isEmpty(countyCode)){
            publishText.setText("同步中...countyCode:"+countyCode);
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherInfo(countyCode);
        }else{
            showWeather();
        }
        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.switch_city:
                Intent intent = new Intent(this, ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
                break;
            case R.id.refresh_weather:
                publishText.setText("同步中...");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode = prefs.getString("weather_code","");
                if(!TextUtils.isEmpty(weatherCode)){
                    queryWeatherInfo(weatherCode);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 查询countyCode对应的天气代号
     * @param
     */
    private void queryWeatherCode(String countyName){

//        String address = "http://flash.weather.com.cn/wmaps/xml/"+countyNamePY+".html";
        StringBuffer jsonData = new StringBuffer();
        File file = new File("/sdcard/allWeatherCode.txt");
        try{
            InputStream inputStream = new FileInputStream(file);
            if(inputStream!=null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                char[] buff = new char[1024];
                int len = 0;
                while((len = reader.read(buff)) != -1){
                    jsonData.append(buff,0,len);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        String weatherCode = parseJSONWithJsonObject(countyName, jsonData.toString());
        Log.d("county","countyName:" + countyName+"\nweatherCode:"+weatherCode+"\njsonData:"+jsonData.toString());
        queryWeatherInfo(weatherCode);
    }

    /**
     * 查询天气代号对应的天气
     */
    private void queryWeatherInfo(String countyCode){
        String address = "http://www.weather.com.cn/adat/cityinfo/"+countyCode+".html";
//        String address = "http://www.weather.com.cn/adat/cityinfo/"+101270401+".html";
        queryFromServer(address);
    }


    /**
     *
     */
    private String parseJSONWithJsonObject(String countyName,String jsonData){
        String weatherCode = "";
        try{
            JSONArray jsonArray = new JSONArray(jsonData);
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                if(object.getBoolean(countyName)){
                    weatherCode = object.getString(countyName);
                    break;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return weatherCode;
    }

    /**
     * 根据传入的地址和类型向服务器查询天气代号或者天气信息
     */
    private void queryFromServer(final String address){
        Log.d("weather",address);
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(WeatherActivity.this,response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                publishText.setText("同步失败!");
            }
        });
    }

    /**
     * 从SharePreferences文件中读取存储的天气信息，并显示到屏幕
     */
    public void showWeather(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(prefs.getString("city_name",""));
        temp1Text.setText(prefs.getString("temp1",""));
        temp2Text.setText(prefs.getString("temp2",""));
        weatherDespText.setText(prefs.getString("weather_desp",""));
        publishText.setText("今天"+prefs.getString("publish_time","")+"发布");
        currentDateText.setText(prefs.getString("current_date",""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }
}

