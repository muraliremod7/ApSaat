package com.pronix.android.apssaataudit.Dal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pronix.android.apssaataudit.db.DBManager;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by ravi on 1/2/2018.
 */

public class DalDoorToDoorResults {

    public long insertOrUpdateDoorToDoorResultData(SQLiteDatabase database, String sNo, String wageSeekerId, String workerCode,String fullName,
                                                   String postBank, String workDetails, String workDuration, String payOrderRelDate,
                                                   String musterId, String workedDays, String amtToBePaid, String actualWorkedDays,
                                                   String actualAmtPaid, String differenceInAmt, String isJobCardAvail, String isPassbookAvail,
                                                   String isPayslipIssued, String resPersonName, String resPersonDesig, String categoryOne, String categoryTwo,
                                                   String categoryThree, String createDate, String comments, String createdBy, String modifiedDate,
                                                   String modifiedBy, String isActive, String districCode, String mandalCode, String panchayatCode) {
        long res = -1;
        String strWhereClauseValues = "";
        try {
            ContentValues newTaskValue = new ContentValues();
            newTaskValue.put("sno", sNo);
            newTaskValue.put("wageSeekerId", wageSeekerId.trim());
            newTaskValue.put("worker_code", workerCode.trim());
            newTaskValue.put("fullname", fullName);
            newTaskValue.put("postBank", postBank);
            newTaskValue.put("work_details", workDetails);
            newTaskValue.put("work_duration", workDuration);
            newTaskValue.put("payOrderRelDate", payOrderRelDate);
            newTaskValue.put("musterId", musterId);
            newTaskValue.put("workedDays", workedDays);
            newTaskValue.put("amtToBePaid", amtToBePaid);
            newTaskValue.put("actualWorkedDays", actualWorkedDays);
            newTaskValue.put("actualAmtPaid", actualAmtPaid);
            newTaskValue.put("differenceInAmt", differenceInAmt);
            newTaskValue.put("isJobCardAvail", isJobCardAvail);
            newTaskValue.put("isPassbookAvail", isPassbookAvail);
            newTaskValue.put("isPayslipIssued", isPayslipIssued);
            newTaskValue.put("respPersonName", resPersonName);
            newTaskValue.put("respPersonDesig", resPersonDesig);
            newTaskValue.put("categoryone", categoryOne);
            newTaskValue.put("categorytwo", categoryTwo);
            newTaskValue.put("categorythree", categoryThree);
            newTaskValue.put("comments", comments);
            newTaskValue.put("modified_date", modifiedDate);
            newTaskValue.put("modified_by", modifiedBy);
            newTaskValue.put("isActive", isActive);
            newTaskValue.put("district_code", districCode);
            newTaskValue.put("mandal_code", mandalCode);
            newTaskValue.put("panchayat_code", panchayatCode);
            newTaskValue.put("serverflag", "0");


            strWhereClauseValues = wageSeekerId.trim() + "," + workerCode.trim() + "," + musterId.trim();

            res = DBManager.getInstance().updateRecord( "format4A", newTaskValue,
                    "wageSeekerId=? " + " AND worker_code=? " +
                            " AND musterId=?", strWhereClauseValues);
            if (res == 0) {
                newTaskValue.put("created_date", createDate);
                newTaskValue.put("created_by", createdBy);
                newTaskValue.put("modified_date","");
                newTaskValue.put("modified_by","");
                res = DBManager.getInstance().insertRecord("format4A", newTaskValue);
            }
        } catch (Exception e) {

        }
        return res;
    }

    public JSONArray getDoorToDoorData() throws Exception
    {
        Cursor cursor;
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        try {
            String query = "SELECT wageSeekerId, worker_code, fullname, musterId, workedDays, amtToBePaid,  actualWorkedDays," +//6
                    "actualAmtPaid, differenceInAmt, isJobCardAvail, isPassbookAvail, isPayslipIssued, respPersonName," +//12
                    "respPersonDesig, categoryone, categorytwo, categorythree, comments, created_date, created_by," +//19
                    "modified_date, modified_by, district_code, mandal_code, panchayat_code FROM format4A WHERE IFNULL(serverflag,'0') = '0'";
            cursor = DBManager.getInstance().getRawQuery(query);
            if (cursor.moveToFirst()) {
                do {
                    jsonObject = new JSONObject();
                    jsonObject.put("districtCode", cursor.getInt(22));
                    jsonObject.put("mandalCode", cursor.getInt(23));
                    jsonObject.put("panchayatCode", cursor.getInt(24));
                    jsonObject.put("householdCode", cursor.getString(0));
                    jsonObject.put("workerCode", cursor.getString(1));
                    jsonObject.put("musterId", cursor.getString(3));
                    jsonObject.put("workedDays", cursor.getString(4));
                    jsonObject.put("amtToBePaid", cursor.getString(5));
                    jsonObject.put("actualWorkedDays", cursor.getString(6));
                    jsonObject.put("actualAmtPaid", cursor.getString(7));
                    jsonObject.put("differenceInAmt", cursor.getString(8));
                    jsonObject.put("isJobCardAvail", cursor.getString(9));
                    jsonObject.put("isPassbookAvail", cursor.getString(10));
                    jsonObject.put("isPaySlipIssued", cursor.getString(11));
                    jsonObject.put("resPersonName", cursor.getString(12));
                    jsonObject.put("respersonDesg", cursor.getString(13));
                    jsonObject.put("categoryOne", cursor.getString(14));
                    jsonObject.put("categoryTwo", cursor.getString(15));
                    jsonObject.put("categoryThree", cursor.getString(16));
                    jsonObject.put("comments", cursor.getString(17));
                    jsonObject.put("createdDate", cursor.getString(18));
                    jsonObject.put("createdBy", cursor.getString(19));
                    jsonObject.put("modifiedDate", cursor.getString(20));
                    jsonObject.put("modifiedBy", cursor.getString(21));

                    jsonArray.put(jsonObject);

                } while (cursor.moveToNext());



            }
        }
        catch (Exception e)
        {
            throw e;
        }
        return jsonArray;
    }

    public void updateServerFlag()
    {
        String query = "UPDATE format4A SET serverflag = '1'";
        DBManager.getInstance().executeQuery(query);
    }

}
