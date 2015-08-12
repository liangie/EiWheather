package com.liangei.eiwheather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.liangei.eiwheather.receiver.updateWeatherReceiver;
import com.liangei.eiwheather.util.HttpCallbackListener;
import com.liangei.eiwheather.util.HttpUtil;
import com.liangei.eiwheather.util.Utility;

import java.util.Date;

/**
 * Created by LIANG_000 on 2015/4/8.
 */
public class MyService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("myservice","executed at "+new Date().toString());
                updateWeather();
            }
        }).start();
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        int oneMin = 60 * 1 * 1000;
        long triggerAlarm = SystemClock.elapsedRealtime() + oneMin;
        Intent i = new Intent(this, updateWeatherReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0, i,0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAlarm,pi);

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weathercode = prefs.getString("weather_code","");
        String address = "http://www.weather.com.cn/adat/cityinfo/"+ weathercode +".html";
        HttpUtil.sendHttpRequest(address,new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(MyService.this, response);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
