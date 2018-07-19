package com.pronix.android.apssaataudit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.pronix.android.apssaataudit.Dal.DalWorksiteDetails;
import com.pronix.android.apssaataudit.Dal.DalWorksiteResults;
import com.pronix.android.apssaataudit.common.Constants;
import com.pronix.android.apssaataudit.common.Utils;
import com.pronix.android.apssaataudit.models.WorkSitePOJO;
import com.pronix.android.apssaataudit.models.Worker;
import com.pronix.android.apssaataudit.render.WorkersAdapter5A;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Format5ARow extends AppCompatActivity implements View.OnClickListener {

    TextView serialNo, work_code_tv, allWorkDetails, taskDetails, techType, ap_measure, ap_total, amb_measure, amb_total;
    EditText cv_measure, cv_total, diff_measure, diff_total,
            respPersonName, respPersonDesig, imp_of_work, comments;
    Spinner isworkDoneSpinner;
    SQLiteDatabase mDatabase;
    private ArrayList<WorkSitePOJO> checkList = new ArrayList<WorkSitePOJO>();
    Bundle bundle;
    String work_code = "";
    RecyclerView rcyVw_workers;
    WorkersAdapter5A mAdapter;
    Context mCon = this;
    TextView tv_SNo, tv_WorkId, tv_WorkNameOrLocatio, tv_TaskDetails, tv_TechnologyType, tv_ApprEstValues,
            tv_ApprEstValues_Measurements, Tv_ApprEstValues_Total, tv_AsPerMBReport, tv_AsPerMBReport_Measurements, tv_AsPerMBReport_Total;
    TextView tv_isWorkDone, tv_CheckingValues, tv_CheckingValues_Measurements, tv_CheckingValues_Total, tv_Difference, tv_Difference_Measurements, tv_Difference_Total, tv_ResponsiblePersonName, tv_RespPersonJobDesg, tv_ImportanceOfWork, tv_Comments;
    DalWorksiteDetails dalWorksiteDetails;
    DalWorksiteResults dalWorksiteResults;
    Button but_Save;
    LinearLayout ll_List;
    Button but_Submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_format5a);
        dalWorksiteDetails = new DalWorksiteDetails();
        dalWorksiteResults = new DalWorksiteResults();
//        initializeControls();
//        setTextValues();
        initializeXML();

//        rcyVw_workers = (RecyclerView) findViewById(R.id.rcyVw_workers);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            work_code = bundle.getString("work_code");
            System.out.println("work_code" + work_code);
        }
//        mAdapter = new WorkersAdapter5A(mCon, checkList);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        rcyVw_workers.setLayoutManager(mLayoutManager);
//        rcyVw_workers.setNestedScrollingEnabled(false);
//        rcyVw_workers.setAdapter(mAdapter);

        checkData();
    }

    private void checkData() {
        checkList = dalWorksiteDetails.getWorkSiteDetails(work_code);

        System.out.println("---Selected work site array size: " + checkList.size());
        //tvMainSelectedCate.setText("First column(district codes): "+checkList.toString());
//        loadListView(checkList);
        loadData();
    }

    private void loadListView(ArrayList<WorkSitePOJO> checkList) {
        if (checkList.size() > 0) {
            mAdapter = new WorkersAdapter5A(mCon, checkList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            rcyVw_workers.setLayoutManager(mLayoutManager);
            rcyVw_workers.setAdapter(mAdapter);
        } else {
            Toast.makeText(Format5ARow.this, "No data found for this work code", Toast.LENGTH_SHORT).show();
        }

    }

    public void initializeXML() {
        ll_List = (LinearLayout) findViewById(R.id.ll_Form5aList);
        but_Submit = (Button) findViewById(R.id.but_Submit);
        but_Submit.setOnClickListener(this);
    }



    public void initializeControls() {
        tv_SNo = (TextView) findViewById(R.id.tv_SNo);
        tv_WorkId = (TextView) findViewById(R.id.tv_WorkId);
        tv_WorkNameOrLocatio = (TextView) findViewById(R.id.tv_WorkNameOrLoc);
        tv_TaskDetails = (TextView) findViewById(R.id.tv_TaskDetails);
        tv_TechnologyType = (TextView) findViewById(R.id.tv_TechnologyType);
        tv_ApprEstValues = (TextView) findViewById(R.id.tv_apprEstValues);
        tv_ApprEstValues_Measurements = (TextView) findViewById(R.id.tv_apprEstValues_Measurements);
        Tv_ApprEstValues_Total = (TextView) findViewById(R.id.tv_apprEstValues_Total);
        tv_AsPerMBReport = (TextView) findViewById(R.id.tv_AsPerMBReport);
        tv_AsPerMBReport_Measurements = (TextView) findViewById(R.id.tv_AsPerMBReport_Total);
        tv_AsPerMBReport_Total = (TextView) findViewById(R.id.tv_AsPerMBReport_Total);
        tv_isWorkDone = (TextView) findViewById(R.id.tv_IsWorkDone);
        tv_CheckingValues = (TextView) findViewById(R.id.tv_CheckingValues);
        tv_CheckingValues_Measurements = (TextView) findViewById(R.id.tv_CheckingValues_Measurements);
        tv_CheckingValues_Total = (TextView) findViewById(R.id.tv_CheckingValues_Total);
        tv_Difference = (TextView) findViewById(R.id.tv_Difference);
        tv_Difference_Measurements = (TextView) findViewById(R.id.tv_Difference_Measurements);
        tv_Difference_Total = (TextView) findViewById(R.id.tv_Difference_Total);
        tv_ResponsiblePersonName = (TextView) findViewById(R.id.tv_RespPersonName);
        tv_RespPersonJobDesg = (TextView) findViewById(R.id.tv_RespPersonDesg);
        tv_ImportanceOfWork = (TextView) findViewById(R.id.tv_ImportanceOfWork);
        tv_Comments = (TextView) findViewById(R.id.tv_Comments);
        but_Save = (Button) findViewById(R.id.but_Formate5ASave);
        but_Save.setOnClickListener(this);
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
            WorkSitePOJO obj = checkList.get(i);
            tv_WageSeekerId.setText(obj.getWork_code());
            tv_FullName.setText(obj.getWork_name());
            tv_WorkName.setText(obj.getTask_name());

            if (obj.getServerFlag().equals("0") || obj.getServerFlag().equals("1")) {
//                bt_view.setBackgroundColor(getResources().getColor(R.color.grey));
                if(!Constants.userMasterDO.designation.toUpperCase().equals("STM")) {
                    bt_view.setTextColor(getResources().getColor(R.color.colorPrimary));
                    bt_view.setVisibility(View.INVISIBLE);
                }
                else
                    bt_view.setOnClickListener(new Format5ARow.DetailsClickListener(obj));
            } else
                bt_view.setOnClickListener(new Format5ARow.DetailsClickListener(obj));
            ll_List.addView(v);

        }
    }

    public void setTextValues() {

        tv_SNo.setText(R.string.sNo);
        tv_WorkId.setText(R.string.workId);
        tv_WorkNameOrLocatio.setText(R.string.workNameorLocation);
        tv_TaskDetails.setText(R.string.taskDetails);
        tv_TechnologyType.setText(R.string.technologyType);
        tv_ApprEstValues.setText(R.string.approvedEstvalues);
        tv_ApprEstValues_Measurements.setText(R.string.measurments);
        Tv_ApprEstValues_Total.setText(R.string.total);
        tv_AsPerMBReport.setText(R.string.asPerMBReporrt);
        tv_AsPerMBReport_Measurements.setText(R.string.measurments);
        tv_AsPerMBReport_Total.setText(R.string.total);
        tv_isWorkDone.setText(R.string.isWorkDone);
        tv_CheckingValues.setText(R.string.checkingValues);
        tv_CheckingValues_Measurements.setText(R.string.measurments);
        tv_CheckingValues_Total.setText(R.string.total);
        tv_Difference.setText(R.string.difference);
        tv_Difference_Measurements.setText(R.string.measurments);
        tv_Difference_Total.setText(R.string.total);
        tv_ResponsiblePersonName.setText(R.string.resPersonName);
        tv_RespPersonJobDesg.setText(R.string.resPersonJobDesg);
        tv_ImportanceOfWork.setText(R.string.impOfWork);
        tv_Comments.setText(R.string.comments);

    }

    private boolean checkValidations() {
        boolean flag = false;
        for (int i = 0; i < checkList.size(); i++) {
            View view = rcyVw_workers.getChildAt(i);
            flag = false;
            ap_measure = view.findViewById(R.id.ap_measure);
            ap_total = view.findViewById(R.id.ap_total);
            amb_measure = view.findViewById(R.id.amb_measure);
            amb_total = view.findViewById(R.id.amb_total);
            isworkDoneSpinner = view.findViewById(R.id.isworkDoneSpinner);
            cv_measure = view.findViewById(R.id.cv_measure);
            cv_total = view.findViewById(R.id.cv_total);
            diff_measure = view.findViewById(R.id.diff_measure);
            diff_total = view.findViewById(R.id.diff_total);
            respPersonName = view.findViewById(R.id.respPersonName);
            respPersonDesig = view.findViewById(R.id.respPersonDesig);
            imp_of_work = view.findViewById(R.id.imp_of_work);
            comments = view.findViewById(R.id.comments);
            if (cv_measure.getText().toString().trim().equals("")) {
                //Toast.makeText(mCon, "Actual Work Days should not be empty", Toast.LENGTH_SHORT).show();
//                actualWorkedDays.setError("Actual Work Days should not be empty");
                cv_measure.requestFocus();
                cv_measure.setBackgroundResource(R.drawable.table_edittext_alert_border);
                showalert(this, "Alert", "Checked Value Measurement should not be empty");
                break;
            } else {
                if (cv_total.getText().toString().trim().equals("")) {

                    cv_total.requestFocus();
                    cv_total.setBackgroundResource(R.drawable.table_edittext_alert_border);
                    showalert(this, "Alert", "Checked Value Total should not be empty");
                    break;
                } else {
                    if (diff_measure.getText().toString().trim().equals("")) {
                        diff_measure.requestFocus();
                        diff_measure.setBackgroundResource(R.drawable.table_edittext_alert_border);
                        showalert(this, "Alert", "Difference in Measurement should not be empty");
                        break;
                    } else {
                        if (diff_total.getText().toString().trim().equals("")) {
                            diff_total.requestFocus();
                            diff_total.setBackgroundResource(R.drawable.table_edittext_alert_border);
                            showalert(this, "Alert", "Diffrence in total should not be empty");
                            break;
                        } else {
                            if (imp_of_work.getText().toString().trim().equals("")) {
                                imp_of_work.requestFocus();
                                imp_of_work.setBackgroundResource(R.drawable.table_edittext_alert_border);
                                showalert(this, "Alert", "Importance of work should not be empty");
                                break;
                            } else {
                                flag = true;
                            }
                        }
                    }
                }
            }
        }
        return flag;
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

    public void saveListItems() {

        long result = -1;
        for (int i = 0; i < checkList.size(); i++) {
            View view = rcyVw_workers.getChildAt(i);
            serialNo = view.findViewById(R.id.serialNo);
            work_code_tv = view.findViewById(R.id.work_code);
            allWorkDetails = view.findViewById(R.id.allWorkDetails);
            taskDetails = view.findViewById(R.id.taskDetails);
            techType = view.findViewById(R.id.techType);
            ap_measure = view.findViewById(R.id.ap_measure);
            ap_total = view.findViewById(R.id.ap_total);
            amb_measure = view.findViewById(R.id.amb_measure);
            amb_total = view.findViewById(R.id.amb_total);
            isworkDoneSpinner = view.findViewById(R.id.isworkDoneSpinner);
            cv_measure = view.findViewById(R.id.cv_measure);
            cv_total = view.findViewById(R.id.cv_total);
            diff_measure = view.findViewById(R.id.diff_measure);
            diff_total = view.findViewById(R.id.diff_total);
            respPersonName = view.findViewById(R.id.respPersonName);
            respPersonDesig = view.findViewById(R.id.respPersonDesig);
            imp_of_work = view.findViewById(R.id.imp_of_work);
            comments = view.findViewById(R.id.comments);
            String created_time = "";
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Calendar cal = Calendar.getInstance();
            created_time = dateFormat.format(cal.getTime());

            result = dalWorksiteResults.insertOrUpdateWorksiteResultData(mDatabase,
                    serialNo.getText().toString(),
                    work_code_tv.getText().toString(),
                    allWorkDetails.getText().toString(),
                    taskDetails.getText().toString(),
                    techType.getText().toString(),
                    ap_measure.getText().toString(),
                    ap_total.getText().toString(),
                    amb_measure.getText().toString(),
                    amb_total.getText().toString(),
                    isworkDoneSpinner.getSelectedItem().toString(),
                    cv_measure.getText().toString(),
                    cv_total.getText().toString(),
                    diff_measure.getText().toString(),
                    diff_total.getText().toString(),
                    respPersonName.getText().toString(),
                    respPersonDesig.getText().toString(),
                    imp_of_work.getText().toString(),
                    comments.getText().toString(),
                    created_time,
                    Constants.userMasterDO.userName, (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())).format(new Date()),
                    Constants.userMasterDO.userName, "", checkList.get(i).getTask_code(), checkList.get(i).getDistrict_code(),
                    checkList.get(i).getMandal_code(), checkList.get(i).getPanchayat_code());
            if (result == 0 || result == -1) {
                break;
            }


//              Toast.makeText(this, tv_workId.getText().toString(), Toast.LENGTH_SHORT).show();
        }
        if (result != 0 && result != -1)
            Toast.makeText(mCon, "Details saved successfully", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(mCon, "Failed to save details", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_Formate5ASave:
                if (checkValidations()) {
                    saveListItems();
                }
                break;
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
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Format5ARow.this);
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

    public class DetailsClickListener implements View.OnClickListener {
        WorkSitePOJO obj;

        public DetailsClickListener(WorkSitePOJO worker) {
            obj = worker;
        }


        @Override
        public void onClick(View view) {
            showForm4aDetailsDialog(obj);
        }
    }

    public void showForm4aDetailsDialog(final WorkSitePOJO worksite) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.worksitedetails, null);
        final TextView tv_WorkName = (TextView) alertLayout.findViewById(R.id.tv_WorkName);
        final TextView tv_WorkId = (TextView) alertLayout.findViewById(R.id.tv_WorkId);
        final TextView tv_WorkDetails = (TextView) alertLayout.findViewById(R.id.tv_WorkNameOrLoc);
        final TextView tv_TaskDetails = (TextView) alertLayout.findViewById(R.id.tv_TaskDetails);
        final TextView tv_TechnologyType = (TextView) alertLayout.findViewById(R.id.tv_TechnologyType);
        final TextView tv_ApprEsti_Measurement = (TextView) alertLayout.findViewById(R.id.tv_apprEstValues_Measurements);
        final TextView tv_ApprEsti_Total = (TextView) alertLayout.findViewById(R.id.tv_apprEstValues_Total);
        final TextView tv_AsperMB_Measurement = (TextView) alertLayout.findViewById(R.id.tv_AsPerMBReport_Measurement);
        final TextView tv_AsperMB_Total = (TextView) alertLayout.findViewById(R.id.tv_AsPerMBReport_Total);
        final Spinner sp_IsWorkDone = (Spinner) alertLayout.findViewById(R.id.sp_IsWorkDone);
        final EditText et_CheckinValues_Meas = (EditText) alertLayout.findViewById(R.id.et_CheckingValues_Measurement);
        final EditText et_CheckinValues_Total = (EditText) alertLayout.findViewById(R.id.et_CheckingValues_Total);
        final EditText et_Diff_Meas = (EditText) alertLayout.findViewById(R.id.et_Diff_Measurement);
        final EditText et_Diff_Total = (EditText) alertLayout.findViewById(R.id.et_Diff_Total);
        final EditText et_Rep_PersonName = (EditText) alertLayout.findViewById(R.id.et_RespPersonName);
        final EditText et_Resp_Desg = (EditText) alertLayout.findViewById(R.id.et_RespPersonDesg);
        final EditText et_ImpOfWork = (EditText) alertLayout.findViewById(R.id.et_ImpOfWork);
        final EditText et_Comments = (EditText) alertLayout.findViewById(R.id.et_Comments);
        Button but_Save = (Button) alertLayout.findViewById(R.id.but_Save);

        tv_WorkName.setText(worksite.getWork_name());
        tv_WorkId.setText(worksite.getWork_code());
        tv_WorkDetails.setText(worksite.getWork_name() + "/" + worksite.getWork_location());
        tv_TaskDetails.setText(worksite.getTask_code() + "/" + worksite.getTask_name());
        tv_TechnologyType.setText(worksite.getSkill_type());
        tv_ApprEsti_Measurement.setText(worksite.getQty_sanc());
        tv_ApprEsti_Total.setText(worksite.getAmount_sanc());
        tv_AsperMB_Measurement.setText(worksite.getQty_done());
        tv_AsperMB_Total.setText(worksite.getAmount_spent());
        sp_IsWorkDone.setSelection(worksite.getIs_workdone().toUpperCase().equals("YES") ? 0 : 1, false);
        et_CheckinValues_Meas.setText(worksite.getCheckvalues_measurment());
        et_CheckinValues_Total.setText(worksite.getCheckvalues_total());
        et_Diff_Meas.setText(worksite.getDifference_measurments());
        et_Diff_Total.setText(worksite.getDifference_total());
        et_Rep_PersonName.setText(worksite.getResp_personnam());
        et_Resp_Desg.setText(worksite.getResp_person_jobdesg());
        et_ImpOfWork.setText(worksite.getImpofwork());
        et_Comments.setText(worksite.getComments());

        AlertDialog.Builder alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        alert.setView(alertLayout);
        final AlertDialog dialog = alert.create();
        but_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation(et_CheckinValues_Meas.getText().toString(), et_CheckinValues_Total.getText().toString(),
                        et_Diff_Meas.getText().toString(),
                        et_Diff_Total.getText().toString(), et_ImpOfWork.getText().toString())) {
                    saveData(tv_WorkId.getText().toString(),
                            tv_WorkDetails.getText().toString(),
                            tv_TaskDetails.getText().toString(),
                            tv_TechnologyType.getText().toString(),
                            tv_ApprEsti_Measurement.getText().toString(),
                            tv_ApprEsti_Total.getText().toString(),
                            tv_AsperMB_Measurement.getText().toString(),
                            tv_AsperMB_Total.getText().toString(),
                            sp_IsWorkDone.getSelectedItem().toString(),
                            et_CheckinValues_Meas.getText().toString(),
                            et_CheckinValues_Total.getText().toString(),
                            et_Diff_Meas.getText().toString(),
                            et_Diff_Total.getText().toString(),
                            et_Rep_PersonName.getText().toString(),
                            et_Resp_Desg.getText().toString(),
                            et_ImpOfWork.getText().toString(),
                            et_Comments.getText().toString(), worksite, dialog);
                }
            }

        });


        dialog.show();
    }

    public void saveData(String workId, String workDetails, String taskDetails, String technologyType, String appEstMeas,
                         String appEstTotal, String asPermbMeas, String asPerMBtotal, String isWorkDone, String checkValueMeas,
                         String checkValueTotal, String diffMeas, String diffTotal, String respPerson, String respPersonDesg,
                         String impOfWork,
                         String comments, WorkSitePOJO worksite, AlertDialog dialog) {
        String created_time = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        created_time = dateFormat.format(cal.getTime());
        long result = dalWorksiteResults.insertOrUpdateWorksiteResultData(mDatabase,
                "",
                workId,
                workDetails,
                taskDetails,
                technologyType,
                appEstMeas,
                appEstTotal,
                asPermbMeas,
                asPerMBtotal,
                isWorkDone,
                checkValueMeas,
                checkValueTotal,
                diffMeas,
                diffTotal,
                respPerson,
                respPersonDesg,
                impOfWork,
                comments,
                created_time,
                Constants.userMasterDO.userName, (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())).format(new Date()),
                Constants.userMasterDO.userName, "", worksite.getTask_code(), worksite.getDistrict_code(),
                worksite.getMandal_code(), worksite.getPanchayat_code());
        if (result > 0) {
            Utils.showalert(Format5ARow.this, "Alert", "Saved successfully");
            dialog.dismiss();
            checkData();
        } else
            Utils.showalert(Format5ARow.this, "Alert", "Failed to save");
    }

    private boolean validation(String checkValueMes, String checkValueTotal, String diffMeas, String diffTotal, String impOfWork) {
        boolean flag = false;

        flag = false;

        if (checkValueMes.trim().equals("")) {
            //Toast.makeText(mCon, "Actual Work Days should not be empty", Toast.LENGTH_SHORT).show();
//                actualWorkedDays.setError("Actual Work Days should not be empty");
            showalert(this, "Alert", "Checked Value Measurement should not be empty");
        } else {
            if (checkValueTotal.trim().equals("")) {

                showalert(this, "Alert", "Checked Value Total should not be empty");
            } else {
                if (diffMeas.trim().equals("")) {
                    showalert(this, "Alert", "Difference in Measurement should not be empty");
                } else {
                    if (diffTotal.trim().equals("")) {
                        showalert(this, "Alert", "Diffrence in total should not be empty");
                    } else {
                        if (impOfWork.trim().equals("")) {
                            showalert(this, "Alert", "Importance of work should not be empty");
                        } else {
                            flag = true;
                        }
                    }
                }
            }
        }

        return flag;
    }
}
