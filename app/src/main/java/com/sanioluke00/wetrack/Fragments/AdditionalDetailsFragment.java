package com.sanioluke00.wetrack.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sanioluke00.wetrack.Activities.HomeActivity;
import com.sanioluke00.wetrack.DataModels.Functions;
import com.sanioluke00.wetrack.R;

import java.util.HashMap;
import java.util.Map;

public class AdditionalDetailsFragment extends Fragment {


    View adddetailsfrag_mainlay;
    TextInputLayout adddetailsfrag_pfullname, adddetailsfrag_pemailid;
    TextInputLayout adddetailsfrag_ccompanyname, adddetailsfrag_clocation, adddetailsfrag_cservices, reg_detailsfrag_cexperience, adddetailsfrag_ccontactnum;
    RadioGroup adddetailsfrag_pgender_radiogrp;
    RadioButton adddetailsfrag_pmale_btn, adddetailsfrag_pfemale_btn, adddetailsfrag_pother_btn;
    Button adddetailsfrag_nextbtn;
    TextView adddetailsfrag_gender_error;

    private final String email_regex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String phonenum, countrycode, selecteduser;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    public AdditionalDetailsFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_additional_details, container, false);

        viewInits(v);

        Log.e("gender_checked","The gender of the user is "+adddetailsfrag_pgender_radiogrp.getCheckedRadioButtonId());

        if (getArguments() != null) {
            phonenum = getArguments().getString("reg_phonenum");
            countrycode = getArguments().getString("reg_countrycode");
            selecteduser = getArguments().getString("reg_selecteduser");
            if (phonenum == null || countrycode == null) {
                returnBackSigninFrag();
            }
        } else {
            returnBackSigninFrag();
        }

        adddetailsfrag_nextbtn.setOnClickListener(v1 -> {

            Dialog success_msgdialog = new Functions().createDialogBox(getActivity(), R.layout.loading_dialog, false);
            ImageView loading_verified_img = success_msgdialog.findViewById(R.id.loading_verified_img);
            ProgressBar loading_prog = success_msgdialog.findViewById(R.id.loading_progbar);
            TextView loading_txt = success_msgdialog.findViewById(R.id.loading_txt);
            loading_prog.setVisibility(View.VISIBLE);
            loading_verified_img.setVisibility(View.GONE);
            loading_txt.setText("Please wait...");
            success_msgdialog.show();

            Map<String, Object> company_details = new HashMap<>();
            company_details.put("company_name", adddetailsfrag_ccompanyname.getEditText().getText().toString());
            company_details.put("company_location", adddetailsfrag_clocation.getEditText().getText().toString());
            company_details.put("company_services", adddetailsfrag_cservices.getEditText().getText().toString());
            company_details.put("company_year_exp", reg_detailsfrag_cexperience.getEditText().getText().toString());
            company_details.put("company_contactnum", adddetailsfrag_ccontactnum.getEditText().getText().toString());

            switch (selecteduser) {
                case "Owners": {
                    db.collection("Companies")
                            .add(company_details)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    int checked = adddetailsfrag_pgender_radiogrp.getCheckedRadioButtonId();
                                    String gender = (checked == adddetailsfrag_pmale_btn.getId()) ? "Male" : ((checked == adddetailsfrag_pfemale_btn.getId()) ? "Female" : "Others");
                                    DocumentReference company_path = db.collection("Companies").document(task.getResult().getId());

                                    Map<String, Object> userdata = new HashMap<>();
                                    userdata.put("full_name", adddetailsfrag_pfullname.getEditText().getText().toString());
                                    userdata.put("county_code", countrycode);
                                    userdata.put("contact_number", phonenum);
                                    userdata.put("gender", gender);
                                    userdata.put("email_id", adddetailsfrag_pemailid.getEditText().getText().toString());
                                    userdata.put("company_path", company_path);
                                    userdata.put("isDetailsAdded", true);

                                    db.collection("Owners")
                                            .document(firebaseUser.getUid())
                                            .set(userdata)
                                            .addOnSuccessListener(unused -> {
                                                new Functions().putSharedPrefsValue(getContext(), "user_data", "login_status", "boolean", true);
                                                new Functions().putSharedPrefsValue(getContext(), "user_data", "full_name", "string", adddetailsfrag_pfullname.getEditText().getText().toString());
                                                new Functions().putSharedPrefsValue(getContext(), "user_data", "country_code", "string", countrycode);
                                                new Functions().putSharedPrefsValue(getContext(), "user_data", "contact_num", "string", phonenum);
                                                new Functions().putSharedPrefsValue(getContext(), "user_data", "email_id", "string", adddetailsfrag_pemailid.getEditText().getText().toString());
                                                new Functions().putSharedPrefsValue(getContext(), "user_data", "company_path", "string", company_path.getPath());
                                                new Functions().putSharedPrefsValue(getContext(), "user_data", "isDetailsAdded", "boolean", true);

                                                // Log.e("company_path","The company path is : "+company_path);
                                                // Log.e("company_path","The company path is "+new Functions().getSharedPrefsValue(getContext(),"user_data","company_path","string",null));

                                                loading_txt.setText("Data Entered Successfully. Going to home page.");
                                                new Handler().postDelayed(() -> {
                                                    success_msgdialog.dismiss();
                                                    startActivity(new Intent(getContext(), HomeActivity.class));
                                                    getActivity().finish();
                                                }, 2000);

                                            }).addOnFailureListener(e -> {
                                                success_msgdialog.dismiss();
                                                Snackbar.make(adddetailsfrag_mainlay, "Data Entry failed !! Please try later.", Snackbar.LENGTH_SHORT).show();
                                            });
                                } else {
                                    success_msgdialog.dismiss();
                                    Snackbar.make(adddetailsfrag_mainlay, "Data Entry failed !! Please try later.", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                    break;
                }

                case "employee": {
                    break;
                }

                case "manager": {
                    break;
                }
            }
        });

        adddetailsfrag_pfullname.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String fullname_txt = adddetailsfrag_pfullname.getEditText().getText().toString();
                String email_txt = adddetailsfrag_pemailid.getEditText().getText().toString();

                String companyname_txt = adddetailsfrag_ccompanyname.getEditText().getText().toString();
                String clocation_txt = adddetailsfrag_clocation.getEditText().getText().toString();
                String cservice_txt = adddetailsfrag_cservices.getEditText().getText().toString();
                String cyearsexp = reg_detailsfrag_cexperience.getEditText().getText().toString();
                String ccontactnum = adddetailsfrag_ccontactnum.getEditText().getText().toString();
                int exp_num = cyearsexp.equals("") ? 0 : Integer.parseInt(cyearsexp);

                if (fullname_txt.length() < 3) {
                    adddetailsfrag_pfullname.setError("Enter more than 4 characters !!");
                    adddetailsfrag_pfullname.requestFocus();
                    updateNextButtonStyle("#DADADA", false);
                } else
                    adddetailsfrag_pfullname.setError(null);

                if (fullname_txt.length() >= 3 && email_txt.matches(email_regex) && companyname_txt.length() >= 5 &&
                        clocation_txt.length() >= 3 && cservice_txt.length() >= 3 && exp_num > 0 && ccontactnum.length() >= 10) {
                    updateNextButtonStyle("#00A3FF", true);
                } else {
                    updateNextButtonStyle("#DADADA", false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        adddetailsfrag_pemailid.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String fullname_txt = adddetailsfrag_pfullname.getEditText().getText().toString();
                String email_txt = adddetailsfrag_pemailid.getEditText().getText().toString();

                String companyname_txt = adddetailsfrag_ccompanyname.getEditText().getText().toString();
                String clocation_txt = adddetailsfrag_clocation.getEditText().getText().toString();
                String cservice_txt = adddetailsfrag_cservices.getEditText().getText().toString();
                String cyearsexp = reg_detailsfrag_cexperience.getEditText().getText().toString();
                String ccontactnum = adddetailsfrag_ccontactnum.getEditText().getText().toString();
                int exp_num = cyearsexp.equals("") ? 0 : Integer.parseInt(cyearsexp);

                if (email_txt.length() > 0 && !email_txt.matches(email_regex)) {
                    adddetailsfrag_pemailid.setError("Enter more than 4 characters !!");
                    adddetailsfrag_pemailid.requestFocus();
                    updateNextButtonStyle("#DADADA", false);
                } else
                    adddetailsfrag_pemailid.setError(null);

                if (fullname_txt.length() >= 3 && email_txt.matches(email_regex) && companyname_txt.length() >= 5 &&
                        clocation_txt.length() >= 3 && cservice_txt.length() >= 3 && exp_num > 0 && ccontactnum.length() >= 10) {
                    updateNextButtonStyle("#00A3FF", true);
                } else {
                    updateNextButtonStyle("#DADADA", false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        adddetailsfrag_pgender_radiogrp.setOnCheckedChangeListener((radioGroup, i) -> {
            String fullname_txt = adddetailsfrag_pfullname.getEditText().getText().toString();
            String email_txt = adddetailsfrag_pemailid.getEditText().getText().toString();

            String companyname_txt = adddetailsfrag_ccompanyname.getEditText().getText().toString();
            String clocation_txt = adddetailsfrag_clocation.getEditText().getText().toString();
            String cservice_txt = adddetailsfrag_cservices.getEditText().getText().toString();
            String cyearsexp = reg_detailsfrag_cexperience.getEditText().getText().toString();
            String ccontactnum = adddetailsfrag_ccontactnum.getEditText().getText().toString();
            int exp_num = cyearsexp.equals("") ? 0 : Integer.parseInt(cyearsexp);

            if(adddetailsfrag_pgender_radiogrp.getCheckedRadioButtonId()==-1){
                adddetailsfrag_gender_error.setVisibility(View.VISIBLE);
                updateNextButtonStyle("#DADADA", false);
            }
            else{
                updateNextButtonStyle("#00A3FF", true);
                adddetailsfrag_gender_error.setVisibility(View.GONE);
            }

            if (fullname_txt.length() >= 3 && email_txt.matches(email_regex) && companyname_txt.length() >= 5 &&
                    clocation_txt.length() >= 3 && cservice_txt.length() >= 3 && exp_num > 0 && ccontactnum.length() >= 10) {
                updateNextButtonStyle("#00A3FF", true);
            } else {
                updateNextButtonStyle("#DADADA", false);
            }
        });


        adddetailsfrag_ccompanyname.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String fullname_txt = adddetailsfrag_pfullname.getEditText().getText().toString();
                String email_txt = adddetailsfrag_pemailid.getEditText().getText().toString();

                String companyname_txt = adddetailsfrag_ccompanyname.getEditText().getText().toString();
                String clocation_txt = adddetailsfrag_clocation.getEditText().getText().toString();
                String cservice_txt = adddetailsfrag_cservices.getEditText().getText().toString();
                String cyearsexp = reg_detailsfrag_cexperience.getEditText().getText().toString();
                String ccontactnum = adddetailsfrag_ccontactnum.getEditText().getText().toString();
                int exp_num = cyearsexp.equals("") ? 0 : Integer.parseInt(cyearsexp);

                if (companyname_txt.length() < 5) {
                    adddetailsfrag_ccompanyname.setError("Enter more than 4 characters !!");
                    adddetailsfrag_ccompanyname.requestFocus();
                    updateNextButtonStyle("#DADADA", false);
                } else
                    adddetailsfrag_ccompanyname.setError(null);

                if (fullname_txt.length() >= 3 && email_txt.matches(email_regex) && getGenderChecked() && companyname_txt.length() >= 5 &&
                        clocation_txt.length() >= 3 && cservice_txt.length() >= 3 && exp_num > 0 && ccontactnum.length() >= 10) {
                    updateNextButtonStyle("#00A3FF", true);
                } else {
                    updateNextButtonStyle("#DADADA", false);
                    if(adddetailsfrag_pgender_radiogrp.getCheckedRadioButtonId()==-1){
                        adddetailsfrag_gender_error.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        adddetailsfrag_clocation.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String fullname_txt = adddetailsfrag_pfullname.getEditText().getText().toString();
                String email_txt = adddetailsfrag_pemailid.getEditText().getText().toString();

                String companyname_txt = adddetailsfrag_ccompanyname.getEditText().getText().toString();
                String clocation_txt = adddetailsfrag_clocation.getEditText().getText().toString();
                String cservice_txt = adddetailsfrag_cservices.getEditText().getText().toString();
                String cyearsexp = reg_detailsfrag_cexperience.getEditText().getText().toString();
                String ccontactnum = adddetailsfrag_ccontactnum.getEditText().getText().toString();
                int exp_num = cyearsexp.equals("") ? 0 : Integer.parseInt(cyearsexp);

                if (clocation_txt.length() < 3) {
                    adddetailsfrag_clocation.setError("Enter more than 2 characters !!");
                    adddetailsfrag_clocation.requestFocus();
                    updateNextButtonStyle("#DADADA", false);
                } else
                    adddetailsfrag_clocation.setError(null);

                if (fullname_txt.length() >= 3 && email_txt.matches(email_regex) && getGenderChecked() && companyname_txt.length() >= 5 &&
                        clocation_txt.length() >= 3 && cservice_txt.length() >= 3 && exp_num > 0 && ccontactnum.length() >= 10) {
                    updateNextButtonStyle("#00A3FF", true);
                } else {
                    updateNextButtonStyle("#DADADA", false);
                    if(adddetailsfrag_pgender_radiogrp.getCheckedRadioButtonId()==-1){
                        adddetailsfrag_gender_error.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        adddetailsfrag_cservices.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String fullname_txt = adddetailsfrag_pfullname.getEditText().getText().toString();
                String email_txt = adddetailsfrag_pemailid.getEditText().getText().toString();

                String companyname_txt = adddetailsfrag_ccompanyname.getEditText().getText().toString();
                String clocation_txt = adddetailsfrag_clocation.getEditText().getText().toString();
                String cservice_txt = adddetailsfrag_cservices.getEditText().getText().toString();
                String cyearsexp = reg_detailsfrag_cexperience.getEditText().getText().toString();
                String ccontactnum = adddetailsfrag_ccontactnum.getEditText().getText().toString();
                int exp_num = cyearsexp.equals("") ? 0 : Integer.parseInt(cyearsexp);

                if (cservice_txt.length() < 3) {
                    adddetailsfrag_cservices.setError("Enter more than 2 characters !!");
                    adddetailsfrag_cservices.requestFocus();
                    updateNextButtonStyle("#DADADA", false);
                } else
                    adddetailsfrag_cservices.setError(null);

                if (fullname_txt.length() >= 3 && email_txt.matches(email_regex) && getGenderChecked() && companyname_txt.length() >= 5 &&
                        clocation_txt.length() >= 3 && cservice_txt.length() >= 3 && exp_num > 0 && ccontactnum.length() >= 10) {
                    updateNextButtonStyle("#00A3FF", true);
                } else {
                    updateNextButtonStyle("#DADADA", false);
                    if(adddetailsfrag_pgender_radiogrp.getCheckedRadioButtonId()==-1){
                        adddetailsfrag_gender_error.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        reg_detailsfrag_cexperience.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String fullname_txt = adddetailsfrag_pfullname.getEditText().getText().toString();
                String email_txt = adddetailsfrag_pemailid.getEditText().getText().toString();

                String companyname_txt = adddetailsfrag_ccompanyname.getEditText().getText().toString();
                String clocation_txt = adddetailsfrag_clocation.getEditText().getText().toString();
                String cservice_txt = adddetailsfrag_cservices.getEditText().getText().toString();
                String cyearsexp = reg_detailsfrag_cexperience.getEditText().getText().toString();
                String ccontactnum = adddetailsfrag_ccontactnum.getEditText().getText().toString();
                int exp_num = cyearsexp.equals("") ? 0 : Integer.parseInt(cyearsexp);

                if (exp_num <= 0) {
                    reg_detailsfrag_cexperience.setError("Enter a valid year of experience of atleast 1 year !!");
                    reg_detailsfrag_cexperience.requestFocus();
                    updateNextButtonStyle("#DADADA", false);
                } else
                    reg_detailsfrag_cexperience.setError(null);

                if (fullname_txt.length() >= 3 && email_txt.matches(email_regex) && getGenderChecked() && companyname_txt.length() >= 5 &&
                        clocation_txt.length() >= 3 && cservice_txt.length() >= 3 && exp_num > 0 && ccontactnum.length() >= 10) {
                    updateNextButtonStyle("#00A3FF", true);
                } else {
                    updateNextButtonStyle("#DADADA", false);
                    if(adddetailsfrag_pgender_radiogrp.getCheckedRadioButtonId()==-1){
                        adddetailsfrag_gender_error.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        adddetailsfrag_ccontactnum.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String fullname_txt = adddetailsfrag_pfullname.getEditText().getText().toString();
                String email_txt = adddetailsfrag_pemailid.getEditText().getText().toString();

                String companyname_txt = adddetailsfrag_ccompanyname.getEditText().getText().toString();
                String clocation_txt = adddetailsfrag_clocation.getEditText().getText().toString();
                String cservice_txt = adddetailsfrag_cservices.getEditText().getText().toString();
                String cyearsexp = reg_detailsfrag_cexperience.getEditText().getText().toString();
                String ccontactnum = adddetailsfrag_ccontactnum.getEditText().getText().toString();
                int exp_num = cyearsexp.equals("") ? 0 : Integer.parseInt(cyearsexp);

                if (ccontactnum.length() < 10) {
                    adddetailsfrag_ccontactnum.setError("Enter valid contact number !!");
                    adddetailsfrag_ccontactnum.requestFocus();
                    updateNextButtonStyle("#DADADA", false);
                } else
                    adddetailsfrag_ccontactnum.setError(null);

                if (fullname_txt.length() >= 3 && email_txt.matches(email_regex) && getGenderChecked() && companyname_txt.length() >= 5 &&
                        ccontactnum.length() == 10 && clocation_txt.length() >= 3 && cservice_txt.length() >= 3 && exp_num > 0) {
                    updateNextButtonStyle("#00A3FF", true);
                } else {
                    updateNextButtonStyle("#DADADA", false);
                    if(adddetailsfrag_pgender_radiogrp.getCheckedRadioButtonId()==-1){
                        adddetailsfrag_gender_error.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return v;
    }

    private boolean getGenderChecked(){
        int checked= adddetailsfrag_pgender_radiogrp.getCheckedRadioButtonId();
        int male= adddetailsfrag_pmale_btn.getId();
        int female= adddetailsfrag_pfemale_btn.getId();
        int others= adddetailsfrag_pother_btn.getId();
        return checked == male || (checked == female || (checked == others));
    }

    private void viewInits(@NonNull View v) {
        adddetailsfrag_mainlay = v.findViewById(R.id.adddetailsfrag_mainlay);
        adddetailsfrag_pfullname = v.findViewById(R.id.adddetailsfrag_pfullname);
        adddetailsfrag_pemailid = v.findViewById(R.id.adddetailsfrag_pemailid);

        adddetailsfrag_ccompanyname = v.findViewById(R.id.adddetailsfrag_ccompanyname);
        adddetailsfrag_clocation = v.findViewById(R.id.adddetailsfrag_clocation);
        adddetailsfrag_cservices = v.findViewById(R.id.adddetailsfrag_cservices);
        reg_detailsfrag_cexperience = v.findViewById(R.id.reg_detailsfrag_cexperience);
        adddetailsfrag_ccontactnum = v.findViewById(R.id.adddetailsfrag_ccontactnum);

        adddetailsfrag_pgender_radiogrp = v.findViewById(R.id.adddetailsfrag_pgender_radiogrp);
        adddetailsfrag_pmale_btn = v.findViewById(R.id.adddetailsfrag_pmale_btn);
        adddetailsfrag_pfemale_btn = v.findViewById(R.id.adddetailsfrag_pfemale_btn);
        adddetailsfrag_pother_btn = v.findViewById(R.id.adddetailsfrag_pother_btn);

        adddetailsfrag_nextbtn = v.findViewById(R.id.adddetailsfrag_nextbtn);
        adddetailsfrag_gender_error = v.findViewById(R.id.adddetailsfrag_gender_error);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }

    private void updateNextButtonStyle(String s, boolean b) {
        adddetailsfrag_nextbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(s)));
        adddetailsfrag_nextbtn.setEnabled(b);
    }

    private void returnBackSigninFrag() {
        Snackbar.make(adddetailsfrag_mainlay, "Unable to load the server, Please try again !!", Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> getActivity().finish(), 1000);
    }
}