package com.liangei.eiwheather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.liangei.eiwheather.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LIANG_000 on 2015/4/1.
 */
public class TestActivity extends Activity {


    private TextView showText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        showText = (TextView)findViewById(R.id.testText);

        final String address = "http://192.168.1.101:8080/android/choo.xml";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(address);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if(httpResponse.getStatusLine().getStatusCode() == 200){
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity,"utf-8");
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
}
