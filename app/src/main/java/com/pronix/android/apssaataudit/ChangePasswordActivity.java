package com.pronix.android.apssaataudit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pronix.android.apssaataudit.Dal.DalUserMaster;
import com.pronix.android.apssaataudit.common.Constants;
import com.pronix.android.apssaataudit.common.Utils;
import com.pronix.android.apssaataudit.models.WebServiceDO;
import com.pronix.android.apssaataudit.services.AsyncTask;
import com.pronix.android.apssaataudit.services.OnTaskCompleted;

import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

/**
 * Created by ravi on 1/12/2018.
 */

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener, OnTaskCompleted {
    EditText et_Mobile, et_OldPin, et_NewPin, et_ConfirmNewPin;
    Button but_Submit;
    SpotsDialog dialog;
    DalUserMaster dalUserMaster;
    String screen;
    TextInputLayout til_OldPin;
    TextView tv_Header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        dialog = new SpotsDialog(this, "Creating account, Please wait......", R.style.Custom);
        dalUserMaster = new DalUserMaster();
        initializeControls();
    }

    public void initializeControls()
    {
        try {
            et_Mobile = (EditText) findViewById(R.id.et_Mobile);
            screen = getIntent().getStringExtra("SCREEN");
            til_OldPin = (TextInputLayout) findViewById(R.id.tl_OldPin);
            tv_Header = (TextView) findViewById(R.id.tv_Header);
            et_OldPin = (EditText) findViewById(R.id.et_OldPin);
            et_NewPin = (EditText) findViewById(R.id.et_NewPin);
            et_ConfirmNewPin = (EditText) findViewById(R.id.et_ConfirmNewPin);
            but_Submit = (Button) findViewById(R.id.but_Submit);
            but_Submit.setOnClickListener(this);
            if (screen == null || screen.equals(""))
                et_Mobile.setText(Constants.userMasterDO.mobileNumber);
            else {
                et_Mobile.setText(getIntent().getStringExtra("MOBILE"));
                til_OldPin.setVisibility(View.GONE);
                tv_Header.setText("Forget Password");
            }
        }
        catch (Exception e)
        {

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.but_Submit:
                if(validattion())
                {
                    if(screen.equals(""))
                        callService();
                    else
                        callForgetPasswordService();
                }
                break;
        }
    }

    public boolean validattion()
    {
        boolean status = false;
        if(et_OldPin.getText().toString().trim().equals("") && screen.equals(""))
        {
            Utils.showalert(this, "Alert", "Enter oldpin");
        }
        else if(!et_OldPin.getText().toString().trim().equals(Constants.PIN) && screen.equals(""))
        {
            Utils.showalert(this, "Alert", "Invalid oldpin");
        }
        else if(et_NewPin.getText().toString().trim().equals(""))
        {
            Utils.showalert(this, "Alert", "Enter newpin");
        }
        else if(et_ConfirmNewPin.getText().toString().trim().equals(""))
        {
            Utils.showalert(this, "Alert", "Enter confirm newpin");
        }
        else if(!et_NewPin.getText().toString().trim().equals(et_ConfirmNewPin.getText().toString().trim()))
        {
            Utils.showalert(this, "Alert", "new pin and confirm newpin should be same");
        }
        else if(et_NewPin.getText().toString().trim().equals(et_OldPin.getText().toString().trim())  && screen.equals(""))
        {
            Utils.showalert(this, "Alert", "old pin and  newpin should not be same");
        }
        else
        {
            status = true;
        }

        return status;

    }

    public WebServiceDO callService()
    {
        WebServiceDO result = null;
        try {
            dialog.show();
            WebServiceDO webServiceDO = new WebServiceDO();
            JSONObject json = new JSONObject();
            json.put("userId", Constants.userMasterDO.userId);
            json.put("pin", et_OldPin.getText().toString().trim());
            json.put("newPin", et_NewPin.getText().toString().trim());
            webServiceDO.result = Constants.SENT;
            new AsyncTask(ChangePasswordActivity.this, ChangePasswordActivity.this, Constants.URLBase + "" + "changePassword", "POST", json.toString()).execute(webServiceDO);

        }
        catch (Exception e)
        {
            Utils.hideProgress(dialog);
            Utils.showalert(this, "Alert", e.getMessage());
        }
        return result;
    }

    public WebServiceDO callForgetPasswordService()
    {
        WebServiceDO result = null;
        try {
            dialog.show();
            WebServiceDO webServiceDO = new WebServiceDO();
            JSONObject json = new JSONObject();
            json.put("mobile", et_Mobile.getText().toString());
            json.put("pin", et_NewPin.getText().toString().trim());
            webServiceDO.result = Constants.SENT;
            new AsyncTask(ChangePasswordActivity.this, ChangePasswordActivity.this, Constants.URLBase + "" + "updatePassword", "POST", json.toString()).execute(webServiceDO);

        }
        catch (Exception e)
        {
            Utils.hideProgress(dialog);
            Utils.showalert(this, "Alert", e.getMessage());
        }
        return result;
    }

    @Override
    public void onTaskCompletes(WebServiceDO webServiceDO) {
        try
        {
            Utils.hideProgress(dialog);
            if(webServiceDO.result.equals(Constants.SUCCESS)) {
                JSONObject jsonObject = new JSONObject(webServiceDO.responseContent);
                if(jsonObject.getString("status").toUpperCase().equals("SUCCESS"))
                {
                    if(screen.equals(""))
                        dalUserMaster.updatepin(et_Mobile.getText().toString(), et_NewPin.getText().toString());
                    Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }
                else
                {
                    Utils.showalert(ChangePasswordActivity.this, "Alert",jsonObject.getString("errorDescription"));
                }
            }
            else
                Utils.showalert(ChangePasswordActivity.this, "Alert",webServiceDO.responseContent);

        }
        catch (Exception e)
        {
            e.getMessage();
        }
    }
}
