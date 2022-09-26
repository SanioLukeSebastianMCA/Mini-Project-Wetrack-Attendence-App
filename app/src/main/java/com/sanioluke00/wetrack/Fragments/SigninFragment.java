package com.sanioluke00.wetrack.Fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;
import com.sanioluke00.wetrack.R;

import java.util.Objects;

public class SigninFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String selected_user;
    private RelativeLayout signinfrag_maincontainer;
    private LinearLayout signinfrag_contentcontainer;
    private TextInputLayout signinfrag_phonenum;
    private CheckBox signinfrag_termscheck;
    private ImageButton signinfrag_proceed_btn;
    private final TextWatcher inputPhoneTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String phonenum_txt = Objects.requireNonNull(signinfrag_phonenum.getEditText()).getText().toString();

            if (phonenum_txt.length() < 10) {
                signinfrag_phonenum.setError("Enter your phone number !!");
                signinfrag_phonenum.requestFocus();
                updateProceedBtnStyle("#DADADA", false);
            } else if (!signinfrag_termscheck.isChecked()) {
                signinfrag_phonenum.setError(null);
                updateProceedBtnStyle("#DADADA", false);
            } else {
                updateProceedBtnStyle("#00A3FF", true);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
    private CountryCodePicker signinfrag_ccpicker;

    public SigninFragment(String selected_user) {
        this.selected_user = selected_user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signin, container, false);

        signinfrag_maincontainer = v.findViewById(R.id.signinfrag_maincontainer);
        signinfrag_contentcontainer = v.findViewById(R.id.signinfrag_contentcontainer);
        signinfrag_phonenum = v.findViewById(R.id.signinfrag_phonenum);
        signinfrag_termscheck = v.findViewById(R.id.signinfrag_termscheck);
        signinfrag_proceed_btn = v.findViewById(R.id.signinfrag_proceed_btn);
        signinfrag_ccpicker = v.findViewById(R.id.signinfrag_ccpicker);

        signinfrag_proceed_btn.setEnabled(false);
        signinfrag_phonenum.getEditText().addTextChangedListener(inputPhoneTextWatcher);

        signinfrag_termscheck.setOnCheckedChangeListener((compoundButton, b) -> {

            String phonenum_txt = Objects.requireNonNull(signinfrag_phonenum.getEditText()).getText().toString();
            if (!b) {
                Snackbar.make(signinfrag_maincontainer, "Please read the terms and conditions and check to proceed !!", Snackbar.LENGTH_SHORT).show();
                updateProceedBtnStyle("#DADADA", false);
            } else {
                if (phonenum_txt.length() == 10)
                    updateProceedBtnStyle("#00A3FF", true);
                else {
                    updateProceedBtnStyle("#DADADA", false);
                }
            }
        });

        signinfrag_proceed_btn.setOnClickListener(v1 -> {

            OtpPhoneFragment OtpPhoneFragment = new OtpPhoneFragment();
            Bundle args = new Bundle();
            args.putString("reg_phonenum", signinfrag_phonenum.getEditText().getText().toString());
            args.putString("reg_countrycode", signinfrag_ccpicker.getSelectedCountryCodeWithPlus());
            args.putString("reg_selecteduser", selected_user);
            OtpPhoneFragment.setArguments(args);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            transaction.replace(R.id.signin_maincontainer, OtpPhoneFragment);
            transaction.commit();
        });

        return v;
    }

    private void updateProceedBtnStyle(String s, boolean b) {
        signinfrag_proceed_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(s)));
        signinfrag_proceed_btn.setEnabled(b);
    }
}