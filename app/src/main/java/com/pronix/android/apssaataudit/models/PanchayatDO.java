package com.pronix.android.apssaataudit.models;

/**
 * Created by ravi on 1/17/2018.
 */

public class PanchayatDO {

    public int panchayatCode;
    public String panchayatName;
    public int mandalCode;
    public int districtCode;

    public int getPanchayatCode() {
        return panchayatCode;
    }

    public void setPanchayatCode(int panchayatCode) {
        this.panchayatCode = panchayatCode;
    }

    public String getPanchayatName() {
        return panchayatName;
    }

    public void setPanchayatName(String panchayatName) {
        this.panchayatName = panchayatName;
    }

    public int getMandalCode() {
        return mandalCode;
    }

    public void setMandalCode(int mandalCode) {
        this.mandalCode = mandalCode;
    }

    public int getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(int districtCode) {
        this.districtCode = districtCode;
    }
}
