package com.pronix.android.apssaataudit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pronix.android.apssaataudit.Dal.DalDoorToDoorDetails;
import com.pronix.android.apssaataudit.common.Constants;
import com.pronix.android.apssaataudit.common.RecyclerViewClickListener;
import com.pronix.android.apssaataudit.models.PanchayatDO;
import com.pronix.android.apssaataudit.render.PanchaytListAdapter;

import java.util.ArrayList;

/**
 * Created by ravi on 2/23/2018.
 */

public class PanchayatListActivity extends BaseActivity implements RecyclerViewClickListener {
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    DalDoorToDoorDetails dalDoorToDoorDetails;
    ArrayList<PanchayatDO> panchayatDOArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_selectpanchayat);
            dalDoorToDoorDetails = new DalDoorToDoorDetails();
            panchayatDOArrayList = dalDoorToDoorDetails.getDownloadedPanchayats();
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            PanchaytListAdapter listAdapter = new PanchaytListAdapter(panchayatDOArrayList, this);
            recyclerView.setAdapter(listAdapter);


        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        try {
            Constants.PANCHAYATID = panchayatDOArrayList.get(position).getPanchayatCode();
            Constants.PANCHAYATNAME = panchayatDOArrayList.get(position).getPanchayatName();
            Constants.MANDALID = panchayatDOArrayList.get(position).getMandalCode();
            Constants.DISTRICTID = panchayatDOArrayList.get(position).getDistrictCode();
            Intent in = new Intent(this, AuditDetailsActivity.class);
            startActivity(in);
        } catch (Exception e) {
            e.getMessage();
        }

    }
}
