package com.pronix.android.apssaataudit;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pronix.android.apssaataudit.Dal.DalUserMaster;
import com.pronix.android.apssaataudit.common.Constants;
import com.pronix.android.apssaataudit.common.Utils;
import com.pronix.android.apssaataudit.models.WebServiceDO;
import com.pronix.android.apssaataudit.services.AsyncTask;
import com.pronix.android.apssaataudit.services.OnTaskCompleted;

import org.json.JSONObject;

import dmax.dialog.SpotsDialog;


public class ProfileActivity extends BaseActivity implements View.OnClickListener, OnTaskCompleted{
    private TextView user_profile_name, txt_designation, txt_mobilenumber, txt_pin;
    EditText et_Email, et_Mobile, et_Pin, et_Name;
    ImageButton ib_Email, ib_Mobile, ib_Name;
    Button but_Update;
    SpotsDialog dialog;
    DalUserMaster dalUserMaster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_profiles, frame_base);
        dalUserMaster = new DalUserMaster();
        dialog = new SpotsDialog(this,"Please wait..", R.style.Custom);
        initializeConrols();

    }

    public void initializeConrols()
    {
        user_profile_name = (TextView) findViewById(R.id.user_profile_name);
        txt_designation = (TextView) findViewById(R.id.txt_designation);
        user_profile_name.setText(Constants.userMasterDO.userName);
        txt_designation.setText(Constants.userMasterDO.designation);
        et_Email = (EditText) findViewById(R.id.et_Email);
        et_Mobile = (EditText) findViewById(R.id.et_MobileNumber);
        et_Pin = (EditText) findViewById(R.id.et_Pin);
        et_Name = (EditText) findViewById(R.id.et_Name);
        ib_Name = (ImageButton) findViewById(R.id.ib_Name);
        ib_Name.setOnClickListener(this);
        ib_Email = (ImageButton) findViewById(R.id.ib_email);
        ib_Email.setOnClickListener(this);
        ib_Mobile = (ImageButton) findViewById(R.id.ib_MobileNumber);
        ib_Mobile.setOnClickListener(this);
        ib_Mobile.setVisibility(View.GONE);
        but_Update = (Button) findViewById(R.id.but_Update);
        but_Update.setOnClickListener(this);
        et_Name.setText(Constants.userMasterDO.userName);
        et_Email.setText(Constants.userMasterDO.email);
        et_Mobile.setText(Constants.userMasterDO.mobileNumber);
        et_Email.setEnabled(false);
        et_Mobile.setEnabled(false);
        et_Pin.setEnabled(false);
        et_Name.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ib_email:
                et_Email.setEnabled(true);
                break;
            case R.id.ib_MobileNumber:
                et_Mobile.setEnabled(true);
                break;
            case R.id.ib_Name:
                et_Name.setEnabled(true);
                break;
            case R.id.but_Update:
                if(Utils.isNetworkAvailable(this))
                    calWebService();
                else
                    Utils.showalert(this, "Alert", "Please verify network");
                break;

        }

    }

    public void calWebService()
    {
        try {
            dialog.show();
            WebServiceDO webServiceDO = new WebServiceDO();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", Constants.userMasterDO.userId);
            jsonObject.put("name", et_Name.getText().toString());
            jsonObject.put("email", et_Email.getText().toString());
            webServiceDO.result = Constants.SENT;
            new AsyncTask(ProfileActivity.this, ProfileActivity.this, Constants.URLBase + "" + "updateProfile", "POST", jsonObject.toString()).execute(webServiceDO);
        }
        catch (Exception e)
        {
            Utils.hideProgress(dialog);
        }
    }

    @Override
    public void onTaskCompletes(WebServiceDO webServiceDO) {
        try {
            Utils.hideProgress(dialog);
            if (webServiceDO.result.equals(Constants.SUCCESS)) {
                JSONObject jsonObject = new JSONObject(webServiceDO.responseContent);
                if (jsonObject.getString("status").toUpperCase().equals("SUCCESS"))
                {
                    Utils.showalert(this, "Alert", "Profile updated succesfully");
                    dalUserMaster.updateProfile(et_Name.getText().toString(), et_Email.getText().toString(), et_Mobile.getText().toString());
                    Constants.userMasterDO.userName = et_Name.getText().toString();
                    Constants.userMasterDO.email = et_Email.getText().toString();
                    et_Email.setEnabled(false);
                    et_Mobile.setEnabled(false);
                    et_Pin.setEnabled(false);
                    et_Name.setEnabled(false);
                }
                else
                    {
                        Utils.showalert(this, "Alert", jsonObject.getString("errorDescription"));
                    }
                }
         else {
                Utils.showalert(this, "Alert", webServiceDO.responseContent);
            }
        }
        catch (Exception e)
        {
            e.getMessage();
        }
    }
}
