package com.liangei.eiwheather.model;

/**
 * Created by LIANG_000 on 2015/4/1.
 */
public class Province {
    private int id ;
    private String provinceName;
    private String provinceCode;
    private String provinceNamePY;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceNamePY() {
        return provinceNamePY;
    }

    public void setProvinceNamePY(String provinceNamePY) {
        this.provinceNamePY = provinceNamePY;
    }
}
