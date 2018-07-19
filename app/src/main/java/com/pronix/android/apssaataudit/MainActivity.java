package com.pronix.android.apssaataudit;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pronix.android.apssaataudit.Dal.DalDoorToDoorDetails;
import com.pronix.android.apssaataudit.Dal.DalWorksiteDetails;
import com.pronix.android.apssaataudit.common.Constants;
import com.pronix.android.apssaataudit.common.Utils;
import com.pronix.android.apssaataudit.db.DBManager;
import com.pronix.android.apssaataudit.models.DistrictDO;
import com.pronix.android.apssaataudit.models.MandalDO;
import com.pronix.android.apssaataudit.models.PanchayatDO;
import com.pronix.android.apssaataudit.models.WebServiceDO;
import com.pronix.android.apssaataudit.render.CommonSpinnerAdapter;
import com.pronix.android.apssaataudit.services.AsyncTask;
import com.pronix.android.apssaataudit.services.OnTaskCompleted;
import com.pronix.android.apssaataudit.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


import au.com.bytecode.opencsv.CSVReader;
import dmax.dialog.SpotsDialog;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends BaseActivity implements OnTaskCompleted {
    private TextView total_wage_seekers, total_areas_worked, total_worksite_sync_records, total_jobseeker_sync_records, total_jobseeker_unsync_records, total_worksite_unsync_records;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int READ_REQUEST_CODE = 42;
    private ProgressDialog progressDialog;
    Dialog dialog;
    SessionManager sessionManager;
    String selectedauditType = "";
    private int countOfFormat4A, countFormat4AResults;
    SpotsDialog spotDialog;
    private int countOfFormat5A;
    private Intent intent;
    private List<String[]> listdata = new ArrayList();
    private int countFormat5AResults;
    DalDoorToDoorDetails dalDoorToDoorDetails;
    DalWorksiteDetails dalWorksiteDetails;
    private Button start_audit, upload_csv;
    ArrayList<DistrictDO> districtList = new ArrayList<>();
    ArrayList<MandalDO> mandalList = new ArrayList<>();
    ArrayList<PanchayatDO> panchayatList = new ArrayList<>();
    int districCode, mandalCode, panchayatCode;
    String panchayatName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, frame_base);
        spotDialog = new SpotsDialog(this, "Uploading data, Please wait..", R.style.Custom);
        spotDialog.setCancelable(false);
        dalDoorToDoorDetails = new DalDoorToDoorDetails();
        dalWorksiteDetails = new DalWorksiteDetails();
        sessionManager = new SessionManager(this);
        upload_csv = (Button) findViewById(R.id.upload_csv);
        start_audit = (Button) findViewById(R.id.start_audit);
        total_wage_seekers = (TextView) findViewById(R.id.total_wage_seekers);
        total_areas_worked = (TextView) findViewById(R.id.total_areas_worked);
        total_jobseeker_sync_records = (TextView) findViewById(R.id.total_jobseeker_sync_records);
        total_worksite_sync_records = (TextView) findViewById(R.id.total_worksite_sync_records);
        total_jobseeker_unsync_records = (TextView) findViewById(R.id.total_jobseeker_unsync_records);
        total_worksite_unsync_records = (TextView) findViewById(R.id.total_worksite_unsync_records);

        try {
        } catch (SQLException error) {
            System.out.println(error.toString());
        }
        progressDialog = new ProgressDialog(MainActivity.this);
        if (!checkPermission()) {
            requestPermission();
        }

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            System.out.println("not read available");
        }
        upload_csv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.isNetworkAvailable(MainActivity.this))
                    calPanchayatWebservice();
                else
                    Utils.showalert(MainActivity.this, "Alert", "Please verify internet connection");
            }
        });

        start_audit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDashboard();
                //we used rawQuery(sql, selectionargs) for fetching all the employees
                //cursorFormat5A = mDatabase.rawQuery("SELECT * FROM worksite", null);
                if (countOfFormat4A > 0) {
                    if (countOfFormat5A > 0) {
                        intent = new Intent(MainActivity.this, PanchayatListActivity.class);
                        intent.putExtra("size", listdata.size());
                        intent.putExtra("data", listdata.toString());
                        startActivity(intent);
                        MainActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        Toast.makeText(MainActivity.this, "Please download Work Site Audit Data", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please download Door To Door Audit Data", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void openSelectLocalityDialog() {
        dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_locality);
        Button uploadCSV = dialog.findViewById(R.id.uploadCSV);
//        final Spinner selectAuditType = dialog.findViewById(R.id.selectAuditType);
        final Spinner sp_District = dialog.findViewById(R.id.sp_District);
        final Spinner sp_Mandal = dialog.findViewById(R.id.sp_Mandal);
        final Spinner sp_Panchayat = dialog.findViewById(R.id.sp_Panchayat);
        CommonSpinnerAdapter districtsAdapter = new CommonSpinnerAdapter(MainActivity.this, districtList);
        sp_District.setAdapter(districtsAdapter);
        sp_District.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (i != -1) {
                        mandalList.clear();
                        districCode = districtList.get(i).getDistrictCode();

                        mandalList = (ArrayList<MandalDO>) districtList.get(i).getMandalList();
                        CommonSpinnerAdapter mandalsAdapter = new CommonSpinnerAdapter(MainActivity.this, mandalList);
                        sp_Mandal.setAdapter(mandalsAdapter);

                        sp_Mandal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                try {
                                    panchayatList.clear();
                                    mandalCode = mandalList.get(i).getMandalCode();
                                    panchayatList = (ArrayList<PanchayatDO>) mandalList.get(i).getPanchayatList();
                                    CommonSpinnerAdapter panchayatAdapter = new CommonSpinnerAdapter(MainActivity.this, panchayatList);
                                    sp_Panchayat.setAdapter(panchayatAdapter);
                                    sp_Panchayat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            panchayatCode = panchayatList.get(i).getPanchayatCode();
                                            panchayatName = panchayatList.get(i).getPanchayatName();
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                } catch (Exception e) {

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        uploadCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sp_District.getSelectedItemPosition() == -1) {
                    Utils.showalert(MainActivity.this, "Alert", "Please select District");
                } else if (sp_Mandal.getSelectedItemPosition() == -1) {
                    Utils.showalert(MainActivity.this, "Alert", "Please select Mandal");
                } else if (sp_Panchayat.getSelectedItemPosition() == -1) {
                    Utils.showalert(MainActivity.this, "Alert", "Please select Panchayat");
                } else
                    calWebservice(districCode, mandalCode, panchayatCode);

//                calWebservice("1", "28", "15");


//                dialog.dismiss();
            }
        });

        ImageView dialogButton = (ImageView) dialog.findViewById(R.id.closeImg);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            System.out.println("read only");
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            System.out.println("available");
            return true;
        }
        return false;
    }


    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i("CSVScreen", "Uri: " + uri.getPath());
                String[] ss = uri.getPath().split("\\/");
                //System.out.println("-------------0" + ss[0] + "-1" + ss[1] + "-2" + ss[2] + "-3" + ss[3] + "----length" + ss[ss.length - 1]);

                final File myExternalFile = new File(Environment.getExternalStorageDirectory() + "/Download/", ss[ss.length - 1]);


                String[] next = {};
                try {
                    FileInputStream fis = new FileInputStream(myExternalFile);
                    InputStreamReader csvStreamReader = new InputStreamReader(fis);

                    CSVReader reader = new CSVReader(csvStreamReader);
                    if (selectedauditType.equalsIgnoreCase("Door To Door")) {

                        for (; ; ) {
                            next = reader.readNext();
                            if (next[0].split("\\^").length < 39) {
                                progressDialog.hide();
                                Toast.makeText(this, "Wrong file Selected", Toast.LENGTH_LONG).show();
                                break;
                            } else {
                                readCSVdata(myExternalFile);
                            }
                            break;
                        }

                    } else if (selectedauditType.equalsIgnoreCase("Work Site")) {
                        for (; ; ) {
                            next = reader.readNext();
                            if (next[0].split("\\^").length < 33) {
                                progressDialog.hide();
                                Toast.makeText(this, "Wrong file Selected", Toast.LENGTH_LONG).show();
                                break;
                            } else {
                                readCSVdata(myExternalFile);
                            }
                            break;
                        }


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted) {
                        //Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private void readCSVdata(File file) {

        //List<String[]> list = new ArrayList<String[]>();
        String[] next = {};
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader csvStreamReader = new InputStreamReader(fis);

            CSVReader reader = new CSVReader(csvStreamReader);
            if (selectedauditType.equalsIgnoreCase("Door To Door")) {
                for (; ; ) {
                    next = reader.readNext();
                    if (next != null)
                        insertToDoorToDoor(next);
                    else
                        break;
                }
                progressDialog.hide();
                updateDashboard();
                Toast.makeText(this, "Door to Door Audit File Downloaded Sucesfully", Toast.LENGTH_LONG).show();
            } else if (selectedauditType.equalsIgnoreCase("Work Site")) {
                for (; ; ) {
                    next = reader.readNext();
                    if (next != null)
                        insertToWorkSite(next);
                    else
                        break;
                }
                progressDialog.hide();
                updateDashboard();
                Toast.makeText(this, "Work Site Audit File Downloaded Sucesfully", Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        //return list;
    }

    public void insertToDoorToDoor(String[] list) {
        //for (int i = 0; i < list.size(); i++) {
        //categoryList.add(list.get(i)[0]);
        String[] csvparts = list[0].split("\\^");
        dalDoorToDoorDetails.insertOrUpdateDoorToDoorData(csvparts);

    }

    public void insertToWorkSite(String[] list) {
        //for (int i = 0; i < list.size(); i++) {
        //categoryList.add(list.get(i)[0]);
        String[] csvparts = list[0].split("\\^", -1);
        System.out.println("Content=worksite=====" + list[0]);
        dalWorksiteDetails.insertOrUpdateWorksiteDetails(csvparts);

    }


    @Override
    protected void onResume() {
        sessionManager.checkLogin();
        super.onResume();
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            super.onBackPressed();

            finish();
        } else {
            Toast.makeText(this, getString(R.string.exit), Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }

            }, 3 * 1000);
        }
    }


    private void updateDashboard() {
        countOfFormat4A = stringToIn(DBManager.getInstance().getScalar("SELECT count(1) FROM employees"));
        total_wage_seekers.setText(String.valueOf(countOfFormat4A));

        countOfFormat5A = stringToIn(DBManager.getInstance().getScalar("SELECT count(1) FROM worksite"));
        total_areas_worked.setText(String.valueOf(countOfFormat5A));

        //we used rawQuery(sql, selectionargs) for fetching all the employeesRe
        countFormat4AResults = stringToIn(DBManager.getInstance().getScalar("SELECT count(1) FROM format4A WHERE IFNULL(serverflag,'0') = '1'"));
        total_jobseeker_sync_records.setText(String.valueOf(countFormat4AResults));

        //we used rawQuery(sql, selectionargs) for fetching all the employees
        countFormat5AResults = stringToIn(DBManager.getInstance().getScalar("SELECT count(1) FROM format5A WHERE IFNULL(serverflag,'0') = '1'"));
        total_worksite_sync_records.setText(String.valueOf(countFormat5AResults));

        total_jobseeker_unsync_records.setText(DBManager.getInstance().getScalar("SELECT count(1) FROM format4A WHERE IFNULL(serverflag,'0') = '0'"));
        total_worksite_unsync_records.setText(DBManager.getInstance().getScalar("SELECT count(1) FROM format5A WHERE IFNULL(serverflag,'0') = '0'"));

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateDashboard();
    }

    public int stringToIn(String result) {
        try {
            if (result == null || result.equals("")) {
                return 0;
            }
            return Integer.parseInt(result);
        } catch (Exception e) {
            e.getMessage();
        }
        return 0;
    }


    public void calWebservice(int district, int mandal, int panchayat) {
        try {
            spotDialog.show();
            WebServiceDO webServiceDO = new WebServiceDO();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("districtCode", district);
            jsonObject.put("mandalCode", mandal);
            jsonObject.put("panchayatCode", panchayat);
            webServiceDO.result = Constants.SENT;
            webServiceDO.request = "AUDITDATA";
            new AsyncTask(MainActivity.this, MainActivity.this, Constants.URLBase + "" + "auditData", "POST", jsonObject.toString()).execute(webServiceDO);
        } catch (Exception e) {
            e.getMessage();
            Utils.hideProgress(spotDialog);
        }

    }

    public void calPanchayatWebservice() {
        try {
            spotDialog.show();
            WebServiceDO webServiceDO = new WebServiceDO();
            JSONObject jsonObject = new JSONObject();
            webServiceDO.result = Constants.SENT;
            webServiceDO.request = "PANCHAYATLIST";
            new AsyncTask(MainActivity.this, MainActivity.this, Constants.URLBase + "" + "panchayatList", "GET", jsonObject.toString()).execute(webServiceDO);
        } catch (Exception e) {
            e.getMessage();
            Utils.hideProgress(spotDialog);
        }

    }

    @Override
    public void onTaskCompletes(WebServiceDO webServiceDO) {
        try {
            if (webServiceDO.result.equals(Constants.SUCCESS)) {
                if (webServiceDO.request.equals("AUDITDATA")) {
                    Utils.hideProgress(spotDialog);
                    if (dialog.isShowing())
                        dialog.dismiss();

                    new insertRecordFromServer(webServiceDO.responseContent).execute("");


                } else if (webServiceDO.request.equals("PANCHAYATLIST")) {

                    Type listType = new TypeToken<List<DistrictDO>>() {
                    }.getType();
                    districtList = new Gson().fromJson(webServiceDO.responseContent, listType);
                    openSelectLocalityDialog();
                    Utils.hideProgress(spotDialog);

                }
            } else {
                Utils.showalert(this, "Alert", webServiceDO.responseContent);
                Utils.hideProgress(spotDialog);
            }
        } catch (Exception e) {
            e.getMessage();
            Utils.hideProgress(spotDialog);
        }
    }

    class insertRecordFromServer extends android.os.AsyncTask<String, String, String>
    {
        String result;

        public insertRecordFromServer(String result)
        {
            this.result = result;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Fetching data, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... webServiceDOS) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("format4ADataList"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    dalDoorToDoorDetails.insertOrUpdateDoorToDoorDetails(String.valueOf(districCode), String.valueOf(mandalCode), String.valueOf(panchayatCode), json.getString("villageCode"), json.getString("habitationCode"), "",
                            json.getString("householdCode"), json.getString("workerCode"), json.getString("surname"), "",
                            json.getString("name"), "", json.getString("accountNo"), json.getString("workCode"),
                            json.getString("workName"), "", json.getString("workLocation"), "",
                            "", json.getString("fromDate"), json.getString("toDate"), json.getString("daysWorked"),
                            json.getString("amountPaid"), json.getString("paymentDate"), "",
                            "", "", "", "", "", "", "", "", "", "", "",
                            "", "", json.getString("musterId"), panchayatName, json.getString("villageName"), json.getString("habitationName"));
                    publishProgress("Please wait..loading doortodoor details(" + String.valueOf(i) + "/"+String.valueOf(jsonArray.length())+")");
                }
                jsonArray = new JSONArray(jsonObject.getString("format5ADataList"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    dalWorksiteDetails.insertOrUpdateWorksiteData(String.valueOf(districCode), String.valueOf(mandalCode), String.valueOf(panchayatCode), json.getString("villageCode"), json.getString("habitationCode"), "",
                            json.getString("workCode"), json.getString("workName"), "", json.getString("workLocation"), "",
                            json.getString("taskCode"), json.getString("taskName"), json.getString("skillType"), json.getString("qtySanc"),
                            json.getString("amountSanc"), json.getString("qtyDone"), json.getString("amountSpent"), "", "", "", "", "", "",
                            "", "", "", "", "", "", "", "", "", panchayatName,json.getString("villageName"), json.getString("habitationName"));
                    publishProgress("Please wait..loading worksite details(" + String.valueOf(i) + "/"+String.valueOf(jsonArray.length())+")");
                }
            }
            catch (Exception e)
            {
                e.getMessage();

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            progressDialog.setMessage(values[0]);
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            Utils.hideProgress(progressDialog);
            updateDashboard();
        }
    }
}
