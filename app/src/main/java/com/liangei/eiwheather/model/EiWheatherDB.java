package com.liangei.eiwheather.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.liangei.eiwheather.db.EiWheatherOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LIANG_000 on 2015/4/1.
 */
public class EiWheatherDB {

    /**
     * Database name
     */
    public static final String DB_NAME = "ei_wheather";

    /**
     * Database version
     */
    public static final int VERSION = 1;
    private static EiWheatherDB eiWheatherDB;
    private SQLiteDatabase db;

    /**
     * 构造私有化方法
     */
    private EiWheatherDB(Context context){
        EiWheatherOpenHelper helper = new EiWheatherOpenHelper(context,DB_NAME,null,VERSION);
        db = helper.getWritableDatabase();
    }

    /**
     * 获得EiWheatherDB的实例
     */
    public synchronized static EiWheatherDB getInstance(Context context){
        if(eiWheatherDB==null){
            eiWheatherDB = new EiWheatherDB(context);
        }
        return eiWheatherDB;
    }

    /**
     * save Province data into database
     */
    public void saveProvince(Province province){
        if(province!=null){
            ContentValues values = new ContentValues();
            values.put("province_name",province.getProvinceName());
            values.put("province_code",province.getProvinceCode());
            values.put("province_name_py",province.getProvinceNamePY());
            db.insert("Province",null,values);
        }
    }

    /**
     * load province data from database
     */
    public List<Province> loadProvince(){
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("Province",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                province.setProvinceNamePY(cursor.getString(cursor.getColumnIndex("province_name_py")));
                list.add(province);
            }while(cursor.moveToNext());
        }
        Log.d("choo", "list:\n"+list.toString());
        return list;
    }

    /**
     * save city data into database
     */
    public void saveCity(City city){
        if(city != null){
            ContentValues values = new ContentValues();
            values.put("city_name",city.getCityName());
            values.put("city_code",city.getCityCode());
            values.put("province_id",city.getProvinceId());
            values.put("city_name_py",city.getCityNamePY());
            db.insert("City",null,values);
        }
    }

    /**
     * load city data from database
     */
    public List<City> loadCity(int provinceId){
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City",null,"province_id = ?",new String[]{String.valueOf(provinceId)},null,null,null);
        if(cursor.moveToFirst()){
            do{
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setCityNamePY(cursor.getString(cursor.getColumnIndex("city_name_py")));
                city.setProvinceId(provinceId);
                list.add(city);
            }while(cursor.moveToNext());
        }
        return list;
    }

    /**
     * save county data into database
     */
    public void saveCounty(County county){
        if(county != null){
            ContentValues values = new ContentValues();
            values.put("county_name",county.getCountyName());
            values.put("county_code",county.getCountyCode());
            values.put("city_id",county.getCityId());
            values.put("county_name_py",county.getCountyNamePY());
            db.insert("County",null,values);
        }
    }

    /**
     * load county data from database
     */
    public List<County> loadCounty(int cityId){
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("County",null,"city_id = ?",new String[]{String.valueOf(cityId)},null,null,null);
        if(cursor.moveToFirst()){
            do{
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCountyNamePY(cursor.getString(cursor.getColumnIndex("county_name_py")));
                county.setCityId(cityId);
                list.add(county);
            }while(cursor.moveToNext());
        }
        return list;
    }

    /**
     * （测试用，临时方法）
     * 在退出程序的时候，清除数据库中的数据
     * @param tableName
     */
    public void clearFeedTable(String tableName){
        String sql = "DELETE FROM " + tableName + ";";
        db.execSQL(sql);
        revertSeq(tableName);
    }

    public void revertSeq(String tableName){
        String sql = "update sqlite_sequence set seq = 0 where name = '" + tableName + "';";
        db.execSQL(sql);
    }
}
