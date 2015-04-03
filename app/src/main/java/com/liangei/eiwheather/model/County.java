package com.liangei.eiwheather.model;

/**
 * Created by LIANG_000 on 2015/4/1.
 */
public class County {
    private int id;
    private String countyName;
    private String countyCode;
    private int cityId;

    private String countyNamePY;//县名的拼音

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCountyNamePY() {
        return countyNamePY;
    }

    public void setCountyNamePY(String countyNamePY) {
        this.countyNamePY = countyNamePY;
    }
}
