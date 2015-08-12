package com.liangei.eiwheather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.liangei.eiwheather.service.MyService;

/**
 * Created by LIANG_000 on 2015/4/8.
 */
public class updateWeatherReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MyService.class);
        context.startService(i);

    }
}
