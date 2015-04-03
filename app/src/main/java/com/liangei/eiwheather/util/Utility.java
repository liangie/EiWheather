package com.liangei.eiwheather.util;

import android.text.TextUtils;
import android.util.Log;

import com.liangei.eiwheather.model.City;
import com.liangei.eiwheather.model.County;
import com.liangei.eiwheather.model.EiWheatherDB;
import com.liangei.eiwheather.model.Province;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

/**
 * Created by LIANG_000 on 2015/4/1.
 */
public class Utility {


    /**
     * 解析xml数据中的省份信息，并插入数据库中
     * @param eiWheatherDB
     * @param xmlData
     * @return
     */
    public synchronized static boolean parseXMLWithPullForProvince(EiWheatherDB eiWheatherDB,String xmlData){

        Log.d("choo", "xmlData\n:" + xmlData);
        if(!TextUtils.isEmpty(xmlData)){
            Province province = null;
            try{
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = factory.newPullParser();
                xmlPullParser.setInput(new StringReader(xmlData));
                int eventType = xmlPullParser.getEventType();
                String quName = "";
                String pyName = "";
                while(eventType != XmlPullParser.END_DOCUMENT){
                    String nodeName = xmlPullParser.getName();
                    switch (eventType){
                        case XmlPullParser.START_TAG:{
                            if("city".equals(nodeName)){
                                province = new Province();
                                quName = xmlPullParser.getAttributeValue(0);
                                pyName = xmlPullParser.getAttributeValue(1);
                                Log.d("choo","name:"+quName+pyName);
                                province.setProvinceName(quName);
                                province.setProvinceNamePY(pyName);
                                eiWheatherDB.saveProvince(province);
                            }
//                            if("quName".equals(nodeName)){
//                                quName = xmlPullParser.nextText();
//                                province.setProvinceName(quName);
//                            }else if("pyName".equals(nodeName)){
//                                pyName = xmlPullParser.nextText();
//                                province.setProvinceNamePY(pyName);
//                            }
                            break;
                        }
                        case XmlPullParser.END_TAG:{
                            province = null;
                            break;
                        }

                        default:
                            break;
                    }
                    eventType = xmlPullParser.next();
                }
                return true;
            }catch(Exception e){
                Log.d("choo",e.toString());
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析xml数据中的city信息，并插入数据库中
     * @param eiWheatherDB
     * @param xmlData
     * @param provinceId
     * @return
     */
    public static boolean parseXMLWithPullForCity(EiWheatherDB eiWheatherDB, String xmlData, int provinceId){
        Log.d("city",provinceId+" + "+xmlData);
        if(!TextUtils.isEmpty(xmlData)){
            City city =null;
            try{
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = factory.newPullParser();
                xmlPullParser.setInput(new StringReader(xmlData));
                int eventType = xmlPullParser.getEventType();
                String cityName = "";
                String cityCode = "";
                String cityNamePY = "";
                while(eventType != XmlPullParser.END_DOCUMENT){
                    String nodeName = xmlPullParser.getName();
                    switch (eventType){
                        case XmlPullParser.START_TAG:{
                            if("city".equals(nodeName)){
                                city = new City();
                                cityName = xmlPullParser.getAttributeValue(2);
                                cityCode = xmlPullParser.getAttributeValue(xmlPullParser.getAttributeCount()-1);
                                cityNamePY = xmlPullParser.getAttributeValue(5);
                                city.setCityNamePY(cityNamePY);
                                city.setCityName(cityName);
                                city.setCityCode(cityCode);
                                city.setProvinceId(provinceId);
                                eiWheatherDB.saveCity(city);
                            }
//                            if("cityname".equals(nodeName)){
//                                cityName = xmlPullParser.nextText();
//                                city.setCityName(cityName);
//                            }else if("pyName".equals(nodeName)){
//                                cityName = xmlPullParser.nextText();
//                                city.setCityNamePY(cityNamePY);
//                            }else if("url".equals(nodeName)){
//                                cityCode = xmlPullParser.nextText();
//                                city.setCityCode(cityCode);
//                            }
                            break;
                        }
                        case XmlPullParser.END_TAG:
                            city = null;
                            break;
                        default:
                            break;
                    }
                    eventType = xmlPullParser.next();
                }
                return true;
            }catch(Exception e){
                e.printStackTrace();

            }
        }
        return false;
    }

    /**
     * 解析xml数据中的county信息，并插入数据库中
     * @param eiWheatherDB
     * @param xmlData
     * @param cityId
     * @return
     */
    public static boolean parseXMLWithPullForCounty(EiWheatherDB eiWheatherDB, String xmlData, int cityId){
//        Log.d("county",)
        if(!TextUtils.isEmpty(xmlData)){
            County county = null;
            try{
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = factory.newPullParser();
                xmlPullParser.setInput(new StringReader(xmlData));
                int eventType = xmlPullParser.getEventType();
                String countyName = "";
                String countyCode = "";
                String countyNamePY = "";
                while(eventType != XmlPullParser.END_DOCUMENT){
                    String nodeName = xmlPullParser.getName();
                    switch (eventType){
                        case XmlPullParser.START_TAG:{
                            if("city".equals(nodeName)){
                                county = new County();
                                countyName = xmlPullParser.getAttributeValue(2);
                                countyCode = xmlPullParser.getAttributeValue(xmlPullParser.getAttributeCount()-1);
                                countyNamePY = xmlPullParser.getAttributeValue(5);
                                county.setCountyCode(countyCode);
                                county.setCountyNamePY(countyNamePY);
                                county.setCountyName(countyName);
                                county.setCityId(cityId);
                                eiWheatherDB.saveCounty(county);
                            }
//                            if("cityname".equals(nodeName)){
//                                countyName = xmlPullParser.nextText();
//                                county.setCountyName(countyName);
//                            }else if("pyName".equals(nodeName)){
//                                countyNamePY = xmlPullParser.nextText();
//                                county.setCountyNamePY(countyNamePY);
//                            }else if("url".equals(nodeName)){
//                                countyCode = xmlPullParser.nextText();
//                                county.setCountyCode(countyCode);
//                            }
                            break;
                        }
                        case XmlPullParser.END_TAG:

                            county = null;
                            break;
                        default:
                            break;
                    }
                    eventType = xmlPullParser.next();
                }
                return true;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }
}
