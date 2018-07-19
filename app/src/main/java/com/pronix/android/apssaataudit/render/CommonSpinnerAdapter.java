package com.pronix.android.apssaataudit.render;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pronix.android.apssaataudit.R;
import com.pronix.android.apssaataudit.models.DistrictDO;
import com.pronix.android.apssaataudit.models.MandalDO;
import com.pronix.android.apssaataudit.models.PanchayatDO;
import com.pronix.android.apssaataudit.pojo.Habitations;
import com.pronix.android.apssaataudit.pojo.Villages;

import java.util.ArrayList;

/**
 * Created by ravi on 1/17/2018.
 */

public class CommonSpinnerAdapter extends BaseAdapter {

    Context context;
    ArrayList list;

    public CommonSpinnerAdapter(Context context, ArrayList list)
    {
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.spinner_item, viewGroup, false);
        TextView tv_Item = v.findViewById(R.id.tv_Item);

        Object object = list.get(i);
        if(object instanceof DistrictDO)
        {
            tv_Item.setText(((DistrictDO) object).getDistrictName());
        }
        else if(object instanceof MandalDO)
        {
            tv_Item.setText(((MandalDO) object).getMandalName());
        }
        else if(object instanceof PanchayatDO)
        {
            tv_Item.setText(((PanchayatDO) object).getPanchayatName());
        }
        else if(object instanceof Villages)
        {
            tv_Item.setText(((Villages) object).getVillageName() + " / "+ ((Villages) object).getVillageCode());
        }
        else if(object instanceof Habitations)
        {
            if(!((Habitations) object).getHabitationName().equals(""))
                tv_Item.setText(((Habitations) object).getHabitationName() + " / " +((Habitations) object).getHabitationCode());
            else
                tv_Item.setText(((Habitations) object).getHabitationCode());
        }
        return v;
    }
}
