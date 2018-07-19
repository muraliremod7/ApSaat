package com.pronix.android.apssaataudit.models;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by ravi on 1/17/2018.
 */

public class DistrictDO {

    public int districtCode;
    public String districtName;
    public int noOFMandals;
    public List<MandalDO> mandalList;

    public List<MandalDO> getMandalList() {
        return mandalList;
    }

    public void setMandalList(List<MandalDO> mandalList) {
        this.mandalList = mandalList;
    }

    public int getNoOFMandal() {
        return noOFMandals;
    }

    public void setNoOFMandals(int noOFMandals) {
        this.noOFMandals = noOFMandals;
    }

    public int getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(int districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
}
