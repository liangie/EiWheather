package com.liangei.eiwheather.util;

/**
 * Created by LIANG_000 on 2015/4/1.
 */
public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);

}
