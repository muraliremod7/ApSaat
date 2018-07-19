package com.pronix.android.apssaataudit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.pronix.android.apssaataudit.Dal.DalDoorToDoorResults;
import com.pronix.android.apssaataudit.Dal.DalWorksiteResults;
import com.pronix.android.apssaataudit.common.Constants;
import com.pronix.android.apssaataudit.common.Utils;
import com.pronix.android.apssaataudit.models.AuditItems;
import com.pronix.android.apssaataudit.models.WebServiceDO;
import com.pronix.android.apssaataudit.services.AsyncTask;
import com.pronix.android.apssaataudit.services.OnTaskCompleted;

import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class AuditDetailsActivity extends BaseActivity implements View.OnClickListener, OnTaskCompleted{
    private Intent intent;
    RelativeLayout rl_D2DAudit, rl_WorksiteAudit, rl_RecordVerification, rl_Sync;
    DalDoorToDoorResults dalDoorToDoorResults;
    DalWorksiteResults dalWorksiteResults;
    SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_auditdashboard, frame_base);
        dalDoorToDoorResults = new DalDoorToDoorResults();
        dalWorksiteResults = new DalWorksiteResults();
        dialog = new SpotsDialog(this, "Syncing data, Please wait...", R.style.Custom);
        dialog.setCancelable(false);
        initializeConrols();

    }

    public void initializeConrols()
    {
        rl_D2DAudit = (RelativeLayout) findViewById(R.id.rl_D2DAudit);
        rl_D2DAudit.setOnClickListener(this);
        rl_WorksiteAudit = (RelativeLayout) findViewById(R.id.rl_WorksiteAudit);
        rl_WorksiteAudit.setOnClickListener(this);
        rl_RecordVerification = (RelativeLayout) findViewById(R.id.rl_RecordVerification);
        rl_RecordVerification.setOnClickListener(this);
        rl_Sync = (RelativeLayout) findViewById(R.id.rl_Sync);
        rl_Sync.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.rl_D2DAudit:
                intent = new Intent(AuditDetailsActivity.this, DoortoDoorAtivity.class);
                startActivity(intent);
                AuditDetailsActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.rl_WorksiteAudit:
                intent = new Intent(AuditDetailsActivity.this, DoortoDoorF5Ativity.class);
                startActivity(intent);
                AuditDetailsActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.rl_RecordVerification:
                intent = new Intent(AuditDetailsActivity.this, RecordVerificationActivity.class);
                startActivity(intent);
                AuditDetailsActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.rl_Sync:
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AuditDetailsActivity.this);
//                alertDialog.setTitle("SSAAT");
//
//                alertDialog.setMessage("DataSync Process Completed Successfully?");
//
//                alertDialog.setIcon(R.mipmap.ic_launcher);
//
//                alertDialog.setPositiveButton("OK.Gotit", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        dialog.dismiss();
//                    }
//                });
//
//                alertDialog.show();
                calWebservice();
                break;
        }
    }

    public String getSyncDetails() throws Exception
    {
        JSONObject json = new JSONObject();
        try {

            json.put("apssaatFormat5AAuditResults", dalWorksiteResults.getWorksiteData());
            json.put("apssaatFormat4AAuditResults", dalDoorToDoorResults.getDoorToDoorData());
        }
        catch (Exception e)
        {
            throw e;
        }
        return json.toString();

    }

    public void calWebservice()
    {
        try
        {
            dialog.show();
            String parameter = getSyncDetails();

            WebServiceDO webServiceDO = new WebServiceDO();
            webServiceDO.result = Constants.SENT;
            new AsyncTask(AuditDetailsActivity.this, AuditDetailsActivity.this, Constants.URLBase + "" + Constants.REQUEST_SYNCDATA, "POST", parameter).execute(webServiceDO);
//            Utils.hideProgress(dialog);
        }
        catch (Exception e)
        {
            Utils.hideProgress(dialog);
            Utils.showalert(this, "Alert", e.getMessage());
        }
    }

    @Override
    public void onTaskCompletes(WebServiceDO webServiceDO) {
        try
        {
            Utils.hideProgress(dialog);
            if(webServiceDO.result.equals(Constants.SUCCESS))
            {
                JSONObject json = new JSONObject(webServiceDO.responseContent);
                if(json.getString("status").toUpperCase().equals("SUCCESS")) {
                    Utils.showalert(this, "Alert", "Sync done successfully");
                    dalDoorToDoorResults.updateServerFlag();
                    dalWorksiteResults.updateServerFlag();
                }
                else
                {
                    Utils.showalert(this, "Alert", json.getString("errorDescription"));
                }
            }
            else
                Utils.showalert(this, "Alert", webServiceDO.responseContent);
        }
        catch (Exception e)
        {
            Utils.hideProgress(dialog);
            Utils.showalert(this, "Alert", e.getMessage());
        }

    }
}
