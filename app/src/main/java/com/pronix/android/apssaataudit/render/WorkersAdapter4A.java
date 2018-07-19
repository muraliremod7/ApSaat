package com.pronix.android.apssaataudit.render;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.pronix.android.apssaataudit.Dal.DalDoorToDoorResults;
import com.pronix.android.apssaataudit.R;
import com.pronix.android.apssaataudit.models.Worker;
import com.pronix.android.apssaataudit.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class WorkersAdapter4A extends RecyclerView.Adapter<WorkersAdapter4A.MyViewHolder> {


    private List<Worker> workersList;

    private Context mCon;
    String subCategState="";
    SQLiteDatabase mDatabase;
    SessionManager sessionManager;
    DalDoorToDoorResults dalDoorToDoorResults;


    public WorkersAdapter4A(Context mCon, List<Worker> workersList) {
        this.workersList = workersList;
        this.mCon = mCon;
        this.sessionManager = new SessionManager(mCon);
        dalDoorToDoorResults = new DalDoorToDoorResults();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_format4a, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Worker worker = workersList.get(position);
        holder.serialNo.setText(worker.getSno());
        holder.wageSeekerId.setText(worker.getHousehold_code()+" / "+worker.getWorker_code());
        holder.fullname.setText(worker.getName());
        holder.postOfficeBankAccDetails.setText(worker.getAccount_no());
        holder.allWorkDetails.setText(worker.getWork_code()+" / "+worker.getWork_name()+" / "+worker.getWork_location());
        holder.workDuration.setText(worker.getFrom_date()+" to "+worker.getTo_date());
        holder.payOrderReleaseDate.setText(worker.getPayment_date());
        holder.musterId.setText(worker.getMuster_id());
        holder.noOfWorkingDays.setText(worker.getDays_worked());
        holder.amtToBePaid.setText(worker.getAmount_paid());
        holder.actualWorkedDays.setText(worker.getActualworkeddays());
        holder.actualAmtPaid.setText(worker.getActualamtpaid());
        holder.diffInAmtPaid.setText(worker.getDifferenceinamount());
        holder.isJobCardAvailSpinner.setSelection(worker.getIsjobcardavail().toUpperCase().equals("NO") ? 1 : 0, false);
        holder.isPassbookAvailSpinner.setSelection(worker.getIspassbookavail().toUpperCase().equals("NO") ? 1 : 0, false);
        holder.isPaylipIssuedSpinner.setSelection(worker.getIspayslipissued().toUpperCase().equals("NO") ? 1 : 0, false);
        holder.respPersonName.setText(worker.getResppersonname());
        holder.respPersonDesig.setText(worker.getResppesonjobdesg());


        holder.subCategorySpinner2.setSelection(getCatefgory3position(worker.getCategory3()), false);
        holder.comments.setText(worker.getComments());

        String[] maincategories = mCon.getResources().getStringArray(R.array.maincategories);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(mCon,android.R.layout.simple_list_item_1, maincategories);
        holder.mainCategorySpinner.setAdapter(adapter);


        holder.mainCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //loadsecondspinner
                String[] subcategories1;
                if(i==1){
                    subCategState = "Category1";
                    subcategories1 = mCon.getResources().getStringArray(R.array.Category1);
                } else  if(i==2){
                    subCategState = "Category2";
                    subcategories1 = mCon.getResources().getStringArray(R.array.Category2);
                } else  if(i==3){
                    subCategState = "Category3";
                    subcategories1 = mCon.getResources().getStringArray(R.array.Category3);
                } else{
                    subCategState = "Category4";
                    subcategories1 = mCon.getResources().getStringArray(R.array.Category4);
                }

                ArrayAdapter<String> adapter=new ArrayAdapter<String>(mCon,android.R.layout.simple_list_item_1, subcategories1);
                holder.subCategorySpinner1.setAdapter(adapter);
                holder.subCategorySpinner1.setSelection(getCatefgory2position(worker.getCategory2(), subCategState), false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        holder.subCategorySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //loadsecondspinner
                String[] subcategories2 = new String[0];
                subcategories2 = mCon.getResources().getStringArray(R.array.Noissue);
                if(subCategState.equalsIgnoreCase("Category1")){
                    if(i==1){
                        subcategories2 = mCon.getResources().getStringArray(R.array.IssueCategory11);
                    } else  if(i==2){
                        subcategories2 = mCon.getResources().getStringArray(R.array.IssueCategory12);
                    } else if(i==3){
                        subcategories2 = mCon.getResources().getStringArray(R.array.IssueCategory13);
                    }
                }else if(subCategState.equalsIgnoreCase("Category2")){
                    if(i==1){
                        subcategories2 = mCon.getResources().getStringArray(R.array.procedural1);
                    } else  if(i==2){
                        subcategories2 = mCon.getResources().getStringArray(R.array.procedural2);
                    } else  if(i==3){
                        subcategories2 = mCon.getResources().getStringArray(R.array.procedural3);
                    } else if(i==4){
                        subcategories2 = mCon.getResources().getStringArray(R.array.procedural4);
                    }
                }else if(subCategState.equalsIgnoreCase("Category3")){
                    if(i==1) {
                        subcategories2 = mCon.getResources().getStringArray(R.array.information1);
                    }
                }else if(subCategState.equalsIgnoreCase("Category4")){
                    if(i==1){
                        subcategories2 = mCon.getResources().getStringArray(R.array.grievances1);
                    } else if(i==2){
                        subcategories2 = mCon.getResources().getStringArray(R.array.grievances2);
                    }
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(mCon,android.R.layout.simple_list_item_1, subcategories2);
                holder.subCategorySpinner2.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        holder.mainCategorySpinner.setSelection(getCatefgory1position(worker.getCategory1()), false);


    }

    @Override
    public int getItemCount() {
        return workersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView serialNo, wageSeekerId,fullname,postOfficeBankAccDetails,allWorkDetails,workDuration,
                payOrderReleaseDate,musterId,noOfWorkingDays,amtToBePaid;
        EditText actualWorkedDays,actualAmtPaid,diffInAmtPaid,respPersonName,respPersonDesig,comments;
        Spinner isJobCardAvailSpinner,isPassbookAvailSpinner,isPaylipIssuedSpinner,mainCategorySpinner,subCategorySpinner1,subCategorySpinner2;
        Button updateWorkerDetails;

        public MyViewHolder(View view) {
            super(view);
            serialNo = view.findViewById(R.id.serialNo);
            wageSeekerId = view.findViewById(R.id.wageSeekerId);
            fullname = view.findViewById(R.id.fullname);
            postOfficeBankAccDetails = view.findViewById(R.id.postOfficeBankAccDetails);
            allWorkDetails = view.findViewById(R.id.allWorkDetails);
            workDuration = view.findViewById(R.id.workDuration);
            payOrderReleaseDate = view.findViewById(R.id.payOrderReleaseDate);
            musterId = view.findViewById(R.id.musterId);
            noOfWorkingDays = view.findViewById(R.id.noOfWorkingDays);
            amtToBePaid = view.findViewById(R.id.amtToBePaid);
            actualWorkedDays = view.findViewById(R.id.actualWorkedDays);
            actualAmtPaid = view.findViewById(R.id.actualAmtPaid);
            diffInAmtPaid = view.findViewById(R.id.diffInAmtPaid);
            isJobCardAvailSpinner = view.findViewById(R.id.isJobCardAvailSpinner);
            isPassbookAvailSpinner = view.findViewById(R.id.isPassbookAvailSpinner);
            isPaylipIssuedSpinner = view.findViewById(R.id.isPaylipIssuedSpinner);
            respPersonName = view.findViewById(R.id.respPersonName);
            respPersonDesig = view.findViewById(R.id.respPersonDesig);
            mainCategorySpinner = view.findViewById(R.id.mainCategorySpinner);
            subCategorySpinner1 = view.findViewById(R.id.subCategorySpinner1);
            subCategorySpinner2 = view.findViewById(R.id.subCategorySpinner2);
//            updateWorkerDetails = view.findViewById(R.id.updateWorkerDetails);
            comments = view.findViewById(R.id.comments);

        }

        public void setFilter(List<Worker> lrModels) {
            workersList = new ArrayList<>();
            workersList.addAll(lrModels);
            notifyDataSetChanged();
        }
    }

    public int getCatefgory3position(String value)
    {
        int pos = 0;
        if(value.toUpperCase().equals("NO ISSUE"))
            pos = 0;
        else if(value.toUpperCase().equals("BENAMI"))
            pos = 1;
        else if(value.toUpperCase().equals("EXCESS MAN"))
            pos = 2;
        else if(value.toUpperCase().equals("FAKE MUSTER ROLLS"))
            pos = 3;
        else if(value.toUpperCase().equals("ADDITION OF NAMES AND MANDAYS IN MUSTER AFTER CLOSING"))
            pos = 4;
        else if(value.toUpperCase().equals("DELETION OF NAMES AND MANDAYS IN MUSTER AFTER CLOSING"))
            pos = 5;
        return pos;
    }

    public int getCatefgory2position(String value, String category)
    {
        int pos = 0;

        if(category.equals("Category3"))
        {
            if(value.toUpperCase().equals("NO ISSUE"))
                pos = 0;
            else if(value.toUpperCase().equals("INFORMATION"))
                pos = 1;
        }
        else if(category.equals("CATEGORY3"))
        {
            if (value.toUpperCase().equals("NO ISSUE"))
                pos = 0;
            else if (value.toUpperCase().equals("JC RELATED"))
                pos = 1;
            else if (value.toUpperCase().equals("WORK RELATED"))
                pos = 2;
        }
        else {

            if (value.toUpperCase().equals("NO ISSUE"))
                pos = 0;
            else if (value.toUpperCase().equals("MUSTER RELATED"))
                pos = 1;
            else if (value.toUpperCase().equals("WORK RELATED"))
                pos = 2;
            else if (value.toUpperCase().equals("PAYMENT RELATED"))
                pos = 3;
            else if (value.toUpperCase().equals("JOB CARD RELATED"))
                pos = 4;
        }
        return pos;

    }


    public int getCatefgory1position(String value)
    {
        int pos = 0;

        if(value.toUpperCase().equals("NO ISSUE"))
            pos = 0;
        else if(value.toUpperCase().equals("FINANCIAL"))
            pos = 1;
        else if(value.toUpperCase().equals("PROCEDURAL"))
            pos = 2;
        else if(value.toUpperCase().equals("INFORMATION"))
            pos = 3;
        else if(value.toUpperCase().equals("GRIEVANCES"))
            pos = 4;
        return pos;
    }
}