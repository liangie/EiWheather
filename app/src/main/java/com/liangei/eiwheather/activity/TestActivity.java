package com.liangei.eiwheather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.liangei.eiwheather.R;
import com.liangei.eiwheather.model.EiWheatherDB;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LIANG_000 on 2015/4/1.
 */
public class TestActivity extends Activity {


    private TextView showText;
    private EiWheatherDB eiWheatherDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        showText = (TextView)findViewById(R.id.testText);

        final String address = "http://192.168.1.101:8080/android/choo.xml";
        queryWeatherCode("绵阳");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(address);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if(httpResponse.getStatusLine().getStatusCode() == 200){
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "utf-8");
                        parseXmlWithPull(response);
                    }
                }catch(Exception e){
                    Log.d("test","exception:\n"+e.toString());

                }
            }
        }).start();
    }

    private void parseXmlWithPull(String xmlData){
//        Log.d("result","xmlData:"+xmlData);
        List<String> list = new ArrayList<String>();
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            String quName = "1";
            String pyName = "2";
//            String[] aa = xmlData.split("city");
//            Log.d("result","aa"+aa[2]);
            while (eventType != XmlPullParser.END_DOCUMENT){

                String nodeName = xmlPullParser.getName();
//                Log.d("result","nodeName:"+nodeName);
                switch (eventType){
                    case XmlPullParser.START_TAG:{
                        if("city".equals(nodeName)){
                            quName = xmlPullParser.getAttributeValue(0);
                            pyName = xmlPullParser.getAttributeValue(1);
                            Log.d("result","quName:"+quName +";"+pyName);
                            list.add(nodeName);
                        }else if("pyName".equals(nodeName)){
                            pyName=xmlPullParser.nextText();
                            list.add(nodeName);
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG:{
                        if("city".equals(nodeName)){
//                            Log.d("result",quName);
//                            Log.d("result",pyName);
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
            Toast.makeText(TestActivity.this,"list:"+list.toString(),Toast.LENGTH_LONG).show();
//            Log.d("result","list:"+list.toString());

        }catch(Exception e){
            e.printStackTrace();
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
        Log.d("county","countyName:" + countyName+"\nweatherCode:"+weatherCode);
//        queryWeatherInfo(weatherCode);
    }


    /**
     *
     */
    private String parseJSONWithJsonObject(String countyName,String jsonData){
        String weatherCode = "";
        try{
//            JSONArray jsonArray = new JSONArray(jsonData);
//            Log.d("county","length:"+jsonArray.length());
//            for(int i = 0;i<jsonArray.length();i++){
//                JSONObject object = jsonArray.getJSONObject(i);
//                if(object.getBoolean(countyName)){
//                    weatherCode = object.getString(countyName);
//                    break;
//                }
//            }
            JSONObject jsonObject = new JSONObject(jsonData);
            weatherCode = jsonObject.getString(countyName);
            Log.d("county","weatherCode:"+weatherCode);
        }catch(Exception e){
            e.printStackTrace();
            Log.d("excep",e.toString());
        }
        return weatherCode;
    }
}
