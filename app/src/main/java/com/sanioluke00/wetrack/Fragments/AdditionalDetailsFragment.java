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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    private final String email_regex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    RelativeLayout adddetailsfrag_mainlay;
    TextInputLayout reg_detailsfrag_fullname_txt, reg_detailsfrag_email_txt, reg_detailsfrag_companyname_txt;
    ImageButton reg_passfrag_back_btn;
    Button reg_passfrag_next_btn;
    private final TextWatcher detailsFullNameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String fullname_txt = reg_detailsfrag_fullname_txt.getEditText().getText().toString();
            String companyname_txt = reg_detailsfrag_companyname_txt.getEditText().getText().toString();
            String email_txt = reg_detailsfrag_email_txt.getEditText().getText().toString();

            if (fullname_txt.length() < 3) {
                reg_detailsfrag_fullname_txt.setError("Enter a valid name !!");
                reg_detailsfrag_fullname_txt.requestFocus();
                updateNextButtonStyle("#DADADA", false);
            } else {
                reg_detailsfrag_fullname_txt.setError(null);
            }

            if (companyname_txt.length() < 5) {
                updateNextButtonStyle("#DADADA", false);
            } else if (email_txt.length() > 0 && !email_txt.matches(email_regex)) {
                updateNextButtonStyle("#DADADA", false);
            } else {
                updateNextButtonStyle("#00A3FF", true);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
    private final TextWatcher detailsCompanyNameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String fullname_txt = reg_detailsfrag_fullname_txt.getEditText().getText().toString();
            String companyname_txt = reg_detailsfrag_companyname_txt.getEditText().getText().toString();
            String email_txt = reg_detailsfrag_email_txt.getEditText().getText().toString();


            if (companyname_txt.length() < 5) {
                reg_detailsfrag_companyname_txt.setError("Enter a valid company name !!");
                reg_detailsfrag_companyname_txt.requestFocus();
                updateNextButtonStyle("#DADADA", false);
            } else {
                reg_detailsfrag_companyname_txt.setError(null);
            }

            if (fullname_txt.length() < 3) {
                updateNextButtonStyle("#DADADA", false);
            } else if (email_txt.length() > 0 && !email_txt.matches(email_regex)) {
                updateNextButtonStyle("#DADADA", false);
            } else {
                updateNextButtonStyle("#00A3FF", true);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
    private final TextWatcher detailsEmailIdTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String fullname_txt = reg_detailsfrag_fullname_txt.getEditText().getText().toString();
            String companyname_txt = reg_detailsfrag_companyname_txt.getEditText().getText().toString();
            String email_txt = reg_detailsfrag_email_txt.getEditText().getText().toString();

            if (email_txt.length() > 0 && !email_txt.matches(email_regex)) {
                reg_detailsfrag_email_txt.setError("Enter a valid Email Address !!");
                reg_detailsfrag_email_txt.requestFocus();
                updateNextButtonStyle("#DADADA", false);
            } else {
                reg_detailsfrag_email_txt.setError(null);
            }

            if (fullname_txt.length() < 3) {
                updateNextButtonStyle("#DADADA", false);
            } else if (companyname_txt.length() < 5) {
                updateNextButtonStyle("#DADADA", false);
            } else {
                updateNextButtonStyle("#00A3FF", true);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
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

        adddetailsfrag_mainlay = v.findViewById(R.id.adddetailsfrag_mainlay);
        reg_detailsfrag_fullname_txt = v.findViewById(R.id.reg_detailsfrag_fullname_txt);
        reg_detailsfrag_email_txt = v.findViewById(R.id.reg_detailsfrag_email_txt);
        reg_detailsfrag_companyname_txt = v.findViewById(R.id.reg_detailsfrag_companyname_txt);
        reg_passfrag_back_btn = v.findViewById(R.id.reg_passfrag_back_btn);
        reg_passfrag_next_btn = v.findViewById(R.id.reg_passfrag_next_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

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

        reg_passfrag_next_btn.setOnClickListener(v1 -> {

            Dialog success_msgdialog = new Functions().createDialogBox(getActivity(), R.layout.loading_dialog, false);
            ImageView loading_verified_img = success_msgdialog.findViewById(R.id.loading_verified_img);
            ProgressBar loading_prog = success_msgdialog.findViewById(R.id.loading_progbar);
            TextView loading_txt = success_msgdialog.findViewById(R.id.loading_txt);
            loading_prog.setVisibility(View.VISIBLE);
            loading_verified_img.setVisibility(View.GONE);
            loading_txt.setText("Please wait...");
            success_msgdialog.show();

            String fullname_txt = reg_detailsfrag_fullname_txt.getEditText().getText().toString();
            String companyname_txt = reg_detailsfrag_companyname_txt.getEditText().getText().toString();

            String email_id = reg_detailsfrag_email_txt.getEditText().getText().toString();
            DocumentReference company_path = db.collection("Companies").document("pZresZFBMG8LbNF6PDEy");

            Map<String, Object> userdata = new HashMap<>();
            userdata.put("contact_number", phonenum);
            userdata.put("county_code", countrycode);
            userdata.put("full_name", fullname_txt);
            userdata.put("company_name", companyname_txt);
            userdata.put("email_id", email_id);
            userdata.put("company_path", company_path);
            userdata.put("isDetailsAdded", true);

            if (selecteduser.equals("Owners")) {
                db.collection("Owners")
                        .document(firebaseUser.getUid())
                        .set(userdata)
                        .addOnSuccessListener(unused -> {
                            new Functions().putSharedPrefsValue(getContext(), "user_data", "login_status", "boolean", true);
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
            } else if (selecteduser.equals("employee")) {

            } else if (selecteduser.equals("manager")) {
            }
        });

        reg_detailsfrag_fullname_txt.getEditText().addTextChangedListener(detailsFullNameTextWatcher);
        reg_detailsfrag_companyname_txt.getEditText().addTextChangedListener(detailsCompanyNameTextWatcher);
        reg_detailsfrag_email_txt.getEditText().addTextChangedListener(detailsEmailIdTextWatcher);
        return v;
    }

    private void updateNextButtonStyle(String s, boolean b) {
        reg_passfrag_next_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(s)));
        reg_passfrag_next_btn.setEnabled(b);
    }

    private void returnBackSigninFrag() {
        Snackbar.make(adddetailsfrag_mainlay, "Unable to load the server, Please try again !!", Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> getActivity().finish(), 1000);
    }
}