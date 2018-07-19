package com.pronix.android.apssaataudit.models;

import java.util.List;

/**
 * Created by ravi on 1/17/2018.
 */

public class MandalDO {

    public int mandalCode;
    public String mandalName;
    public int noOfPanchayats;
    public List<PanchayatDO> panchayatList;

    public int getNoOfPanchayats() {
        return noOfPanchayats;
    }

    public void setNoOfPanchayats(int noOfPanchayats) {
        this.noOfPanchayats = noOfPanchayats;
    }

    public List<PanchayatDO> getPanchayatList() {
        return panchayatList;
    }

    public void setPanchayatList(List<PanchayatDO> panchayatList) {
        this.panchayatList = panchayatList;
    }

    public int getMandalCode() {
        return mandalCode;
    }

    public void setMandalCode(int mandalCode) {
        this.mandalCode = mandalCode;
    }

    public String getMandalName() {
        return mandalName;
    }

    public void setMandalName(String mandalName) {
        this.mandalName = mandalName;
    }
}
