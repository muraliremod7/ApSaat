package com.pronix.android.apssaataudit;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.pronix.android.apssaataudit.Dal.DalDoorToDoorDetails;
import com.pronix.android.apssaataudit.common.Constants;
import com.pronix.android.apssaataudit.db.DBManager;
import com.pronix.android.apssaataudit.pojo.Habitations;
import com.pronix.android.apssaataudit.pojo.Villages;
import com.pronix.android.apssaataudit.render.CommonSpinnerAdapter;

import java.util.ArrayList;

public class DoortoDoorAtivity extends BaseActivity {
    Button btn_search;
    EditText et_heirarchyCode;
    AutoCompleteTextView householdCode;
    TextView tv_PanchayatName;
    TextView tv_pancahayatTotal, tv_Panchayatcompleted, tv_VillageTotal, tv_VillageCompleted, tv_HabitationTotal, tv_HabitationCompleted;
    Spinner sp_VillageCode, sp_Habitationcode;
    String selectedVillageCode = "";
    private ArrayList<String> checkList = new ArrayList<String>();
    private ArrayList<String> checkList2 = new ArrayList<String>();
    ArrayList<Villages> villagesArrayList = new ArrayList<>();
    ArrayList<Habitations> habitationsArrayList = new ArrayList<>();
    ArrayList<String> householdCodes = new ArrayList<>();
    DalDoorToDoorDetails dalDoorToDoorDetails;
    String assemblyCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_doortodoor, frame_base);
        dalDoorToDoorDetails = new DalDoorToDoorDetails();
        householdCode = (AutoCompleteTextView) findViewById(R.id.householdCode);
        tv_PanchayatName = (TextView) findViewById(R.id.tv_PanchayatName);
        tv_PanchayatName.setText("Panchayat : " + Constants.PANCHAYATNAME);
        initializeControls();
        btn_search = (Button) findViewById(R.id.btn_search);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!householdCode.getText().toString().equalsIgnoreCase("")) {
                    checkData();
                } else {
                    Toast.makeText(DoortoDoorAtivity.this, "Please enter work card ID", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Checking if items are there in Format4A table
//        checkDataForm4aTable();
    }

    public void initializeControls() {
        try {
            sp_Habitationcode = (Spinner) findViewById(R.id.sp_HabitationCode);
            sp_VillageCode = (Spinner) findViewById(R.id.sp_VillageCode);
            et_heirarchyCode = (EditText) findViewById(R.id.et_HierarchyCode);
            villagesArrayList = dalDoorToDoorDetails.getVillages(String.valueOf(Constants.PANCHAYATID));
            tv_pancahayatTotal = (TextView) findViewById(R.id.tv_panchayatTotal);
            tv_Panchayatcompleted = (TextView) findViewById(R.id.tv_panchayatCompleted);
            tv_VillageTotal = (TextView) findViewById(R.id.tv_VillageTotal);
            tv_VillageCompleted = (TextView) findViewById(R.id.tv_VillageCompleted);
            tv_HabitationTotal = (TextView) findViewById(R.id.tv_HabitationTotal);
            tv_HabitationCompleted = (TextView) findViewById(R.id.tv_HabitationCompleted);
            String[] records = dalDoorToDoorDetails.getTotalRecords(String.valueOf(Constants.PANCHAYATID));
            tv_pancahayatTotal.setText(records[0]);
            tv_Panchayatcompleted.setText(records[1]);
            assemblyCode = dalDoorToDoorDetails.getHouseholdCode(String.valueOf(Constants.DISTRICTID), String.valueOf(Constants.MANDALID), String.valueOf(Constants.PANCHAYATID));
//        Villages villages = new Villages();
//        villages.setVillageCode("--Select--");
//        villagesArrayList.add(0, villages);
            CommonSpinnerAdapter adapter = new CommonSpinnerAdapter(this, villagesArrayList);
            sp_VillageCode.setAdapter(adapter);
            sp_VillageCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    habitationsArrayList.clear();
                    habitationsArrayList = dalDoorToDoorDetails.getHabitations(villagesArrayList.get(i).getVillageCode());
                    selectedVillageCode = villagesArrayList.get(i).getVillageCode();
                    Habitations habitations = new Habitations();
                    habitations.setHabitationCode("--Select--");
                    habitations.setHabitationName("");
                    habitationsArrayList.add(0, habitations);
                    CommonSpinnerAdapter adapter1 = new CommonSpinnerAdapter(DoortoDoorAtivity.this, habitationsArrayList);
                    sp_Habitationcode.setAdapter(adapter1);
//                    if (i != 0) {
                        String[] value = dalDoorToDoorDetails.getVillageWiseTotalRecords(villagesArrayList.get(i).getVillageCode());
                        tv_VillageTotal.setText(value[0]);
                        tv_VillageCompleted.setText(value[1]);
//                    }
//                    else {
//                        tv_VillageTotal.setText("0");
//                        tv_VillageCompleted.setText("0");
//                    }
//                sp_Habitationcode.setOnItemSelectedListener(this);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            sp_Habitationcode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        if (i != 0) {
                            String districtCode = habitationsArrayList.get(i).getDistrictCode();
                            String mandalCode = habitationsArrayList.get(i).getMandalCode();
                            String panchayatCode = String.valueOf(Constants.PANCHAYATID);
                            String villageCode = selectedVillageCode;
                            String habitationCode = habitationsArrayList.get(i).getHabitationCode();
                            householdCodes = dalDoorToDoorDetails.getHouseholdCodes(villageCode, habitationCode, panchayatCode);
                            districtCode = (districtCode.length() > 1 ? districtCode : "0" + districtCode);
                            mandalCode = mandalCode.length() > 1 ? mandalCode : "0" + mandalCode;
                            villageCode = villageCode.length() > 1 ? villageCode : "0" + villageCode;
                            panchayatCode = panchayatCode.length() > 1 ? panchayatCode : "0" + panchayatCode;
                            habitationCode = habitationCode.length() > 1 ? habitationCode : "0" + habitationCode;
                            et_heirarchyCode.setText(districtCode + assemblyCode + mandalCode + panchayatCode + villageCode + habitationCode);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                    (DoortoDoorAtivity.this, android.R.layout.select_dialog_item, householdCodes);
                            householdCode.setThreshold(1);
                            householdCode.setAdapter(adapter);
                            String[] value = dalDoorToDoorDetails.getHabitationWiseTotalRecords(habitationsArrayList.get(i).getHabitationCode(), selectedVillageCode);
                            tv_HabitationTotal.setText(value[0]);
                            tv_HabitationCompleted.setText(value[1]);
                        } else {
                            et_heirarchyCode.setText("");
                            householdCodes.clear();
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                    (DoortoDoorAtivity.this, android.R.layout.select_dialog_item, householdCodes);
                            householdCode.setThreshold(1);
                            householdCode.setAdapter(adapter);
                            tv_HabitationTotal.setText("0");
                            tv_HabitationCompleted.setText("0");
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        catch (Exception e)
        {
            e.getMessage();
        }
    }

    private void checkData() {
        //opening the database
        String jobSeekerId = et_heirarchyCode.getText().toString() + householdCode.getText().toString();
        //we used rawQuery(sql, selectionargs) for fetching all the employees
        Cursor cursorEmployees = DBManager.getInstance().getRawQuery("SELECT * FROM employees WHERE household_code IN ('" + jobSeekerId + "')");
        //if the cursor has some data
        if (cursorEmployees.moveToFirst()) {
            //looping through all the records
            do {
                checkList.add(cursorEmployees.getString(1));
                break;
            } while (cursorEmployees.moveToNext());
        }
        //closing the cursor
        cursorEmployees.close();
        System.out.println("-----------checking employees with housecode array size: " + checkList.size());
        if (checkList.size() == 0) {
            Toast.makeText(DoortoDoorAtivity.this, "Please enter valid Household code", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(DoortoDoorAtivity.this, Format4ARow.class);
            intent.putExtra("householdCode", jobSeekerId);
            startActivity(intent);
            DoortoDoorAtivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    }

    public void checkDataForm4aTable() {

        //we used rawQuery(sql, selectionargs) for fetching all the employees
        Cursor cursorFormat4A = DBManager.getInstance().getRawQuery("SELECT * FROM format4A");
        //if the cursor has some data

        if (cursorFormat4A.moveToFirst()) {
            //looping through all the records
            do {
                checkList2.add(cursorFormat4A.getString(22));
                System.out.println("in db date: " + cursorFormat4A.getString(22));
            } while (cursorFormat4A.moveToNext());
        }
        //closing the cursor
        cursorFormat4A.close();
        System.out.println("Format 4A Size: " + checkList2.size());
    }


}
