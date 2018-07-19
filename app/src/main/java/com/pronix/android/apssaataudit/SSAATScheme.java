package com.pronix.android.apssaataudit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pronix.android.apssaataudit.Dal.DalUserMaster;
import com.pronix.android.apssaataudit.common.Constants;


/**
 * Created by ravi on 1/2/2018.
 */

public class SSAATScheme extends BaseActivity implements View.OnClickListener{
    RelativeLayout rl_MGNREGA;
    TextView tv_ProfileName;
    TextView tv_MGNREGA;
    DalUserMaster dalUserMaster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            getLayoutInflater().inflate(R.layout.activity_ssaatscheme, frame_base);
            dalUserMaster = new DalUserMaster();
            dalUserMaster.getUserDetails(Constants.PIN, Constants.USERORMOBILE);
            initializeControls();
            setLaunguageTexts();
    }

    public void initializeControls()
    {
        rl_MGNREGA = (RelativeLayout) findViewById(R.id.rl_MGNREGA);
        rl_MGNREGA.setOnClickListener(this);
        tv_MGNREGA = (TextView) findViewById(R.id.tv_MGNREGA);
        tv_ProfileName = (TextView) findViewById(R.id.tv_ProfileName);
        tv_ProfileName.setText(Constants.userMasterDO.designation + " : " + Constants.userMasterDO.userName);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.rl_MGNREGA:
                Intent in = new Intent();
                in.setClass(this, MainActivity.class);
                startActivity(in);
                break;
        }
    }

    public void setLaunguageTexts()
     {
//         tv_MGNREGA.setText(R.string.MGNREGA);

    }

    @Override
    public void onBackPressed() {
        logout();
    }

    public void logout()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("SSAAT");
        alertDialog.setMessage("Do you want to Logout Now.. ?");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                sessionManager.logoutUser();
                Intent intent = new Intent(SSAATScheme.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }
}
