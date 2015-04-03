package com.liangei.eiwheather.util;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by LIANG_000 on 2015/4/1.
 */
public class HttpUtil {
    public static void sendHttpRequest(final String address,
                                       final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(address);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setConnectTimeout(8000);
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    InputStream inputStream = connection.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                    StringBuilder response = new StringBuilder();
//                    String line;
//                    while((line = reader.readLine())!=null){
//                        response.append(line);
//                    }
                    int len = 0;
                    byte[] data = new byte[1024];
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    while((len = inputStream.read(data)) != -1){
                        outputStream.write(data, 0 , len);
                    }
                    String response = new String(outputStream.toByteArray());
//                    Log.d("choo","response:"+response);
//                    File file = new File("/sdcard/choo.xml");
//                    FileOutputStream fo = null;
//                    fo = new FileOutputStream(file);
//                    fo.write(response.getBytes());
                    if(listener!=null){
                        listener.onFinish(response);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    Log.d("choo","exception:"+e.toString());
                    if(listener!=null){
                        listener.onError(e);
                    }
                }finally {
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }


}
