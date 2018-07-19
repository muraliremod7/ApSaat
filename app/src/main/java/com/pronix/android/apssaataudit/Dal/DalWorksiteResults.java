package com.pronix.android.apssaataudit.Dal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pronix.android.apssaataudit.common.SqliteConstants;
import com.pronix.android.apssaataudit.common.Utils;
import com.pronix.android.apssaataudit.db.DBManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by ravi on 1/2/2018.
 */

public class DalWorksiteResults {
    public long insertOrUpdateWorksiteResultData(SQLiteDatabase database, String sNo, String workCode, String workDetails, String taskDetails,
                                                 String technologyType, String approvedEvMeasurement, String approvedevTotal, String aspermbReportMeadurement,
                                                 String asperReporttotal, String isWorkDone, String chekValuesMeasurement, String checkValuesTotal, String differenceMeasurements,
                                                 String differenceTotal, String respPersonName, String respPersonDesign, String impOfWork, String comments, String createdDate,
                                                 String createdBy, String modifiedDate, String modifiedBy, String isActive, String taskCode,
                                                 String districtCode, String mandalCode, String panchayatCode)
    {
        long res = -1;
        String strWhereClauseValues = "";
        try
        {
            ContentValues newTaskValue = new ContentValues();
            newTaskValue.put("sno", sNo);
            newTaskValue.put("work_code", workCode);
            newTaskValue.put("work_details", workDetails);
            newTaskValue.put("task_details", taskDetails);
            newTaskValue.put("technology_type", technologyType);
            newTaskValue.put("approved_ev_measurements", approvedEvMeasurement);
            newTaskValue.put("approved_ev_total", approvedevTotal);
            newTaskValue.put("aspermb_report_measurements", aspermbReportMeadurement);
            newTaskValue.put("aspermb_report_total", asperReporttotal);
            newTaskValue.put("is_work_done", isWorkDone);
            newTaskValue.put("chechvalues_measurements", chekValuesMeasurement);
            newTaskValue.put("chechvalues_total", checkValuesTotal);
            newTaskValue.put("difference_measurements", differenceMeasurements);
            newTaskValue.put("difference_total", differenceTotal);
            newTaskValue.put("respPersonName", respPersonName);
            newTaskValue.put("respPersonDesig", respPersonDesign);
            newTaskValue.put("imp_of_work", impOfWork);
            newTaskValue.put("comments", comments);

            newTaskValue.put("modified_date", modifiedDate);
            newTaskValue.put("modified_by", modifiedBy.trim());
            newTaskValue.put("isActive", isActive);
            newTaskValue.put("task_code", taskCode);
            newTaskValue.put("district_code", districtCode);
            newTaskValue.put("mandal_code", mandalCode);
            newTaskValue.put("panchayat_code", panchayatCode);
            newTaskValue.put("serverflag", "0");

            strWhereClauseValues = workCode + "," + taskCode;

            res = DBManager.getInstance().updateRecord("format5A", newTaskValue,
                    "work_code=? AND task_code=? " , strWhereClauseValues);
            if (res == 0)
            {
                newTaskValue.put("created_date", createdDate.trim());
                newTaskValue.put("created_by", createdBy);
                newTaskValue.put("modified_date","");
                newTaskValue.put("modified_by","");
                res = DBManager.getInstance().insertRecord("format5A",  newTaskValue);
            }
        }
        catch (Exception e)
        {

        }
        return res;
    }

    public JSONArray getWorksiteData() throws Exception
    {
        Cursor cursor;
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;

        try
        {
            String query = "SELECT work_code, task_code, approved_ev_measurements, approved_ev_total, aspermb_report_measurements," +//4
                    "aspermb_report_total, is_work_done, chechvalues_measurements, chechvalues_total, difference_measurements, difference_total," +//10
                    "respPersonName, respPersonDesig, imp_of_work, comments, created_date, created_by, modified_date, modified_by, isActive" +//19
                    ", district_code, mandal_code, panchayat_code" +//22
                    " FROM format5A WHERE IFNULL(serverflag,'0') = '0'";
            cursor = DBManager.getInstance().getRawQuery(query);

            if(cursor.moveToFirst())
            {
                do {
                    jsonObject = new JSONObject();
                    jsonObject.put("districtCode", cursor.getInt(20));
                    jsonObject.put("mandalCode", cursor.getInt(21));
                    jsonObject.put("panchayatCode", cursor.getInt(22));
                    jsonObject.put("workCode", cursor.getString(0));
                    jsonObject.put("taskCode", cursor.getString(1));
//                    jsonObject.put("approved_ev_measurements", cursor.getString(2));
//                    jsonObject.put("approved_ev_total", cursor.getString(3));
//                    jsonObject.put("aspermb_report_measurements", cursor.getString(4));
//                    jsonObject.put("aspermb_report_total", cursor.getString(5));
                    jsonObject.put("isWorkDone", cursor.getString(6));
                    jsonObject.put("checkValuesMeasurements", cursor.getString(7));
                    jsonObject.put("checkValuesTotal", cursor.getString(8));
                    jsonObject.put("differenceMeasurements", cursor.getString(9));
                    jsonObject.put("differenceTotal", cursor.getString(10));
                    jsonObject.put("respPersonName", cursor.getString(11));
                    jsonObject.put("respPersonDesig", cursor.getString(12));
                    jsonObject.put("impOfWork", cursor.getString(13));
                    jsonObject.put("comments", cursor.getString(14));
                    jsonObject.put("createdDate", cursor.getString(15));
                    jsonObject.put("createdBy", cursor.getString(16));
                    jsonObject.put("modifiedDate", cursor.getString(17));
                    jsonObject.put("modifiedBy", cursor.getString(18));
                    jsonArray.put(jsonObject);

                }while (cursor.moveToNext());

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
        String query = "UPDATE format5A SET serverflag = '1'";
        DBManager.getInstance().executeQuery(query);
    }






}
