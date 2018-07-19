package com.pronix.android.apssaataudit.common;


import com.pronix.android.apssaataudit.models.UserMasterDO;

/**
 * Created by ravi on 1/2/2018.
 */

public class Constants {

    public static String ROOTDIRECTORYPATH = "";
    public static String DATABASE_DIRECTORY = "SSAAT_DB";
    public static String DATABASE_NAME = "SSAATDB";

    public static int DATABASE_VERSION = 1;

    public static String PIN = "";
    public static String USERORMOBILE = "";

    public static UserMasterDO userMasterDO;

        public static String URLBase = "http://env-0687861.cloud.cms500.com/apssaat-audit/";
//    public static String URLBase = "http://192.168.0.113:8080/apssaat-audit/";
//public static String URLBase = "http://localhost:8080/apssaat-audit/";


    //RequestMethods
    public static String REQUEST_SYNCDATA = "syncData";


    public static int PANCHAYATID = 0;
    public static int MANDALID = 0;
    public static int DISTRICTID = 0;
    public static String PANCHAYATNAME = "";


    //Service
    public static String SUCCESS = "SUCCESS";
    public static String FAILED = "FAILED";
    public static String EXCEPTION = "EXCEPTION";
    public static String SENT = "SENT";
}
