package com.pronix.android.apssaataudit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import com.pronix.android.apssaataudit.Dal.DalDoorToDoorDetails;
import com.pronix.android.apssaataudit.Dal.DalDoorToDoorResults;
import com.pronix.android.apssaataudit.common.Constants;
import com.pronix.android.apssaataudit.common.Utils;
import com.pronix.android.apssaataudit.models.Worker;
import com.pronix.android.apssaataudit.render.WorkersAdapter4A;
import com.pronix.android.apssaataudit.session.SessionManager;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Format4ARow extends AppCompatActivity implements View.OnClickListener {
    SQLiteDatabase mDatabase;
    private ArrayList<Worker> checkList = new ArrayList<Worker>();
    Bundle bundle;
    String householdCode = "";
    RecyclerView rcyVw_workers;
    WorkersAdapter4A mAdapter;
    Context mCon = this;
    List<List<String>> format4aFamilyList;
    List<String> format4aIndividualList;
    private Button btn_save_format4a;
    SessionManager sessionManager;

    TextView serialNo, wageSeekerId, fullname, postOfficeBankAccDetails, allWorkDetails, workDuration,
            payOrderReleaseDate, musterId, noOfWorkingDays, amtToBePaid;
    EditText actualWorkedDays, actualAmtPaid, diffInAmtPaid, respPersonName, respPersonDesig, comments;
    Spinner isJobCardAvailSpinner, isPassbookAvailSpinner, isPaylipIssuedSpinner, mainCategorySpinner, subCategorySpinner1, subCategorySpinner2;
    private DalDoorToDoorResults dalDoorToDoorResults;
    DalDoorToDoorDetails dalDoorToDoorDetails;

    TextView tv_SNo, tv_JobSeekerId, tv_FullName, tv_POOrBA, tv_WorkDetails, tv_WorkDuration, tv_PayOrderRelease, tv_MusterId, tv_NoOfWorkingDays,
            tv_AmountToBePaid, tv_ActualNofOfDaysWorked, tv_ActualPaidAmt, tv_DiffInAmtPaid, tv_IsJobCardAvail, tv_IsPassBookAvail, tv_PayslipsIssuing,
            tv_RespPersonName, tv_RespPersonDesg, tv_Category1, tv_Category2, tv_Category3, tv_Comments;
    Button but_Submit;

    LinearLayout ll_List;
    String subCategState = "";
    ImageView iv_Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_format4a);
        dalDoorToDoorResults = new DalDoorToDoorResults();
        dalDoorToDoorDetails = new DalDoorToDoorDetails();
//        initializeControls();
        initializeXML();
//        setTextTitls();

        bundle = getIntent().getExtras();
        format4aFamilyList = new ArrayList<>();
        sessionManager = new SessionManager(mCon);
        if (bundle != null) {
            householdCode = bundle.getString("householdCode");
            System.out.println("householdCode" + householdCode);
        }


//        mAdapter = new WorkersAdapter4A(mCon, checkList);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        rcyVw_workers.setLayoutManager(mLayoutManager);
//        rcyVw_workers.setNestedScrollingEnabled(false);
//        rcyVw_workers.setAdapter(mAdapter);

        //To check and load if any data is present for selected household code
        checkData();


//        btn_save_format4a.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (checkValidations()){
//                    storeData();
//                }
//            }
//        });


    }

    public void initializeXML() {
        ll_List = (LinearLayout) findViewById(R.id.ll_Form4aList);
        but_Submit = (Button) findViewById(R.id.but_Submit);
        but_Submit.setOnClickListener(this);

    }

    private void checkData() {
        //opening the database

        //we used rawQuery(sql, selectionargs) for fetching all the employees
        checkList = dalDoorToDoorDetails.getDoorToDoorDetails(householdCode);

        System.out.println("---Selected household array size: " + checkList.size());
        //tvMainSelectedCate.setText("First column(district codes): "+checkList.toString());
//        loadListView(checkList);
        loadData();
    }

    public void loadData() {
        ll_List.removeAllViews();
        TextView tv_WageSeekerId, tv_FullName, tv_WorkName;
        Button bt_view;
        for (int i = 0; i < checkList.size(); i++) {
            View v = LayoutInflater.from(this)
                    .inflate(R.layout.activity_4arowitem, null);
            tv_WageSeekerId = (TextView) v.findViewById(R.id.tv_WageSeekerId);
            tv_FullName = (TextView) v.findViewById(R.id.tv_FullName);
            tv_WorkName = (TextView) v.findViewById(R.id.tv_WorkName);
            bt_view = (Button) v.findViewById(R.id.but_View);
            Worker obj = checkList.get(i);
            tv_WageSeekerId.setText(obj.getHousehold_code() + " / " + obj.getWorker_code());
            tv_FullName.setText(obj.getName());
            tv_WorkName.setText(obj.getWork_name());

            if (obj.getServerFlag().equals("0") || obj.getServerFlag().equals("1")) {
//                bt_view.setBackgroundColor(getResources().getColor(R.color.grey));
                if(!Constants.userMasterDO.designation.toUpperCase().equals("STM")) {
                    bt_view.setTextColor(getResources().getColor(R.color.colorPrimary));
                    bt_view.setVisibility(View.INVISIBLE);
                }
                else
                    bt_view.setOnClickListener(new DetailsClickListener(obj));
            } else
                bt_view.setOnClickListener(new DetailsClickListener(obj));
            ll_List.addView(v);

        }
    }

    private void loadListView(ArrayList<Worker> checkList) {
        if (checkList.size() > 0) {
            mAdapter = new WorkersAdapter4A(mCon, checkList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            rcyVw_workers.setLayoutManager(mLayoutManager);
            rcyVw_workers.setAdapter(mAdapter);
        } else {
            Toast.makeText(Format4ARow.this, "No data found for this work card ID", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkValidations() {
        boolean flag = false;
        for (int i = 0; i < checkList.size(); i++) {
            View holder = rcyVw_workers.getChildAt(i);
            flag = false;
            actualWorkedDays = holder.findViewById(R.id.actualWorkedDays);
            actualAmtPaid = holder.findViewById(R.id.actualAmtPaid);
            diffInAmtPaid = holder.findViewById(R.id.diffInAmtPaid);
            isJobCardAvailSpinner = holder.findViewById(R.id.isJobCardAvailSpinner);
            isPassbookAvailSpinner = holder.findViewById(R.id.isPassbookAvailSpinner);
            isPaylipIssuedSpinner = holder.findViewById(R.id.isPaylipIssuedSpinner);
            respPersonName = holder.findViewById(R.id.respPersonName);
            respPersonDesig = holder.findViewById(R.id.respPersonDesig);
            mainCategorySpinner = holder.findViewById(R.id.mainCategorySpinner);
            subCategorySpinner1 = holder.findViewById(R.id.subCategorySpinner1);
            subCategorySpinner2 = holder.findViewById(R.id.subCategorySpinner2);
            comments = holder.findViewById(R.id.comments);
            if (actualWorkedDays.getText().toString().trim().equals("")) {
                //Toast.makeText(mCon, "Actual Work Days should not be empty", Toast.LENGTH_SHORT).show();
//                actualWorkedDays.setError("Actual Work Days should not be empty");
                actualWorkedDays.requestFocus();
                actualWorkedDays.setBackgroundResource(R.drawable.table_edittext_alert_border);
                showalert(this, "Alert", "Actual Work Days should not be empty for " + checkList.get(i).getName());
                break;
            } else {
                if (actualAmtPaid.getText().toString().trim().equals("")) {
                    //Toast.makeText(mCon, "Actual Amount Paid should not be empty", Toast.LENGTH_SHORT).show();
//                    actualAmtPaid.setError("Actual Work Days should not be empty");
                    actualAmtPaid.requestFocus();
                    actualAmtPaid.setBackgroundResource(R.drawable.table_edittext_alert_border);
                    showalert(this, "Alert", "Actual amount paid should not be empty for " + checkList.get(i).getName());
                    break;
                } else {
                    if (diffInAmtPaid.getText().toString().trim().equals("")) {
                        //Toast.makeText(mCon, "Diiference in amount paid should not be empty if not keep 0", Toast.LENGTH_SHORT).show();
//                        diffInAmtPaid.setError("Actual Work Days should not be empty");
                        diffInAmtPaid.requestFocus();
                        diffInAmtPaid.setBackgroundResource(R.drawable.table_edittext_alert_border);
                        showalert(this, "Alert", "Difference amount paid should not be empty for " + checkList.get(i).getName());
                        break;
                    } else {
                        /*actualWorkedDays.setBackgroundResource(R.drawable.table_edittext_border);
                        actualAmtPaid.setBackgroundResource(R.drawable.table_edittext_border);
                        diffInAmtPaid.setBackgroundResource(R.drawable.table_edittext_border);*/
                        flag = true;
                    }
                }
            }
        }
        return flag;

    }

    public boolean validations(String actualWorkedDays, String actualAmtpaid, String diffInAmt, String name) {
        boolean flag = false;
        if (actualWorkedDays.trim().equals("")) {
            Utils.showalert(this, "Alert", "Actual Work Days should not be empty for " + name);
        } else {
            if (actualAmtpaid.trim().equals("")) {
                Utils.showalert(this, "Alert", "Actual amount paid should not be empty for " + name);

            } else {
                if (diffInAmt.trim().equals("")) {
                    Utils.showalert(this, "Alert", "Difference amount paid should not be empty for " + name);

                } else {
                    flag = true;
                }
            }
        }

        return flag;

    }

    private void storeData() {
        try {
            long result = -1;
            for (int i = 0; i < checkList.size(); i++) {
                View holder = rcyVw_workers.getChildAt(i);
                serialNo = holder.findViewById(R.id.serialNo);
                wageSeekerId = holder.findViewById(R.id.wageSeekerId);
                fullname = holder.findViewById(R.id.fullname);
                postOfficeBankAccDetails = holder.findViewById(R.id.postOfficeBankAccDetails);
                allWorkDetails = holder.findViewById(R.id.allWorkDetails);
                workDuration = holder.findViewById(R.id.workDuration);
                payOrderReleaseDate = holder.findViewById(R.id.payOrderReleaseDate);
                musterId = holder.findViewById(R.id.musterId);
                noOfWorkingDays = holder.findViewById(R.id.noOfWorkingDays);
                amtToBePaid = holder.findViewById(R.id.amtToBePaid);
                actualWorkedDays = holder.findViewById(R.id.actualWorkedDays);
                actualAmtPaid = holder.findViewById(R.id.actualAmtPaid);
                diffInAmtPaid = holder.findViewById(R.id.diffInAmtPaid);
                isJobCardAvailSpinner = holder.findViewById(R.id.isJobCardAvailSpinner);
                isPassbookAvailSpinner = holder.findViewById(R.id.isPassbookAvailSpinner);
                isPaylipIssuedSpinner = holder.findViewById(R.id.isPaylipIssuedSpinner);
                respPersonName = holder.findViewById(R.id.respPersonName);
                respPersonDesig = holder.findViewById(R.id.respPersonDesig);
                mainCategorySpinner = holder.findViewById(R.id.mainCategorySpinner);
                subCategorySpinner1 = holder.findViewById(R.id.subCategorySpinner1);
                subCategorySpinner2 = holder.findViewById(R.id.subCategorySpinner2);
                //updateWorkerDetails = holder.findViewById(R.id.updateWorkerDetails);
                comments = holder.findViewById(R.id.comments);

                format4aIndividualList = new ArrayList<>();

                String created_time = "";
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Calendar cal = Calendar.getInstance();
                System.out.println("created timestamp" + dateFormat.format(cal.getTime()));
                created_time = dateFormat.format(cal.getTime());


                String[] jobseeker = wageSeekerId.getText().toString().trim().split("\\/");


                result = dalDoorToDoorResults.insertOrUpdateDoorToDoorResultData(mDatabase,
                        serialNo.getText().toString().trim(),
                        jobseeker[0],
                        jobseeker[1],
                        fullname.getText().toString().trim(),
                        postOfficeBankAccDetails.getText().toString().trim(),
                        allWorkDetails.getText().toString().trim(),
                        workDuration.getText().toString().trim(),
                        payOrderReleaseDate.getText().toString().trim(),
                        musterId.getText().toString().trim(),
                        noOfWorkingDays.getText().toString().trim(),
                        amtToBePaid.getText().toString().trim(),
                        actualWorkedDays.getText().toString().trim(),
                        actualAmtPaid.getText().toString().trim(),
                        diffInAmtPaid.getText().toString().trim(),
                        isJobCardAvailSpinner.getSelectedItem().toString().trim(),
                        isPassbookAvailSpinner.getSelectedItem().toString().trim(),
                        isPaylipIssuedSpinner.getSelectedItem().toString().trim(),
                        respPersonName.getText().toString().trim(),
                        respPersonDesig.getText().toString().trim(),
                        mainCategorySpinner.getSelectedItem().toString().trim(),
                        subCategorySpinner1.getSelectedItem().toString().trim(),
                        subCategorySpinner2.getSelectedItem().toString().trim(),
                        created_time,
                        comments.getText().toString().trim(),
                        Constants.userMasterDO.userName, (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())).format(new Date()),
                        Constants.userMasterDO.userName, "", checkList.get(i).getDistrict_code(), checkList.get(i).getMandal_code(),
                        checkList.get(i).getPanchayat_code());

                if (result == 0 || result == -1) {
                    break;
                }
            }
            if (result != 0 || result != -1) {
                Toast.makeText(this, "Details saved successfully", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Failed to save details", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void showalert(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        builder.show();
    }

    public void initializeControls() {

        tv_SNo = (TextView) findViewById(R.id.tv_SNo);
        tv_JobSeekerId = (TextView) findViewById(R.id.tv_JobSeekerId);
        tv_FullName = (TextView) findViewById(R.id.tv_FullName);
        tv_POOrBA = (TextView) findViewById(R.id.tv_POOrBA);
        tv_WorkDetails = (TextView) findViewById(R.id.tv_WorkDetails);
        tv_WorkDuration = (TextView) findViewById(R.id.tv_WorkDuration);
        tv_PayOrderRelease = (TextView) findViewById(R.id.tv_PayOrderRelease);
        tv_MusterId = (TextView) findViewById(R.id.tv_MusterId);
        tv_NoOfWorkingDays = (TextView) findViewById(R.id.tv_NoOfWorkingDays);
        tv_AmountToBePaid = (TextView) findViewById(R.id.tv_AmountToBePaid);
        tv_ActualNofOfDaysWorked = (TextView) findViewById(R.id.tv_ActualNofOfDaysWorked);
        tv_ActualPaidAmt = (TextView) findViewById(R.id.tv_ActualPaidAmt);
        tv_DiffInAmtPaid = (TextView) findViewById(R.id.tv_DiffInAmtPaid);
        tv_IsJobCardAvail = (TextView) findViewById(R.id.tv_IsJobCardAvail);
        tv_IsPassBookAvail = (TextView) findViewById(R.id.tv_IsPassBookAvail);
        tv_PayslipsIssuing = (TextView) findViewById(R.id.tv_PayslipsIssuing);
        tv_RespPersonName = (TextView) findViewById(R.id.tv_RespPersonName);
        tv_RespPersonDesg = (TextView) findViewById(R.id.tv_RespPersonDesg);
        tv_Category1 = (TextView) findViewById(R.id.tv_Category1);
        tv_Category2 = (TextView) findViewById(R.id.tv_Category2);
        tv_Category3 = (TextView) findViewById(R.id.tv_Category3);
        tv_Comments = (TextView) findViewById(R.id.tv_Comments);
        btn_save_format4a = (Button) findViewById(R.id.btn_save_format4a);
        rcyVw_workers = (RecyclerView) findViewById(R.id.rcyVw_workers);
    }

    public void setTextTitls() {
        tv_SNo.setText(R.string.sNo);
        tv_JobSeekerId.setText(R.string.jobSeekerId);
        tv_FullName.setText(R.string.fullName);
        tv_POOrBA.setText(R.string.POorBAD);
        tv_WorkDetails.setText(R.string.WorkDetails);
        tv_WorkDuration.setText(R.string.workDuration);
        tv_PayOrderRelease.setText(R.string.payOrderReleaseDate);
        tv_MusterId.setText(R.string.musterId);
        tv_NoOfWorkingDays.setText(R.string.noOfWorkingDays);
        tv_AmountToBePaid.setText(R.string.AmtToBePaid);
        tv_ActualNofOfDaysWorked.setText(R.string.actualNoOfdaysWorked);
        tv_ActualPaidAmt.setText(R.string.actualPaidAmt);
        tv_DiffInAmtPaid.setText(R.string.diffInAmtPaid);
        tv_IsJobCardAvail.setText(R.string.isJobCardIdAvai);
        tv_IsPassBookAvail.setText(R.string.isPassbookisAvailable);
        tv_PayslipsIssuing.setText(R.string.paySlipsIssuing);
        tv_RespPersonName.setText(R.string.resPersonName);
        tv_RespPersonDesg.setText(R.string.resPersonJobDesg);
        tv_Category1.setText(R.string.category1);
        tv_Category2.setText(R.string.category2);
        tv_Category3.setText(R.string.category3);
        tv_Comments.setText(R.string.comments);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_Submit:
                int count = 0;
                for (int i = 0; i < checkList.size(); i++) {
                    View v = ll_List.getChildAt(i);
                    Button but_View = (Button) v.findViewById(R.id.but_View);
                    if (but_View.getVisibility() == View.VISIBLE) {
                        count = 1;
                        break;
                    }
                }
                if (count == 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Format4ARow.this);
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Details Submited successfully");
                    alertDialog.setIcon(R.mipmap.ic_launcher);
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            finish();
                        }
                    });

                    alertDialog.show();
                }
                else
                {
                    Utils.showalert(this, "Alert", "Please save all the details");
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        int count = 0;
        for (int i = 0; i < checkList.size(); i++) {
            View v = ll_List.getChildAt(i);
            Button but_View = (Button) v.findViewById(R.id.but_View);
            if (but_View.getVisibility() == View.VISIBLE) {
                count += 1;
            }
        }
        if(checkList.size() == count || count == 0)
        {
            finish();
        }
        else
            Utils.showalert(this, "Alert", "Please save all the details");
    }

    public class DetailsClickListener implements View.OnClickListener {
        Worker obj;

        public DetailsClickListener(Worker worker) {
            obj = worker;
        }


        @Override
        public void onClick(View view) {
            showForm4aDetailsDialog(obj);
        }
    }

    public void showForm4aDetailsDialog(final Worker workerDetails) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.form4a_details, null);
        final TextView tv_FullName = (TextView) alertLayout.findViewById(R.id.tv_FullName);
        final TextView tv_JobSeekerId = (TextView) alertLayout.findViewById(R.id.tv_JobSeekerId);
        final TextView tv_POAndBank = (TextView) alertLayout.findViewById(R.id.tv_PostOfc);
        final TextView tv_WorkDetails = (TextView) alertLayout.findViewById(R.id.tv_Workdetails);
        final TextView tv_workDuration = (TextView) alertLayout.findViewById(R.id.tv_WorkDuration);
        final TextView tv_PayOrderRlease = (TextView) alertLayout.findViewById(R.id.tv_PayOrderRelease);
        final TextView tv_MusterId = (TextView) alertLayout.findViewById(R.id.tv_MusterId);
        final TextView tv_NoofWorkingDays = (TextView) alertLayout.findViewById(R.id.tv_NoOfWorkingDays);
        final TextView tv_AmtTobePaid = (TextView) alertLayout.findViewById(R.id.tv_AmountToBePaid);
        final EditText et_ActualNoOfWorkedDays = (EditText) alertLayout.findViewById(R.id.et_ActualNofOfDaysWorked);
        final EditText et_ActualPaidAmount = (EditText) alertLayout.findViewById(R.id.et_ActualPaidAmt);
        final EditText et_DiffInAmt = (EditText) alertLayout.findViewById(R.id.et_DiffInAmtPaid);
        final Spinner sp_IsJobCard = (Spinner) alertLayout.findViewById(R.id.sp_IsJobcardAvail);
        final Spinner sp_IsPassbook = (Spinner) alertLayout.findViewById(R.id.sp_IsPassbookAvailable);
        final Spinner sp_PayslipsIssuing = (Spinner) alertLayout.findViewById(R.id.sp_PayslipsIssuing);
        final EditText et_RepPerson = (EditText) alertLayout.findViewById(R.id.et_RespPersonName);
        final EditText et_ResPersonDesg = (EditText) alertLayout.findViewById(R.id.et_RespPersonDesg);
        final Spinner sp_Category1 = (Spinner) alertLayout.findViewById(R.id.sp_Category1);
        final Spinner sp_Category2 = (Spinner) alertLayout.findViewById(R.id.sp_Category2);
        final Spinner sp_Category3 = (Spinner) alertLayout.findViewById(R.id.sp_Category3);
        final EditText et_Comments = (EditText) alertLayout.findViewById(R.id.et_Comments);
        Button but_Save = (Button) alertLayout.findViewById(R.id.but_Save);
        Button but_Capture = (Button) alertLayout.findViewById(R.id.but_Capture);
        iv_Image = (ImageView) alertLayout.findViewById(R.id.iv_Img);


        tv_FullName.setText("Name: " + workerDetails.getName());
        tv_JobSeekerId.setText(workerDetails.getHousehold_code() + " / " + workerDetails.getWorker_code());
        tv_POAndBank.setText(workerDetails.getAccount_no());
        tv_WorkDetails.setText(workerDetails.getWork_code() + " / " + workerDetails.getWork_name() + " / " + workerDetails.getWork_location());
        tv_workDuration.setText(workerDetails.getFrom_date() + " to " + workerDetails.getTo_date());
        tv_PayOrderRlease.setText(workerDetails.getPayment_date());
        tv_MusterId.setText(workerDetails.getMuster_id());
        tv_NoofWorkingDays.setText(workerDetails.getDays_worked());
        tv_AmtTobePaid.setText(workerDetails.getAmount_paid());
        et_ActualNoOfWorkedDays.setText(workerDetails.getActualworkeddays());
        et_ActualPaidAmount.setText(workerDetails.getActualamtpaid());
        et_DiffInAmt.setText(workerDetails.getDifferenceinamount());
        et_RepPerson.setText(workerDetails.getResppersonname());
        et_ResPersonDesg.setText(workerDetails.getResppesonjobdesg());
        sp_IsJobCard.setSelection(workerDetails.getIsjobcardavail().toUpperCase().equals("NO") ? 1 : 0, false);
        sp_IsPassbook.setSelection(workerDetails.getIspassbookavail().toUpperCase().equals("NO") ? 1 : 0, false);
        sp_PayslipsIssuing.setSelection(workerDetails.getIspayslipissued().toUpperCase().equals("NO") ? 1 : 0, false);
        et_Comments.setText(workerDetails.getComments());

        String[] maincategories = mCon.getResources().getStringArray(R.array.maincategories);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mCon, android.R.layout.simple_list_item_1, maincategories);
        sp_Category1.setAdapter(adapter);

        sp_Category1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //loadsecondspinner
                String[] subcategories1;
                if (i == 1) {
                    subCategState = "Category1";
                    subcategories1 = mCon.getResources().getStringArray(R.array.Category1);
                } else if (i == 2) {
                    subCategState = "Category2";
                    subcategories1 = mCon.getResources().getStringArray(R.array.Category2);
                } else if (i == 3) {
                    subCategState = "Category3";
                    subcategories1 = mCon.getResources().getStringArray(R.array.Category3);
                } else {
                    subCategState = "Category4";
                    subcategories1 = mCon.getResources().getStringArray(R.array.Category4);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mCon, android.R.layout.simple_list_item_1, subcategories1);
                sp_Category2.setAdapter(adapter);
//                sp_Category2.setSelection(getCatefgory2position(worker.getCategory2(), subCategState), false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Category2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //loadsecondspinner
                String[] subcategories2 = new String[0];
                subcategories2 = mCon.getResources().getStringArray(R.array.Noissue);
                if (subCategState.equalsIgnoreCase("Category1")) {
                    if (i == 1) {
                        subcategories2 = mCon.getResources().getStringArray(R.array.IssueCategory11);
                    } else if (i == 2) {
                        subcategories2 = mCon.getResources().getStringArray(R.array.IssueCategory12);
                    } else if (i == 3) {
                        subcategories2 = mCon.getResources().getStringArray(R.array.IssueCategory13);
                    }
                } else if (subCategState.equalsIgnoreCase("Category2")) {
                    if (i == 1) {
                        subcategories2 = mCon.getResources().getStringArray(R.array.procedural1);
                    } else if (i == 2) {
                        subcategories2 = mCon.getResources().getStringArray(R.array.procedural2);
                    } else if (i == 3) {
                        subcategories2 = mCon.getResources().getStringArray(R.array.procedural3);
                    } else if (i == 4) {
                        subcategories2 = mCon.getResources().getStringArray(R.array.procedural4);
                    }
                } else if (subCategState.equalsIgnoreCase("Category3")) {
                    if (i == 1) {
                        subcategories2 = mCon.getResources().getStringArray(R.array.information1);
                    }
                } else if (subCategState.equalsIgnoreCase("Category4")) {
                    if (i == 1) {
                        subcategories2 = mCon.getResources().getStringArray(R.array.grievances1);
                    } else if (i == 2) {
                        subcategories2 = mCon.getResources().getStringArray(R.array.grievances2);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mCon, android.R.layout.simple_list_item_1, subcategories2);
                sp_Category3.setAdapter(adapter);
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        AlertDialog.Builder alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        alert.setView(alertLayout);
        final AlertDialog dialog = alert.create();
        but_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validations(et_ActualNoOfWorkedDays.getText().toString(), et_ActualPaidAmount.getText().toString(),
                        et_DiffInAmt.getText().toString(), tv_FullName.getText().toString()) == true) {
                    saveData(tv_JobSeekerId.getText().toString(), tv_FullName.getText().toString(), tv_POAndBank.getText().toString(), tv_WorkDetails.getText().toString(),
                            tv_workDuration.getText().toString(), tv_PayOrderRlease.getText().toString(), tv_MusterId.getText().toString(), tv_NoofWorkingDays.getText().toString(),
                            tv_AmtTobePaid.getText().toString(), et_ActualNoOfWorkedDays.getText().toString(), et_ActualPaidAmount.getText().toString(),
                            et_DiffInAmt.getText().toString(), sp_IsJobCard.getSelectedItem().toString(), sp_IsPassbook.getSelectedItem().toString(),
                            sp_PayslipsIssuing.getSelectedItem().toString(), et_RepPerson.getText().toString(), et_ResPersonDesg.getText().toString(),
                            sp_Category1.getSelectedItem().toString(), sp_Category2.getSelectedItem().toString(), sp_Category3.getSelectedItem().toString(),
                            et_Comments.getText().toString(), workerDetails, dialog);
                }
            }
        });
        but_Capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(str_Path.equals("")) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri photoURI = FileProvider.getUriForFile(Format4ARow.this,
                                BuildConfig.APPLICATION_ID + ".provider",
                                createImageFile());
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(cameraIntent, 101);
                    }
                    else
                    {
//                        uploadFile();
                        new uploadImage().execute("");
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        });
        dialog.show();
    }

    String photoPath = "";
    String str_Path = "";

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Images");
        if (!storageDir.exists())
            storageDir.mkdir();
//                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = "file:" + image.getAbsolutePath();
        str_Path = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && resultCode == RESULT_OK) {
            try {
//                Bundle extras = data.getExtras();
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(photoPath));
//                    (Bitmap) extras.get("data");
                iv_Image.setImageBitmap(imageBitmap);
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    public void saveData(String jobSeekerId, String fullName, String POandBD, String workDetails, String workDuration, String payOrderRelease,
                         String musterId, String noOfworkingDays, String amtToPaid, String actWorkedDays, String actAmtPaid, String diffInAmt,
                         String isJobCard, String isPassBook, String isPayslipIssuing, String respPerson, String respPersonDesg,
                         String category1, String category2, String category3, String comments, Worker worker, AlertDialog dialog) {
        String created_time = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        System.out.println("created timestamp" + dateFormat.format(cal.getTime()));
        created_time = dateFormat.format(cal.getTime());


        String[] jobseeker = jobSeekerId.trim().split("\\/");


        long result = dalDoorToDoorResults.insertOrUpdateDoorToDoorResultData(mDatabase,
                "",
                jobseeker[0],
                jobseeker[1],
                fullName,
                POandBD,
                workDetails,
                workDuration,
                payOrderRelease,
                musterId,
                noOfworkingDays,
                amtToPaid,
                actWorkedDays,
                actAmtPaid,
                diffInAmt,
                isJobCard,
                isPassBook,
                isPayslipIssuing,
                respPerson,
                respPersonDesg,
                category1,
                category2,
                category3,
                created_time,
                comments,
                Constants.userMasterDO.userName, (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())).format(new Date()),
                Constants.userMasterDO.userName, "", worker.getDistrict_code(), worker.getMandal_code(),
                worker.getPanchayat_code());
        if (result > 0) {
            Utils.showalert(this, "Alert", "Saved successfully");
            dialog.dismiss();
            checkData();
        } else
            Utils.showalert(this, "Alert", "Failed to save");
    }


    private String uploadFile(String paht) {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Constants.URLBase + "updatePhoto");

        try {
            File file = new File(paht);
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            entityBuilder.addBinaryBody("file", file);
            HttpEntity entity = entityBuilder.build();
            httppost.setEntity(entity);
            HttpResponse response = httpclient.execute(httppost);
//            HttpEntity httpEntity = response.getEntity();

//            HttpClient httpClient = AndroidHttpClient.newInstance("App");
//            HttpPost httpPost = new HttpPost(Constants.URLBase + "updatePhoto");
//            File file = new File(paht);
//            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
//            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//            entityBuilder.addBinaryBody("file", file);
//            HttpEntity entity = entityBuilder.build();
//            httpPost.setEntity(new FileEntity(new File(str_Path), "application/octet-stream"));
//
//            HttpResponse response = httpClient.execute(httpPost);
//            int statusCode = response.getStatusLine().getStatusCode();
//            responseString = String.valueOf(statusCode);
//            Utils.showalert(Format4ARow.this, "Alert", statusCode+"");

//            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

//            File sourceFile = new File(str_Path);

            // Adding file data to http body
//            entity.addPart("file", new FileBody(sourceFile));

//            long totalSize = entity.getContentLength();
//            httppost.setEntity(entity);

            // Making server call
//            HttpResponse response = httpclient.execute(httppost);
//            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                responseString = "Success";
//                        EntityUtils.toString(r_entity);
            } else {
                responseString = "Error occurred! Http Status Code: "
                        + statusCode;
            }

        } catch (ClientProtocolException e) {
            responseString = e.toString();
            Utils.showalert(Format4ARow.this, "Alert", e.getMessage() + " " + responseString);
        } catch (IOException e) {
            responseString = e.toString();
            Utils.showalert(Format4ARow.this, "Alert", e.getMessage() + " " + responseString);
        }

        return responseString;

    }

    public int uploadFile1(final String selectedFilePath){

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
//            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
                }
            });
            return 0;
        }else{
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(Constants.URLBase + "updatePhoto");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data");
                connection.setRequestProperty("file",selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                //response code of 200 indicates the server status OK
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            tvFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://coderefer.com/extras/uploads/"+ fileName);
                            Utils.showalert(Format4ARow.this, "Alert", "Completed");
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();



            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Format4ARow.this,"File Not Found",Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(Format4ARow.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(Format4ARow.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
//            dialog.dismiss();
            return serverResponseCode;
        }

    }


    class uploadImage extends android.os.AsyncTask<String, String, String>
    {
        String result;

        public uploadImage()

        {

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... webServiceDOS) {
            String response = "";
            try {
                response = uploadFile(str_Path);
            }
            catch (Exception e)
            {
                e.getMessage();
                Utils.showalert(Format4ARow.this, "Alert", e.getMessage());

            }
            return response;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            Utils.showalert(Format4ARow.this, "Alert", res);
//            Utils.hideProgress(progressDialog);
//            updateDashboard();
        }
    }
}


 /* checkList.add(new Worker("S.No",
                "Job Seeker Id (or) Wage Seeker Id",
                "Full name",
                "Post Office / Bank Account Details",
                "Work Details Work-ID/Work_name/Work_location",
                "Work Duration from - to",
                "Pay-order Release Date",
                "Muster Id Number",
                "No of working Days",
                "Amount to be Paid",
                "Actual No of days worked",
                "Actual Paid Amount",
                "Difference in amount paid",
                "Is Job-card-id is available with Job-Seekers(Yes/No)",
                "Is Passbook is available with Job-Seekers(Yes/No)",
                "PaySlips are Issuing(Yes/No)",
                "Responsible Person Name",
                "Responsible Person Job-Designation",
                "Category-I",
                "Category-II",
                "Category-III",
                "No Data",
                "No data"));
*/

       /* checkList.add(new Worker("S.No",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "No of working Days",
                "Post Office / Bank Account Details                                   Amount to be Paid",
                "Full name                                                                     Actual No of days worked",
                "Actual Paid Amount",
                "Job Seeker Id (or) Wage Seeker Id                                          Difference in amount paid",
                "Work Details Work-ID/Work_name/Work_location                     Is Job-card-id is available with Job-Seekers(Yes/No)",
                "Is Passbook is available with Job-Seekers(Yes/No)",
                "PaySlips are Issuing(Yes/No)",
                "Work Duration from - to            Responsible Person Name",
                "Responsible Person Job-Designation",
                "Muster Id Number                                 Category-I",
                "Category-II",
                "Pay-order Release Date                                Category-III",
                "No Data",
                "No data"));*/

       /* checkList.add(new Worker("2.1.1",
                "2.1.2",
                "2.1.3",
                "2.1.4",
                "2.1.5",
                "2.1.6",
                "2.1.7",
                "2.1.8",
                "2.1.9",
                "2.1.10",
                "2.1.11",
                "2.1.12",
                "2.1.13",
                "2.1.14",
                "2.1.15",
                "2.1.16",
                "2.1.17",
                "2.1.18",
                "2.1.19",
                "2.1.20",
                "2.1.21",
                "",
                ""));*/