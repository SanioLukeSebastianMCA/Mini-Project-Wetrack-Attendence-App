package com.sanioluke00.wetrack.Activities;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.sanioluke00.wetrack.R;

public class RegitserActivity extends AppCompatActivity {

    TextInputLayout reg_email_id;
    CheckBox reg_terms_check;
    ImageButton reg_process_btn, reg_google_signbtn, reg_facebook_signbtn;
    TextView reg_login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regitser);

        reg_email_id = findViewById(R.id.reg_email_id);
        reg_terms_check = findViewById(R.id.reg_terms_check);
        reg_process_btn = findViewById(R.id.reg_process_btn);
        reg_google_signbtn = findViewById(R.id.reg_google_signbtn);
        reg_facebook_signbtn = findViewById(R.id.reg_facebook_signbtn);
        reg_login_btn = findViewById(R.id.reg_login_btn);


    }
}