package com.pronix.android.apssaataudit;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.pronix.android.apssaataudit.Dal.DalUserMaster;
import com.pronix.android.apssaataudit.common.Constants;
import com.pronix.android.apssaataudit.common.Utils;
import com.pronix.android.apssaataudit.models.WebServiceDO;
import com.pronix.android.apssaataudit.services.AsyncTask;
import com.pronix.android.apssaataudit.services.OnTaskCompleted;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import dmax.dialog.SpotsDialog;


public class RegistrationActivity extends BaseActivity implements OnTaskCompleted {
    private EditText _nameText, _emailText, _passwordText, et_Mobile, et_EmpId;
    private Button _signupButton;
    private TextView _loginLink;
    Spinner spnrDesignation;
    DalUserMaster dalUserMaster;
    SpotsDialog dialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dalUserMaster = new DalUserMaster();
        dialog = new SpotsDialog(this, "Creating account, Please wait......", R.style.Custom);
        initializeControls();

    }

    public void initializeControls()
    {
        _nameText = (EditText) findViewById(R.id.input_name);
        _emailText = (EditText) findViewById(R.id.input_email);
        et_Mobile = (EditText) findViewById(R.id.input_number);
        _passwordText = (EditText) findViewById(R.id.input_password);
        et_EmpId = (EditText) findViewById(R.id.et_EmpId);
        _signupButton = (Button) findViewById(R.id.btn_signup);
        _loginLink = (TextView) findViewById(R.id.link_login);
        spnrDesignation = (Spinner) findViewById(R.id.spnrDesignation);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isNetworkAvailable(RegistrationActivity.this))
                signup();
                else
                    Utils.showalert(RegistrationActivity.this,"Alert", "Please verify network");
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        try {


            if (!validate()) {
                onSignupFailed();
                return;
            }
            _signupButton.setEnabled(false);


            final String name = _nameText.getText().toString();
            final String email = _emailText.getText().toString();
            final String password = _passwordText.getText().toString();
            final String number = et_Mobile.getText().toString();
            final String designation = spnrDesignation.getSelectedItem().toString();
            callservice();

//            new android.os.Handler().postDelayed(
//                    new Runnable() {
//                        public void run() {
//                            try {
//                                // On complete call either onSignupSuccess or onSignupFailed
//                                // depending on success
//                                Random random = new Random();
//                                int value = random.nextInt(10000);
//                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
//                                WebServiceDO result =callservice();
//                                if(result.equals(Constants.SUCCESS))
//                                {
//                                    long res = dalUserMaster.saveUserDetails("", name, number, email, password, designation, dateFormat.format(new Date()));
//                                    onSignupSuccess(res);
//                                }
//                                else if(result.equals(Constants.FAILED))
//                                {
//                                    hideProgress(progressDialog);
//                                    Toast.makeText(RegistrationActivity.this, "Failed to create "+ result.responseContent, Toast.LENGTH_LONG).show();
//                                }
//                                else if(result.equals(Constants.EXCEPTION))
//                                {
//                                    hideProgress(progressDialog);
//                                    Toast.makeText(RegistrationActivity.this, "Failed to create "+ result.responseContent, Toast.LENGTH_LONG).show();
//                                }
//
//                                // onSignupFailed();
//                            }
//                            catch (Exception e)
//                            {
//                                hideProgress(progressDialog);
//                                Toast.makeText(RegistrationActivity.this, "Failed to create", Toast.LENGTH_LONG).show();
//                            }
//                            hideProgress(progressDialog);
//                        }
//                    }, 2000);
        }
        catch (Exception e)
        {
            e.getMessage();
            Utils.hideProgress(dialog);
        }
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
        RegistrationActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    public void onSignupFailed() {
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String number = et_Mobile.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("Please enter User name");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (number.length() != 10) {
            et_Mobile.setError("Please  enter 10 Digit mobile number");
            valid = false;
        }
        if(et_EmpId.getText().toString().trim().equals(""))
        {
            et_EmpId.setError("Please enter emplyee id");
            valid = false;
        }

        if(spnrDesignation.getSelectedItem().toString().equalsIgnoreCase("Select Designation")){
            Toast.makeText(RegistrationActivity.this,"Please select Designation",Toast.LENGTH_SHORT).show();
            valid = false;
        }

//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            _emailText.setError("Please enter Valid Email");
//            valid = false;
//        } else {
//            _emailText.setError(null);
//        }

        if (password.isEmpty() || password.length() < 4  ) {
            _passwordText.setError("Please enter 4 digit PIN");
            valid = false;
        }
//        else {
//            String pin = dalUserMaster.getPin(password);
//            if(pin.equals(password)) {
//                _passwordText.setError("Please provide another pin");
//                valid = false;
//            }
//            else
//            _passwordText.setError(null);
//        }


        return valid;
    }

    public WebServiceDO callservice() throws Exception
    {
        WebServiceDO result = null;
        try {
            dialog.show();
            WebServiceDO webServiceDO = new WebServiceDO();
            JSONObject json = new JSONObject();
            json.put("name", _nameText.getText().toString().trim());
            json.put("email", _emailText.getText().toString().trim());
            json.put("mobile", et_Mobile.getText().toString().trim());
            json.put("designation", spnrDesignation.getSelectedItem().toString());
            json.put("empId", et_EmpId.getText().toString().trim());
            json.put("pin", _passwordText.getText().toString());
            String parameter = json.toString();
//                    URLEncoder.encode(json.toString(),"UTF-8");
            webServiceDO.result = Constants.SENT;
            new AsyncTask(RegistrationActivity.this, RegistrationActivity.this, Constants.URLBase + "" + "register", "POST", parameter).execute(webServiceDO);

        }
        catch (Exception e)
        {
            Utils.hideProgress(dialog);
            throw e;
        }
        return result;
    }


    @Override
    public void onTaskCompletes(WebServiceDO webServiceDO) {
        try {
            Utils.hideProgress(dialog);
            _signupButton.setEnabled(true);
            if(webServiceDO.result.equals(Constants.SUCCESS)) {
                JSONObject jsonObject = new JSONObject(webServiceDO.responseContent);
                if(jsonObject.getString("status").toUpperCase().equals("SUCESS"))
                {
                    onSignupSuccess();
                }
                else
                {
                    Utils.showalert(RegistrationActivity.this, "Alert",jsonObject.getString("errorDescription"));
                }
            }
            else
            {
                Utils.showalert(RegistrationActivity.this, "Alert",webServiceDO.responseContent);
            }

        }
        catch (Exception e)
        {
            e.getMessage();
        }
    }
}