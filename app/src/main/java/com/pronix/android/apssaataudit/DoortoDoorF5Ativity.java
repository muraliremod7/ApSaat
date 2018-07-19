package com.pronix.android.apssaataudit;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
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


import com.pronix.android.apssaataudit.Dal.DalWorksiteDetails;
import com.pronix.android.apssaataudit.common.Constants;
import com.pronix.android.apssaataudit.db.DBManager;
import com.pronix.android.apssaataudit.pojo.Habitations;
import com.pronix.android.apssaataudit.pojo.Villages;
import com.pronix.android.apssaataudit.render.CommonSpinnerAdapter;

import java.util.ArrayList;

public class DoortoDoorF5Ativity extends BaseActivity {
    Button btn_search;
    EditText et_WorkCode;
    AutoCompleteTextView actv_WorkId;
    private ArrayList<String> checkList = new ArrayList<String>();
    private ArrayList<String> checkList2 = new ArrayList<String>();
    TextView tv_PanchayatTotal, tv_PanchayatCompleted, tv_VillageTotal, tv_VillageCompleted;
    TextView tv_PanchayatName;
    Spinner sp_VillageCode;
    ArrayList<Villages> villagesArrayList = new ArrayList<>();
    ArrayList<String> workCodesArralist = new ArrayList<>();
    String assemblyCode = "";

    DalWorksiteDetails dalWorksiteDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_doortodoor5a, frame_base);
        dalWorksiteDetails = new DalWorksiteDetails();
        initializeControls();
        et_WorkCode = (EditText) findViewById(R.id.householdCode);
        et_WorkCode.setEnabled(false);
        btn_search= (Button) findViewById(R.id.btn_search);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_WorkCode.getText().toString().equalsIgnoreCase("")){
                    checkData();
                }else{
                    Toast.makeText(DoortoDoorF5Ativity.this,"Please enter work code",Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Checking if items are there in Format4A table
        checkDataForm5aTable();
    }

    public void initializeControls()
    {
        sp_VillageCode = (Spinner) findViewById(R.id.sp_VillageCode);
        villagesArrayList = dalWorksiteDetails.getVillages(String.valueOf(Constants.PANCHAYATID));
        tv_PanchayatTotal = (TextView) findViewById(R.id.tv_panchayatTotal);
        tv_PanchayatCompleted = (TextView) findViewById(R.id.tv_panchayatCompleted);
        tv_VillageTotal = (TextView) findViewById(R.id.tv_VillageTotal);
        tv_VillageCompleted = (TextView) findViewById(R.id.tv_VillageCompleted);
        actv_WorkId = (AutoCompleteTextView) findViewById(R.id.actv_WorkId);
        tv_PanchayatName = (TextView) findViewById(R.id.tv_PanchayatName);
        tv_PanchayatName.setText("Panchayat : " + Constants.PANCHAYATNAME);
        String[] value = dalWorksiteDetails.getTotalRecords(String.valueOf(Constants.PANCHAYATID));
        assemblyCode = dalWorksiteDetails.getworkCode(String.valueOf(Constants.DISTRICTID), String.valueOf(Constants.MANDALID), String.valueOf(Constants.PANCHAYATID));
        tv_PanchayatTotal.setText(value[0]);
        tv_PanchayatCompleted.setText(value[1]);

        CommonSpinnerAdapter adapter = new CommonSpinnerAdapter(this, villagesArrayList);
        sp_VillageCode.setAdapter(adapter);
        sp_VillageCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String districtCode = villagesArrayList.get(i).getDistrictCode();
                String mandalCode = villagesArrayList.get(i).getMandalCode();
                String panchayatCode = String.valueOf(Constants.PANCHAYATID);
                String villageCode = villagesArrayList.get(i).getVillageCode();
                districtCode = (districtCode.length() > 1 ? districtCode : "0" + districtCode);
                mandalCode = mandalCode.length() > 1 ? mandalCode : "0" + mandalCode;
                villageCode = villageCode.length() > 1 ? villageCode : "0" + villageCode;
                panchayatCode = panchayatCode.length() > 1 ? panchayatCode : "0" + panchayatCode;
                String[] value = dalWorksiteDetails.getVillageWiseTotalRecords(villagesArrayList.get(i).getVillageCode());
                et_WorkCode.setText(districtCode + assemblyCode + mandalCode + panchayatCode + villageCode);
                tv_VillageTotal.setText(value[0]);
                tv_VillageCompleted.setText(value[1]);
                workCodesArralist = dalWorksiteDetails.getworkCodes(villagesArrayList.get(i).getVillageCode(), String.valueOf(Constants.PANCHAYATID));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (DoortoDoorF5Ativity.this, android.R.layout.select_dialog_item, workCodesArralist);
                actv_WorkId.setThreshold(1);
                actv_WorkId.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void checkData() {
        try {
            //opening the database

            String workCardIdstr = et_WorkCode.getText().toString() + "" + actv_WorkId.getText().toString();
            //we used rawQuery(sql, selectionargs) for fetching all the employees
            Cursor cursorEmployees = DBManager.getInstance().getRawQuery("SELECT * FROM worksite WHERE work_code IN ('" + workCardIdstr + "')");
            //if the cursor has some data
            if (cursorEmployees.moveToFirst()) {
                //looping through all the records
                do {
                    checkList.add(cursorEmployees.getString(1));
                } while (cursorEmployees.moveToNext());
            }
            //closing the cursor
            cursorEmployees.close();
            System.out.println("-----------checking employees with work code array size: " + checkList.size());
            if (checkList.size() == 0) {
                Toast.makeText(DoortoDoorF5Ativity.this, "Please enter valid Work code", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(DoortoDoorF5Ativity.this, Format5ARow.class);
                intent.putExtra("work_code", workCardIdstr);
                startActivity(intent);
                DoortoDoorF5Ativity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }catch (SQLException error){
            System.out.println("error: "+error.toString());
        }
    }

    public void checkDataForm5aTable(){
        Cursor cursorFormat5A = DBManager.getInstance().getRawQuery("SELECT * FROM format5A");
        //if the cursor has some data

        if (cursorFormat5A.moveToFirst()) {
            //looping through all the records
            do {
                checkList2.add(cursorFormat5A.getString(19));
                System.out.println("date: "+cursorFormat5A.getString(19));
            } while (cursorFormat5A.moveToNext());
        }
        //closing the cursor
        cursorFormat5A.close();
        System.out.println("Format 5A Size: "+checkList2.size());
    }


}
