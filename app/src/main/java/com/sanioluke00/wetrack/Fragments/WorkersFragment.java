package com.sanioluke00.wetrack.Fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;
import com.sanioluke00.wetrack.Adapters.AddEmployeesAdapter;
import com.sanioluke00.wetrack.DataModels.EmployeeModel;
import com.sanioluke00.wetrack.DataModels.Functions;
import com.sanioluke00.wetrack.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WorkersFragment extends Fragment {

    NestedScrollView workersfrag_mainlay;
    TextView workerfrag_manager_count, workerfrag_employee_count;
    View workerfrag_ml_lay, workerfrag_el_lay;
    RecyclerView workerfrag_ml, workerfrag_el;
    Button workerfrag_ml_more, workerfrag_el_more;
    TextView workerfrag_addmanager, workerfrag_addemployee;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;
    ArrayList<EmployeeModel> employees_list= new ArrayList<>();
    AddEmployeesAdapter addEmployeesAdapter;
    ArrayList<EmployeeModel> managers_list= new ArrayList<>();
    AddEmployeesAdapter addManagersAdapter;



    public WorkersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_workers, container, false);

        workersFragInits(v);

        workerfrag_addemployee.setOnClickListener(v1 -> addWorker("Employee"));

        workerfrag_addmanager.setOnClickListener(v1 -> addWorker("Manager"));

        String company_path_val = new Functions().getSharedPrefsValue(getContext(), "user_data", "company_path", "string", null);
        if(company_path_val!=null){
            Thread thread = new Thread() {
                @Override
                public void run() {
                    addEmployeesList(company_path_val,"TempEmployees", employees_list);
                    try { sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
                    addEmployeesList(company_path_val,"TempManagers", managers_list);
                }
            };

            thread.start();
        }

        return v;
    }

    private void addEmployeesList(String company_path_val, String dbname, ArrayList<EmployeeModel> arr) {
        db.collection(dbname)
                .whereEqualTo("company_path",db.document(company_path_val))
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot emp : task.getResult().getDocuments()){
                            if(dbname.equals("TempEmployees") || dbname.equals("Employees")){
                                EmployeeModel emp_model= new EmployeeModel(emp.getDocumentReference("company_path"),
                                        emp.getString("emp_name"), emp.getString("emp_contact"), emp.getString("emp_countrycode"),emp.getBoolean("emp_status"));
                                arr.add(emp_model);
                            }
                            else{
                                EmployeeModel manager_model= new EmployeeModel(emp.getDocumentReference("company_path"),
                                        emp.getString("manager_name"), emp.getString("manager_contact"), emp.getString("manager_countrycode"), emp.getBoolean("manager_status"));
                                arr.add(manager_model);
                            }
                        }
                        if(dbname.equals("TempEmployees") || dbname.equals("TempManagers")){
                            String mainDBName= dbname.equals("TempEmployees") ? "Employees" : "Managers";
                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    addEmployeesList(company_path_val,mainDBName, arr);
                                }
                            };

                            thread.start();
                        }
                        else{
                            if(dbname.equals("Employees")){
                                workerfrag_el.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                                addEmployeesAdapter = new AddEmployeesAdapter(getContext(), arr);
                                workerfrag_el.setAdapter(addEmployeesAdapter);
                            }
                            else if(dbname.equals("Managers")){
                                workerfrag_ml.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                                addManagersAdapter = new AddEmployeesAdapter(getContext(), arr);
                                workerfrag_ml.setAdapter(addManagersAdapter);
                            }
                        }
                    }
                });
    }

    private void updateAddEmployeeButtonStyle(String s, boolean b, @NonNull Button btn) {
        btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(s)));
        btn.setEnabled(b);
    }

    private void addWorker(String workerType) {
        BottomSheetDialog addemp_btm_dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetTheme);
        addemp_btm_dialog.setContentView(R.layout.addworkers_btm_dialog);
        addemp_btm_dialog.setCanceledOnTouchOutside(true);
        addemp_btm_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextInputLayout aw_btm_name = addemp_btm_dialog.findViewById(R.id.aw_btm_name);
        TextInputLayout aw_btm_phone = addemp_btm_dialog.findViewById(R.id.aw_btm_phone);
        CountryCodePicker aw_btm_countrycode= addemp_btm_dialog.findViewById(R.id.aw_btm_countrycode);
        Button aw_btm_add_btn = addemp_btm_dialog.findViewById(R.id.aw_btm_add_btn);

        aw_btm_add_btn.setEnabled(false);
        aw_btm_add_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DADADA")));

        aw_btm_name.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String emp_name = aw_btm_name.getEditText().getText().toString();
                String emp_contact = aw_btm_phone.getEditText().getText().toString();

                if (emp_name.length() <= 2) {
                    aw_btm_name.setError("Enter a name with more than 2 characters length !!");
                    aw_btm_name.requestFocus();
                    updateAddEmployeeButtonStyle("#DADADA", false, aw_btm_add_btn);
                } else
                    aw_btm_name.setError(null);

                if (emp_contact.length() >= 10)
                    updateAddEmployeeButtonStyle("#00A3FF", true, aw_btm_add_btn);
                else
                    updateAddEmployeeButtonStyle("#DADADA", false, aw_btm_add_btn);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        aw_btm_phone.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String emp_name = aw_btm_name.getEditText().getText().toString();
                String emp_contact = aw_btm_phone.getEditText().getText().toString();

                if (emp_contact.length() < 10) {
                    aw_btm_phone.setError("Enter a valid contact number !!");
                    aw_btm_phone.requestFocus();
                    updateAddEmployeeButtonStyle("#DADADA", false, aw_btm_add_btn);
                } else
                    aw_btm_phone.setError(null);

                if (emp_name.length() > 2)
                    updateAddEmployeeButtonStyle("#00A3FF", true, aw_btm_add_btn);
                else
                    updateAddEmployeeButtonStyle("#DADADA", false, aw_btm_add_btn);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        aw_btm_add_btn.setOnClickListener(v2 -> {
            String emp_name = aw_btm_name.getEditText().getText().toString();
            String emp_contact = aw_btm_phone.getEditText().getText().toString();
            String company_path_val = new Functions().getSharedPrefsValue(getContext(), "user_data", "company_path", "string", null);
            if(company_path_val!=null){
                DocumentReference company_path= db.document(company_path_val);

                String worker_name= (workerType.equals("Employee")) ? "emp_name" : "manager_name";
                String worker_contact= (workerType.equals("Employee")) ? "emp_contact" : "manager_contact";
                String worker_status= (workerType.equals("Employee")) ? "emp_status" : "manager_status";
                String worker_ccode= (workerType.equals("Employee")) ? "emp_countrycode" : "manager_countrycode";
                String workerDB= (workerType.equals("Employee")) ? "TempEmployees" : "TempManagers";
                String workerRes= (workerType.equals("Employee")) ? "employee" : "manager";
                String workerFail= (workerType.equals("Employee")) ? "an employee" : "a manager";

                Map<String, Object> worker_details = new HashMap<>();
                worker_details.put(worker_name, emp_name);
                worker_details.put(worker_contact, emp_contact);
                worker_details.put(worker_status, false);
                worker_details.put(worker_ccode, aw_btm_countrycode.getSelectedCountryCodeWithPlus());
                worker_details.put("company_path", company_path);

                db.collection(workerDB)
                        .add(worker_details)
                        .addOnCompleteListener(task -> {
                            addemp_btm_dialog.dismiss();
                            if (task.isSuccessful()) {
                                Snackbar.make(workersfrag_mainlay, "New " + workerRes + " added successfully.", Snackbar.LENGTH_SHORT).show();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_mainlay, new WorkersFragment()).commit();
                            } else {
                                Snackbar.make(workersfrag_mainlay, "Unable to add " + workerFail + " !! Please try again.", Snackbar.LENGTH_SHORT).show();
                            }
                        });
            }
            else{
                Snackbar.make(workersfrag_mainlay,"Unable to add new worker for the time being. Please try later !!", Snackbar.LENGTH_SHORT).show();
            }
        });

        addemp_btm_dialog.show();
    }

    private void workersFragInits(@NonNull View v) {
        workersfrag_mainlay = v.findViewById(R.id.workersfrag_mainlay);
        workerfrag_manager_count = v.findViewById(R.id.workerfrag_manager_count);
        workerfrag_employee_count = v.findViewById(R.id.workerfrag_employee_count);
        workerfrag_ml_lay = v.findViewById(R.id.workerfrag_ml_lay);
        workerfrag_el_lay = v.findViewById(R.id.workerfrag_el_lay);
        workerfrag_ml = v.findViewById(R.id.workerfrag_ml);
        workerfrag_el = v.findViewById(R.id.workerfrag_el);
        workerfrag_ml_more = v.findViewById(R.id.workerfrag_ml_more);
        workerfrag_el_more = v.findViewById(R.id.workerfrag_el_more);
        workerfrag_addmanager = v.findViewById(R.id.workerfrag_addmanager);
        workerfrag_addemployee = v.findViewById(R.id.workerfrag_addemployee);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }

}