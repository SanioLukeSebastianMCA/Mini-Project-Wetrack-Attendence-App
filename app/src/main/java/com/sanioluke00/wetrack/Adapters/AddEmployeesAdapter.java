package com.sanioluke00.wetrack.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.sanioluke00.wetrack.DataModels.EmployeeModel;
import com.sanioluke00.wetrack.DataModels.EmployeeModel;
import com.sanioluke00.wetrack.R;

import java.util.ArrayList;

public class AddEmployeesAdapter extends RecyclerView.Adapter<AddEmployeesAdapter.ListViewHolder> {

    Context context;
    private ArrayList<EmployeeModel> employees_list;

    public AddEmployeesAdapter(Context context, ArrayList<EmployeeModel> employees_list){
        this.context= context;
        this.employees_list= employees_list;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.workers_item, null);
        return new ListViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.workeritem_wname.setText(employees_list.get(position).getEmp_name());
        holder.workeritem_wcontactno.setText(employees_list.get(position).getEmp_contact());
        if(employees_list.get(position).isEmp_status()){
            holder.workeritem_isverified.setText("Verified");
            holder.workeritem_isverified.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#51C300")));
        }
        else{
            holder.workeritem_isverified.setText("Pending");
            holder.workeritem_isverified.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFC800")));
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
//        Log.e("employee_list_size","The size of the array is "+employees_list.size());
        return employees_list.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView workeritem_wname, workeritem_wcontactno, workeritem_isverified;
        RelativeLayout workeritem_mainlay;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            workeritem_wname= itemView.findViewById(R.id.workeritem_wname);
            workeritem_wcontactno= itemView.findViewById(R.id.workeritem_wcontactno);
            workeritem_isverified= itemView.findViewById(R.id.workeritem_isverified);
            workeritem_mainlay= itemView.findViewById(R.id.workeritem_mainlay);
        }
    }
}
