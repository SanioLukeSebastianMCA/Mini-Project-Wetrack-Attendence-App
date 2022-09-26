package com.sanioluke00.wetrack.Activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.sanioluke00.wetrack.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private final String pass_regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$).{4,}$";
    LinearLayout log_maincontainer;
    TextView log_forgotpass_btn, log_signbtn;
    Button log_loginbtn;
    ImageButton log_googlesign_btn, log_facebooksign_btn;
    TextInputLayout log_phonenum, log_pass;
    private final TextWatcher inputtxtwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String phonenum_txt = Objects.requireNonNull(log_phonenum.getEditText()).getText().toString();
            String pass_txt = Objects.requireNonNull(log_pass.getEditText()).getText().toString();

            if (phonenum_txt.length() < 10) {
                log_phonenum.getEditText().setError("Enter a valid phone number !!");
                log_phonenum.getEditText().requestFocus();
                log_loginbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DADADA")));
                log_loginbtn.setEnabled(false);
            } else if (!pass_txt.matches(pass_regex)) {
                log_pass.getEditText().setError("Enter a valid password !!\nThe password should contain-\natleast one digit,\natleast one lower case letter\natleast one upper case letter,\natleast one special character like (@ # $ % ^ & + =),\nno whitespace allowed,\natleast 6 characters.");
                log_pass.getEditText().requestFocus();
                log_loginbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DADADA")));
                log_loginbtn.setEnabled(false);
            } else {
                log_phonenum.getEditText().clearFocus();
                log_pass.getEditText().clearFocus();

                log_phonenum.getEditText().setError("");
                log_pass.getEditText().setError("");

                log_loginbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00A3FF")));
                log_loginbtn.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    FirebaseAuth firebaseAuth;
    String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        log_maincontainer = findViewById(R.id.log_maincontainer);
        log_phonenum = findViewById(R.id.log_phonenum);
//        log_pass = findViewById(R.id.log_pass);
//        log_forgotpass_btn = findViewById(R.id.log_forgotpass_btn);
        log_signbtn = findViewById(R.id.log_signbtn);
        log_loginbtn = findViewById(R.id.log_loginbtn);
        log_googlesign_btn = findViewById(R.id.log_googlesign_btn);
        log_facebooksign_btn = findViewById(R.id.log_facebooksign_btn);

        firebaseAuth = FirebaseAuth.getInstance();

        log_phonenum.getEditText().addTextChangedListener(inputtxtwatcher);
        log_pass.getEditText().addTextChangedListener(inputtxtwatcher);

        log_loginbtn.setOnClickListener(v -> {

        });
    }
}