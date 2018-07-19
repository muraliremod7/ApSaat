package com.pronix.android.apssaataudit.Dal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pronix.android.apssaataudit.common.Constants;
import com.pronix.android.apssaataudit.common.SqliteConstants;
import com.pronix.android.apssaataudit.common.Utils;
import com.pronix.android.apssaataudit.db.DBManager;


/**
 * Created by ravi on 1/3/2018.
 */

public class DalUserMaster {

    public long saveUserDetails(String userId, String userName, String mobileNumber, String email, String pin, String designation, String employeeId, String timeStamp)
    {
        long res = -1;
        String strWhereClauseValues = "";
        try {
            ContentValues newTaskValue = new ContentValues();
            newTaskValue.put("userid", userId);
            newTaskValue.put("username", userName);
            newTaskValue.put("mobile_number",mobileNumber);
            newTaskValue.put("email",email);
            newTaskValue.put("pin",pin);
            newTaskValue.put("designation",designation);
            newTaskValue.put("emplyeeid",employeeId);
            newTaskValue.put("created_datetime","");
            strWhereClauseValues = mobileNumber;

            res = DBManager.getInstance().updateRecord(SqliteConstants.TABLE_USERMASTER,newTaskValue, "mobile_number=?", strWhereClauseValues);
            if(res == 0)
            res = DBManager.getInstance().insertRecord(SqliteConstants.TABLE_USERMASTER, newTaskValue);
        }
        catch (Exception e)
        {
            e.getMessage();
        }
        return res;
    }

    public String getPin(String enteredUserId)
    {
        String query = "SELECT pin FROM " + SqliteConstants.TABLE_USERMASTER + " WHERE mobile_number = " + Utils.getQuotedString(enteredUserId);
        return DBManager.getInstance().getScalar(query);

    }

    public String getMobile(String enteredMobile)
    {
        String query = "SELECT mobile_number FROM " + SqliteConstants.TABLE_USERMASTER + " WHERE mobile_number = '" + enteredMobile+"'";
        return DBManager.getInstance().getScalar(query);

    }

    public void getUserDetails(String pin, String mobile)
    {
        Cursor c = null;
        try {

            String query = "SELECT userid, username, mobile_number, email, pin, designation FROM " + SqliteConstants.TABLE_USERMASTER
                    + " WHERE pin = '" + pin + "' AND mobile_number = '" + mobile + "'";
            c = DBManager.getInstance().getRawQuery(query);
            if (c.moveToFirst()) {
                Constants.userMasterDO.userId = c.getString(0);
                Constants.userMasterDO.userName = c.getString(1);
                Constants.userMasterDO.mobileNumber = c.getString(2);
                Constants.userMasterDO.email = c.getString(3);
                Constants.userMasterDO.designation = c.getString(5);
            }

        }
        catch (Exception e)
        {
            e.getMessage();
        }
        finally {
            c.close();
        }

    }

    public void updatepin(String mobile, String pin)
    {
                String query = "UPDATE " + SqliteConstants.TABLE_USERMASTER + " SET pin = " + Utils.getQuotedString(pin) + " WHERE mobile_number ="
                        + Utils.getQuotedString(mobile);
                DBManager.getInstance().executeQuery(query);
    }

    public String getScalar(String s, SQLiteDatabase database) {
        String result = "";
        try {
            Cursor c = database.rawQuery(s, null);
            c.moveToFirst();
            // result=(isIntValue)?c.getInt(0)+"":c.getString(0);
            result = c.getString(0);
            c.close();
        } catch (Exception e) {
            // HANDLE THIS;
        }
        return result;
    }

    public void updateProfile(String name, String email, String mobileNumber)
    {
        String query = "UPDATE " + SqliteConstants.TABLE_USERMASTER + " SET username = " + Utils.getQuotedString(name) + ", email = "+
                Utils.getQuotedString(email) + " WHERE mobile_number = " + Utils.getQuotedString(mobileNumber);
        DBManager.getInstance().executeQuery(query);
    }
}
