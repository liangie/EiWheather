package com.liangei.eiwheather.model;

/**
 * Created by LIANG_000 on 2015/4/1.
 */
public class City {
    private int id;
    private String cityName;
    private String cityCode;
    private int provinceId;

    private String cityNamePY;//市名的拼音

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityNamePY() {
        return cityNamePY;
    }

    public void setCityNamePY(String cityNamePY) {
        this.cityNamePY = cityNamePY;
    }
}
