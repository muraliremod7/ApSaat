package com.pronix.android.apssaataudit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.pronix.android.apssaataudit.Dal.DalUserMaster;
import com.pronix.android.apssaataudit.common.Constants;
import com.pronix.android.apssaataudit.common.Utils;
import com.pronix.android.apssaataudit.db.DBManager;
import com.pronix.android.apssaataudit.models.UserMasterDO;
import com.pronix.android.apssaataudit.models.WebServiceDO;
import com.pronix.android.apssaataudit.services.AsyncTask;
import com.pronix.android.apssaataudit.services.OnTaskCompleted;
import com.pronix.android.apssaataudit.session.SessionManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dmax.dialog.SpotsDialog;


public class LoginActivity extends AppCompatActivity implements OnTaskCompleted, View.OnClickListener {
    private Button btn_login;
    private EditText edt_username, edt_pwd;
    EditText et_Mobile;
    private TextView txt_forgot, txt_signup;
    Spinner sp_Langauge;
    SessionManager sessionManager;
    DalUserMaster dalUserMaster;
    SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dalUserMaster = new DalUserMaster();
        dialog = new SpotsDialog(this,"Please wait..", R.style.Custom);
        dialog.setCancelable(false);

        //Initializing the database & opening the database
        try {
            DBManager.initializeInstance(this);
        } catch (Exception e) {
            e.getMessage();
        }
        sessionManager = new SessionManager(this);
        initializeControls();


    }

    public void initializeControls() {
        txt_signup = (TextView) findViewById(R.id.txt_signup);
        txt_forgot = (TextView) findViewById(R.id.txt_forgot);
        txt_forgot.setOnClickListener(this);
        edt_username = (EditText) findViewById(R.id.edt_username);
//        edt_username.setText("nouser");
        edt_pwd = (EditText) findViewById(R.id.edt_pwd);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setText(R.string.Login);
        sp_Langauge = (Spinner) findViewById(R.id.sp_Langauge);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLoginValidation("");
            }
        });

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                LoginActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

        sp_Langauge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Locale locale;
                if (sp_Langauge.getSelectedItem().equals("Telugu")) {
                    locale = new Locale("te");
                    Locale.setDefault(locale);
                } else {
                    locale = new Locale("en");
                    Locale.setDefault(locale);
                }
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                btn_login.setText(R.string.Login);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getLoginValidation(String url) {
        if (edt_username.getText().toString().equalsIgnoreCase("") && edt_pwd.getText().toString().equalsIgnoreCase("")) {
            edt_username.setError("user name is required");
            edt_pwd.setError("password is required");
        } else if (edt_username.getText().toString().equalsIgnoreCase("")) {
            edt_username.setError("user name is required");
        } else if (edt_pwd.getText().toString().equalsIgnoreCase("")) {
            edt_pwd.setError("Please enter valid PIN");
        }
         else if(edt_pwd.getText().toString().trim().length() != 4)
        {
            edt_pwd.setError("Please enter valid PIN");
        } else {

            if (Utils.isNetworkAvailable(this)) {
                calWebService();
            } else {
                verifyLocalCredentials();
            }
        }
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
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

    public void calWebService() {
        try {
            dialog.show();
            WebServiceDO webServiceDO = new WebServiceDO();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mobile", edt_username.getText().toString());
            jsonObject.put("pin", edt_pwd.getText().toString());
            webServiceDO.result = Constants.SENT;
            webServiceDO.request = "LOGIN";
            new AsyncTask(LoginActivity.this, LoginActivity.this, Constants.URLBase + "" + "login", "POST", jsonObject.toString()).execute(webServiceDO);
        } catch (Exception e) {
            Utils.hideProgress(dialog);
            e.getMessage();
        }
    }

    public void calForgetWebService() {
        try {
            dialog.show();
            WebServiceDO webServiceDO = new WebServiceDO();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mobile", edt_username.getText().toString());
            webServiceDO.result = Constants.SUCCESS;
            webServiceDO.request = "FORGETPASSWORD";
            new AsyncTask(LoginActivity.this, LoginActivity.this, Constants.URLBase + "" + "forgotPassword", "POST", jsonObject.toString()).execute(webServiceDO);
        } catch (Exception e) {
            Utils.hideProgress(dialog);
            e.getMessage();
        }
    }

    public void verifyLocalCredentials()
    {
        String pinnumber = dalUserMaster.getPin(edt_username.getText().toString());
        if(edt_username.getText().toString().trim().equalsIgnoreCase(dalUserMaster.getMobile(edt_username.getText().toString())))
        {
            if (edt_pwd.getText().toString().trim().equalsIgnoreCase(pinnumber)) {
                moveToMenuScreen();
            } else {
                Utils.showalert(this, "Alert", "Invalid pin");
//            edt_pwd.setError("Please enter valid PIN");
            }
        }
        else
        Utils.showalert(this, "Alert", "Please verify network connection");
    }
    public void moveToChangePasswordScreen()
    {
        Intent in = new Intent();
        in.setClass(this, ChangePasswordActivity.class);
        in.putExtra("SCREEN","LOGIN");
        in.putExtra("MOBILE",edt_username.getText().toString());
        startActivity(in);
    }


    @Override
    public void onTaskCompletes(WebServiceDO webServiceDO) {
        try {
            Utils.hideProgress(dialog);
            if(webServiceDO.result.equals(Constants.SUCCESS))
            {
                JSONObject jsonObject = new JSONObject(webServiceDO.responseContent);
                if(webServiceDO.request.equals("FORGETPASSWORD"))
                {
                    if (jsonObject.getString("status").toUpperCase().equals("SUCCESS")) {
                        moveToChangePasswordScreen();
                    }
                    else
                    {
                        Utils.showalert(this, "Alert", jsonObject.getString("errorDescription"));
                    }
                }
                else
                    {

                    JSONObject json = new JSONObject(jsonObject.getString("responseStatus"));
                    if (json.getString("status").toUpperCase().equals("SUCESS")) {
                        JSONObject userDetails = new JSONObject(jsonObject.getString("userDetails"));
                        long res = dalUserMaster.saveUserDetails(userDetails.getString("userId"), userDetails.getString("name")
                                , userDetails.getString("mobile"), userDetails.getString("email"), userDetails.getString("pin")
                                , userDetails.getString("designation"), userDetails.getString("empId")
                                , (new SimpleDateFormat("yyyy-MM-dd HH:mm:SS", Locale.getDefault())).format(new Date()));
                        if (res != -1 && res != 0) {
                            moveToMenuScreen();
                        } else
                            Utils.showalert(this, "Alert", "Failed to update login details");
                    } else
                        Utils.showalert(this, "Alert", json.getString("errorDescription"));
                }
            }
            else
            {
                Utils.showalert(this, "Alert", webServiceDO.responseContent);
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void moveToMenuScreen()
    {
        Constants.userMasterDO = new UserMasterDO();
        Intent intent = new Intent(LoginActivity.this, SSAATScheme.class);
        Constants.PIN = edt_pwd.getText().toString();
        Constants.USERORMOBILE = edt_username.getText().toString();
        sessionManager.createLoginSession("", "", "", "", "", "");
        startActivity(intent);
        LoginActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.txt_forgot:
                if(validateForgetPassword())
                {
                    calForgetWebService();
                }
                break;
        }
    }

    public boolean validateForgetPassword()
    {
        boolean status = false;
        if(edt_username.getText().toString().trim().equals("")) {
            edt_username.requestFocus();
            edt_username.setError("Mobile number required");
            status = false;
        }
        else if(edt_username.getText().toString().trim().length() != 10)
        {
            edt_username.requestFocus();
            edt_username.setError("Mobile should be 10 digits");
            status = false;
        }
        else
        {
            status = true;
        }
        return status;
    }
}
