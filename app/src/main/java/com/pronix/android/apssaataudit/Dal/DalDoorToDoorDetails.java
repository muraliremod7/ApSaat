package com.pronix.android.apssaataudit.Dal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.pronix.android.apssaataudit.common.Constants;
import com.pronix.android.apssaataudit.common.SqliteConstants;
import com.pronix.android.apssaataudit.common.Utils;
import com.pronix.android.apssaataudit.db.DBManager;
import com.pronix.android.apssaataudit.models.DistrictDO;
import com.pronix.android.apssaataudit.models.PanchayatDO;
import com.pronix.android.apssaataudit.models.Worker;
import com.pronix.android.apssaataudit.pojo.Habitations;
import com.pronix.android.apssaataudit.pojo.Villages;

import java.util.ArrayList;

/**
 * Created by ravi on 1/2/2018.
 */

public class DalDoorToDoorDetails {


    public void insertOrUpdateDoorToDoorData(String[] data) {

        long res = -1;
        String strWhereClauseValues = "";
        try {

            ContentValues newTaskValue = new ContentValues();
            newTaskValue.put("district_code", data[0]);
            newTaskValue.put("mandal_code", data[1]);
            newTaskValue.put("panchayat_code", data[2]);
            newTaskValue.put("village_code", data[3]);
            newTaskValue.put("habitation_code", data[4]);
            newTaskValue.put("ssaat_code", data[5]);
            newTaskValue.put("household_code", data[6]);
            newTaskValue.put("worker_code", data[7]);
            newTaskValue.put("surname", data[8]);
            newTaskValue.put("telugu_surname", data[9]);
            newTaskValue.put("name", data[10]);
            newTaskValue.put("telugu_name", data[11]);
            newTaskValue.put("account_no", data[12]);
            newTaskValue.put("work_code", data[13]);
            newTaskValue.put("work_name", data[14]);
            newTaskValue.put("work_name_telugu", data[15]);
            newTaskValue.put("work_location", data[16]);
            newTaskValue.put("work_location_telugu", data[17]);
            newTaskValue.put("work_progress_code", data[18]);
            newTaskValue.put("from_date", data[19]);
            newTaskValue.put("to_date", data[20]);
            newTaskValue.put("days_worked", data[21]);
            newTaskValue.put("amount_paid", data[22]);
            newTaskValue.put("payment_date", data[23]);
            newTaskValue.put("audit_payslip_date", data[24]);
            newTaskValue.put("audit_is_passbook_avail", data[25]);
            newTaskValue.put("audit_is_payslip_issuing", data[26]);
            newTaskValue.put("audit_is_jobcard_avail", data[27]);
            newTaskValue.put("audit_days_worked", data[28]);
            newTaskValue.put("audit_amount_rec", data[29]);
            newTaskValue.put("audit_remarks", data[30]);
            newTaskValue.put("status", data[31]);
            newTaskValue.put("sent_file_name", data[32]);
            newTaskValue.put("sent_date", data[33]);
            newTaskValue.put("resp_filename", data[34]);
            newTaskValue.put("resp_date", data[35]);
            newTaskValue.put("created_date", data[36]);
            newTaskValue.put("department", data[37]);
            newTaskValue.put("muster_id", data[38]);


            strWhereClauseValues = data[6] + "," + data[7] + "," + data[13] + ","
                    + data[19] + "," + data[20] + "," + data[38];

            res = DBManager.getInstance().updateRecord("employees", newTaskValue,
                    "household_code=? " +
                            "AND worker_code=?  AND work_code=? AND from_date=? AND to_date=? AND muster_id=?", strWhereClauseValues);
            if (res == 0) {
                res = DBManager.getInstance().insertRecord("employees", newTaskValue);
            }
        } catch (Exception e) {

        }
//        database.update("worksite", )
    }


    public void insertOrUpdateDoorToDoorDetails(String districCode, String mandalCode, String panchayatCode, String villageCode,
                                                String habitationCode, String ssaatCode, String householdCode, String workerCode,
                                                String surname, String teluguSurName, String name, String teluguName, String accountNo,
                                                String workCode, String workName, String workNameTelugu, String workLocation,
                                                String workLocationTelugu, String workProgressCode, String fromDate, String toDate,
                                                String daysWorked, String amountPaid, String paymentDate, String auditPayslipDate, String auditIsPassbookAvail,
                                                String auditIsPayslipIssuing, String auditIsJobCardAvail, String auditDaysWorked, String audiAmtRec, String auditRemarks,
                                                String status, String sentFileName, String sentDate, String resFileName, String resDate, String creratedDate,
                                                String department, String musterId, String panchayatName, String villageName, String habitationName) {

        long res = -1;
        String strWhereClauseValues = "";
        try {

            ContentValues newTaskValue = new ContentValues();
            newTaskValue.put("district_code", districCode);
            newTaskValue.put("mandal_code", mandalCode);
            newTaskValue.put("panchayat_code", panchayatCode);
            newTaskValue.put("village_code", villageCode);
            newTaskValue.put("habitation_code", habitationCode);
            newTaskValue.put("ssaat_code", ssaatCode);
            newTaskValue.put("household_code", householdCode);
            newTaskValue.put("worker_code", workerCode);
            newTaskValue.put("surname", surname);
            newTaskValue.put("telugu_surname", teluguSurName);
            newTaskValue.put("name", name);
            newTaskValue.put("telugu_name", teluguName);
            newTaskValue.put("account_no", accountNo);
            newTaskValue.put("work_code", workCode);
            newTaskValue.put("work_name", workName);
            newTaskValue.put("work_name_telugu", workNameTelugu);
            newTaskValue.put("work_location", workLocation);
            newTaskValue.put("work_location_telugu", workLocationTelugu);
            newTaskValue.put("work_progress_code", workProgressCode);
            newTaskValue.put("from_date", fromDate);
            newTaskValue.put("to_date", toDate);
            newTaskValue.put("days_worked", daysWorked);
            newTaskValue.put("amount_paid", amountPaid);
            newTaskValue.put("payment_date", paymentDate);
            newTaskValue.put("audit_payslip_date", auditPayslipDate);
            newTaskValue.put("audit_is_passbook_avail", auditIsPassbookAvail);
            newTaskValue.put("audit_is_payslip_issuing", auditIsPayslipIssuing);
            newTaskValue.put("audit_is_jobcard_avail", auditIsJobCardAvail);
            newTaskValue.put("audit_days_worked", auditDaysWorked);
            newTaskValue.put("audit_amount_rec", audiAmtRec);
            newTaskValue.put("audit_remarks", auditRemarks);
            newTaskValue.put("status", status);
            newTaskValue.put("sent_file_name", sentFileName);
            newTaskValue.put("sent_date", sentDate);
            newTaskValue.put("resp_filename", resFileName);
            newTaskValue.put("resp_date", resDate);
            newTaskValue.put("created_date", creratedDate);
            newTaskValue.put("department", department);
            newTaskValue.put("muster_id", musterId);
            newTaskValue.put("panchayat_name", panchayatName);
            newTaskValue.put("village_name", villageName);
            newTaskValue.put("habitation_name", habitationName);


            strWhereClauseValues = householdCode + "," + workerCode + "," + workCode + ","
                    + fromDate + "," + toDate + "," + musterId;

            res = DBManager.getInstance().updateRecord("employees", newTaskValue,
                    "household_code=? " +
                            "AND worker_code=?  AND work_code=? AND from_date=? AND to_date=? AND muster_id=?", strWhereClauseValues);
            if (res == 0) {
                res = DBManager.getInstance().insertRecord("employees", newTaskValue);
            }
        } catch (Exception e) {

        }
//        database.update("worksite", )
    }

    public int updateRecord(SQLiteDatabase database, String tableName, ContentValues contentValues, String whereClause, String whereArgs) {
        int iResult = 0;
        String[] strWhereArgs = whereArgs.split(",");
        try {
            try {
                iResult = database.update(tableName, contentValues, whereClause, strWhereArgs);
            } catch (Exception e) {
//                AndroidUtils.logMsg("DBManager.updateRecord(): TableName: " + tableName + " " + e.getMessage());
            }
        } catch (SQLiteException e) {
//            AndroidUtils.logMsg("DBManager.getScalar(): " + e.getMessage());
        }

        return iResult;
    }

    public ArrayList<Worker> getDoorToDoorDetails(String householdCode) {
        ArrayList<Worker> arrayList = new ArrayList<>();
        Cursor cursorEmployees = null;
        String query = "SELECT EM.id, EM.district_code, EM.mandal_code, EM.panchayat_code, EM.village_code, EM.habitation_code, \n" +
                "EM.ssaat_code, EM.household_code, EM.worker_code, EM.surname,EM.telugu_surname, EM.name, EM.telugu_name, EM.account_no,\n" +
                "EM.work_code, EM.work_name, EM.work_name_telugu, EM.work_location, EM.work_location_telugu, EM.work_progress_code, \n" +
                "EM.from_date, EM.to_date, EM.days_worked, EM.amount_paid, EM.payment_date, EM.audit_payslip_date, \n" +
                "EM.audit_is_passbook_avail, EM.audit_is_payslip_issuing, EM.audit_is_jobcard_avail, EM.audit_days_worked, \n" +
                "EM.audit_amount_rec, EM.audit_remarks, EM.status, EM.sent_file_name, EM.sent_date, EM.resp_filename, EM.resp_date, EM.created_date, EM.department, EM.muster_id,\n" +
                "IFNULL(f4a.actualWorkedDays,''), IFNULL(f4a.actualAmtPaid,''), IFNULL(f4a.differenceInAmt,''), IFNULL(f4a.isJobCardAvail,''), IFNULL(f4a.isPassbookAvail,''), IFNULL(f4a.isPayslipIssued,''),\n" +
                "IFNULL(f4a.respPersonName,''), IFNULL(f4a.respPersonDesig,''), IFNULL(f4a.categoryone,''), IFNULL(f4a.categorytwo,''), " +
                "IFNULL(f4a.categorythree,''), IFNULL(f4a.comments,''), IFNULL(f4a.serverflag,'') \n" +
                " FROM employees AS EM LEFT OUTER JOIN format4A AS f4a ON EM.household_code = IFNULL(f4a.wageSeekerId,'') \n" +
                " AND EM.muster_id = IFNULL(f4a.musterId,'')  WHERE EM.household_code IN ('" + householdCode + "')";
        cursorEmployees = DBManager.getInstance().getRawQuery(query);
        arrayList.clear();
        int count = 1;
        if (cursorEmployees.moveToFirst()) {
            do {
                arrayList.add(new Worker(
                        String.valueOf(count++),
                        cursorEmployees.getString(1),
                        cursorEmployees.getString(2),
                        cursorEmployees.getString(3),
                        cursorEmployees.getString(4),
                        cursorEmployees.getString(5),
                        cursorEmployees.getString(6),
                        cursorEmployees.getString(7),
                        cursorEmployees.getString(8),
                        cursorEmployees.getString(9),
                        cursorEmployees.getString(11),
                        cursorEmployees.getString(13),
                        cursorEmployees.getString(14),
                        cursorEmployees.getString(15),
                        cursorEmployees.getString(17),
                        cursorEmployees.getString(19),
                        cursorEmployees.getString(20),
                        cursorEmployees.getString(21),
                        cursorEmployees.getString(22),
                        cursorEmployees.getString(23),
                        cursorEmployees.getString(24),
                        cursorEmployees.getString(25),
                        cursorEmployees.getString(39),
                        cursorEmployees.getString(40),
                        cursorEmployees.getString(41),
                        cursorEmployees.getString(42),
                        cursorEmployees.getString(43),
                        cursorEmployees.getString(44),
                        cursorEmployees.getString(45),
                        cursorEmployees.getString(46),
                        cursorEmployees.getString(47),
                        cursorEmployees.getString(48),
                        cursorEmployees.getString(49),
                        cursorEmployees.getString(50),
                        cursorEmployees.getString(51),
                        cursorEmployees.getString(52)));
            } while (cursorEmployees.moveToNext());
        }
        return arrayList;
    }

    public ArrayList<PanchayatDO> getDownloadedPanchayats() {
        Cursor c = null;
        ArrayList<PanchayatDO> arrayList = new ArrayList<>();
        PanchayatDO panchayatDO;
        String query = "SELECT DISTINCT (panchayat_code), panchayat_name, mandal_code, district_code FROM employees";
        c = DBManager.getInstance().getRawQuery(query);
        if (c.moveToFirst()) {
            do {
                panchayatDO = new PanchayatDO();
                panchayatDO.setPanchayatCode(c.getInt(0));
                panchayatDO.setPanchayatName(c.getString(1));
                panchayatDO.setMandalCode(c.getInt(2));
                panchayatDO.setDistrictCode(c.getInt(3));
                arrayList.add(panchayatDO);

            } while (c.moveToNext());
        }
        return arrayList;
    }

    public ArrayList<Villages> getVillages(String panchayatCode) {
        Cursor c = null;
        ArrayList<Villages> arrayList = new ArrayList<>();
        Villages villages;
        String query = "SELECT DISTINCT(village_code), village_name FROM " + SqliteConstants.TABLE_FORMAT4A
                + " WHERE panchayat_code = " + Utils.getQuotedString(panchayatCode);
        c = DBManager.getInstance().getRawQuery(query);
        if (c.moveToFirst()) {
            do {
                villages = new Villages();
                villages.setVillageCode(c.getString(0));
                villages.setVillageName(c.getString(1));
                arrayList.add(villages);

            } while (c.moveToNext());
        }
        return arrayList;
    }

    public ArrayList<Habitations> getHabitations(String villageCode) {
        Cursor c = null;
        ArrayList<Habitations> arrayList = new ArrayList<>();
        Habitations habitations;
        String query = "SELECT DISTINCT(habitation_code), mandal_code, district_code, habitation_name FROM " + SqliteConstants.TABLE_FORMAT4A
                + " WHERE village_code = " + Utils.getQuotedString(villageCode);
        c = DBManager.getInstance().getRawQuery(query);
        if (c.moveToFirst()) {
            do {
                habitations = new Habitations();
                habitations.setHabitationCode(c.getString(0));
                habitations.setMandalCode(c.getString(1));
                habitations.setDistrictCode(c.getString(2));
                habitations.setHabitationName(c.getString(3));
                arrayList.add(habitations);

            } while (c.moveToNext());
        }
        return arrayList;
    }

    public ArrayList<String> getHouseholdCodes(String villageCode, String habitationCode, String panchayatCode)
    {
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor c = null;
        String householdCode = "";
        String query = "SELECT DISTINCT(household_code) FROM " + SqliteConstants.TABLE_FORMAT4A + " WHERE village_code = "
                + Utils.getQuotedString(villageCode) + " AND habitation_code = " + Utils.getQuotedString(habitationCode)
                + " AND panchayat_code = " + Utils.getQuotedString(panchayatCode);
        c = DBManager.getInstance().getRawQuery(query);
        if(c.moveToFirst())
        {
            do {
                householdCode = c.getString(0);
                arrayList.add(householdCode.substring(householdCode.length()-4, householdCode.length()));
            }while (c.moveToNext());
        }
        return arrayList;

    }

    public String[] getTotalRecords(String panchayatCode)
    {
        String[] value = new String[2];
        Cursor c = null;
        String query = "SELECT count(1) FROM " + SqliteConstants.TABLE_FORMAT4A + " WHERE panchayat_code = " + Utils.getQuotedString(panchayatCode)
                + " UNION ALL " +
                " SELECT count(1) FROM format4A WHERE panchayat_code = " + Utils.getQuotedString(panchayatCode);
        c = DBManager.getInstance().getRawQuery(query);
        int i = 0;
        if(c.moveToFirst())
        {
            do {
              value[i++] = c.getString(0);

            }while (c.moveToNext());
        }
        return value;
    }

    public String[] getVillageWiseTotalRecords(String villageCode)
    {
        String[] value = new String[2];
        Cursor c = null;
        String query = "SELECT count(1) FROM " + SqliteConstants.TABLE_FORMAT4A + " WHERE village_code = " + Utils.getQuotedString(villageCode)
                + " UNION ALL " +
                " SELECT count(1) FROM format4A AS F4A, "+SqliteConstants.TABLE_FORMAT4A +" AS Emp WHERE F4A.wageSeekerId = Emp.household_code AND " +
                " F4A.panchayat_code = Emp.panchayat_code AND Emp.village_code = " + Utils.getQuotedString(villageCode) + " AND F4A.musterId = Emp.muster_id ";
        c = DBManager.getInstance().getRawQuery(query);
        int i = 0;
        if(c.moveToFirst())
        {
            do {
                value[i++] = c.getString(0);

            }while (c.moveToNext());
        }
        return value;
    }

    public String[] getHabitationWiseTotalRecords(String habitationCode, String villageCode)
    {
        String[] value = new String[2];
        Cursor c = null;
        String query = "SELECT count(1) FROM " + SqliteConstants.TABLE_FORMAT4A + " WHERE habitation_code = " + Utils.getQuotedString(habitationCode)
                + " AND village_code = " + Utils.getQuotedString(villageCode)
                + " UNION ALL " +
                " SELECT count(1) FROM format4A AS F4A, "+SqliteConstants.TABLE_FORMAT4A +" AS Emp WHERE F4A.wageSeekerId = Emp.household_code AND " +
                " F4A.panchayat_code = Emp.panchayat_code AND Emp.habitation_code = " + Utils.getQuotedString(habitationCode) + " AND Emp.village_code = " + Utils.getQuotedString(villageCode)
                + " AND F4A.musterId = Emp.muster_id ";
        c = DBManager.getInstance().getRawQuery(query);
        int i = 0;
        if(c.moveToFirst())
        {
            do {
                value[i++] = c.getString(0);

            }while (c.moveToNext());
        }
        return value;
    }

    public String getHouseholdCode(String districtCode, String mandalCode, String panchayatCode)
    {
        Cursor c = null;
        String householdCode = "";
        String query = "SELECT household_code FROM " + SqliteConstants.TABLE_FORMAT4A + " WHERE district_code = "
                + Utils.getQuotedString(districtCode) + " AND mandal_code = " + Utils.getQuotedString(mandalCode)
                + " AND panchayat_code = " + Utils.getQuotedString(panchayatCode) + " LIMIT 1";
        c = DBManager.getInstance().getRawQuery(query);
        if(c.moveToFirst())
        {
                householdCode = c.getString(0);
            householdCode = householdCode.substring(2, 5);
        }
        return householdCode;

    }

}
