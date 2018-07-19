package com.pronix.android.apssaataudit.render;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pronix.android.apssaataudit.R;
import com.pronix.android.apssaataudit.common.RecyclerViewClickListener;
import com.pronix.android.apssaataudit.models.PanchayatDO;

import java.util.ArrayList;

/**
 * Created by ravi on 2/22/2018.
 */

public class PanchaytListAdapter extends RecyclerView.Adapter<PanchaytListAdapter.ViewHolder> {
    ArrayList<PanchayatDO> arrayList;
    RecyclerViewClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;


        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.textView);

        }
    }

    public PanchaytListAdapter(ArrayList<PanchayatDO> list, RecyclerViewClickListener listener) {
        this.arrayList = list;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.panchayat_cardview, parent, false);

        final ViewHolder vh = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view,vh.getPosition());
            }
        });
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(arrayList.get(position).getPanchayatName());

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
