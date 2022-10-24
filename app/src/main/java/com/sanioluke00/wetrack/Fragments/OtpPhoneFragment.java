package com.sanioluke00.wetrack.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sanioluke00.wetrack.Activities.HomeActivity;
import com.sanioluke00.wetrack.DataModels.Functions;
import com.sanioluke00.wetrack.R;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

@SuppressWarnings("all")
public class OtpPhoneFragment extends Fragment {

    private String phonenum, countrycode, selecteduser;
    private RelativeLayout otp_main_container;
    private LinearLayout otp_resend_lay, otp_countdown_resendotp_lay;
    private TextView otp_resentotp_btn;
    private ImageButton otp_proceed_btn, reg_passfrag_back_btn;
    private OtpTextView otp_otpbox;
    private ProgressBar otp_progressbar;
    private TextView otp_subheading_txt, otp_countdown_time;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    private long mTimeLeftInMillis = 30000;
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {
                    final String code = credential.getSmsCode();
                    if (code != null) {
                        verifyCode(code);
                    } else {
                        Snackbar.make(otp_main_container, "OTP not received !!", Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Snackbar.make(otp_main_container, "Verification Failed !!", Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onCodeSent(@NonNull String verificationId,
                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {

                    super.onCodeSent(verificationId, token);
                    otp_resend_lay.setVisibility(View.GONE);
                    otp_progressbar.setVisibility(View.GONE);
                    otp_countdown_resendotp_lay.setVisibility(View.VISIBLE);
                    otpResendDelayCountDown();
                    Snackbar.make(otp_main_container, "An OTP has been sent to your Mobile Number.", Snackbar.LENGTH_SHORT).show();
                    mVerificationId = verificationId;
                    resendToken = token;

                }
            };

    public OtpPhoneFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_otp_phone, container, false);

        otp_main_container = v.findViewById(R.id.otp_main_container);
        otp_resend_lay = v.findViewById(R.id.otp_resend_lay);
        otp_countdown_resendotp_lay = v.findViewById(R.id.otp_countdown_resendotp_lay);
        otp_countdown_time = v.findViewById(R.id.otp_countdown_time);
        otp_otpbox = v.findViewById(R.id.otp_otpbox);
        otp_progressbar = v.findViewById(R.id.otp_progressbar);
        otp_resentotp_btn = v.findViewById(R.id.otp_resentotp_btn);
        reg_passfrag_back_btn = v.findViewById(R.id.reg_passfrag_back_btn);
        otp_proceed_btn = v.findViewById(R.id.otp_proceed_btn);
        otp_subheading_txt = v.findViewById(R.id.otp_subheading_txt);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        updateCountDownText();

        if (getArguments() != null) {
            phonenum = getArguments().getString("reg_phonenum");
            countrycode = getArguments().getString("reg_countrycode");
            selecteduser = getArguments().getString("reg_selecteduser");
            otp_subheading_txt.setText(Html.fromHtml("Please enter the veritication code send to <b>" + countrycode + "-" + phonenum + "</b>"));
            if (phonenum == null || countrycode == null) {
                returnBackSigninFrag();
            }
        } else {
            returnBackSigninFrag();
        }

        reg_passfrag_back_btn.setOnClickListener(v1 -> {
            getFragmentManager().popBackStackImmediate();
        });

        otp_otpbox.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                otp_proceed_btn.setEnabled(false);
                otp_proceed_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DADADA")));
            }

            @Override
            public void onOTPComplete(String otp) {
                otp_proceed_btn.setEnabled(true);
                otp_proceed_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00A3FF")));
            }
        });

        otpSendFunction();

        otp_resentotp_btn.setOnClickListener(v1 -> resendVerificationCode(countrycode + phonenum, resendToken));

        otp_proceed_btn.setOnClickListener(v1 -> verifyCode(otp_otpbox.getOTP()));

        return v;
    }

    private void otpSendFunction() {
        otp_resend_lay.setVisibility(View.GONE);
        otp_progressbar.setVisibility(View.VISIBLE);
        otp_countdown_resendotp_lay.setVisibility(View.GONE);
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(countrycode + phonenum)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(getActivity())
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        otp_resend_lay.setVisibility(View.GONE);
        otp_progressbar.setVisibility(View.VISIBLE);
        otp_countdown_resendotp_lay.setVisibility(View.GONE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, getActivity(), mCallbacks, token);
    }

    @SuppressLint("SetTextI18n")
    private void verifyCode(String code) {
        otp_otpbox.setOTP(code);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            otp_progressbar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                Snackbar.make(otp_main_container, "OTP Verification successfully complete.", Snackbar.LENGTH_SHORT).show();
                firebaseUser = task.getResult().getUser();

                Dialog processUserAccDialog = new Functions().createDialogBox(getActivity(), R.layout.loading_dialog, false);
                ImageView loading_verified_img = processUserAccDialog.findViewById(R.id.loading_verified_img);
                ProgressBar loading_prog = processUserAccDialog.findViewById(R.id.loading_progbar);
                TextView loading_txt = processUserAccDialog.findViewById(R.id.loading_txt);

                new Handler().postDelayed(() -> {
                    if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                        addUserData();
                        proceedForAddingDetails("Account Created Successfully.", processUserAccDialog, loading_prog, loading_verified_img, loading_txt);
                    } else {
                        processUserDataStatus(processUserAccDialog, loading_prog, loading_verified_img, loading_txt);
                    }
                }, 1000);
                processUserAccDialog.show();
            } else {
                Snackbar.make(otp_main_container, "Verification failed !!", Snackbar.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            otp_resend_lay.setVisibility(View.VISIBLE);
            otp_progressbar.setVisibility(View.GONE);
            otp_countdown_resendotp_lay.setVisibility(View.GONE);
        });
    }

    private void processUserDataStatus(Dialog processUserAccDialog, ProgressBar loading_prog, ImageView loading_verified_img, TextView loading_txt) {
        Log.e("page_error", "processUserDataStatus start....");
        db.collection(selecteduser)
                .document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        boolean isDetailsAdded = (task1.getResult().contains("isDetailsAdded") ? task1.getResult().getBoolean("isDetailsAdded") : false);
                        if (isDetailsAdded) {
                            loading_prog.setVisibility(View.VISIBLE);
                            loading_verified_img.setVisibility(View.GONE);
                            loading_txt.setText("Login Successful.\nRedirecting to Home Page...");

                            DocumentSnapshot userdata= task1.getResult();
                            new Functions().putSharedPrefsValue(getContext(), "user_data", "login_status", "boolean", true);
                            new Functions().putSharedPrefsValue(getContext(), "user_data", "full_name", "string", userdata.getString("full_name"));
                            new Functions().putSharedPrefsValue(getContext(), "user_data", "country_code", "string", userdata.getString("county_code"));
                            new Functions().putSharedPrefsValue(getContext(), "user_data", "contact_num", "string", userdata.getString("contact_number"));
                            new Functions().putSharedPrefsValue(getContext(), "user_data", "email_id", "string", userdata.getString("email_id"));
                            new Functions().putSharedPrefsValue(getContext(), "user_data", "company_path", "string", userdata.getDocumentReference("company_path").getPath());
                            new Functions().putSharedPrefsValue(getContext(), "user_data", "isDetailsAdded", "boolean", userdata.getBoolean("isDetailsAdded"));

                            new Handler().postDelayed(() -> {
                                processUserAccDialog.dismiss();
                                new Functions().putSharedPrefsValue(getContext(), "user_data", "login_status", "boolean", true);
                                startActivity(new Intent(getContext(), HomeActivity.class));
                                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            }, 1000);
                        } else {
                            proceedForAddingDetails("Login Successful.\nPlease add personal and other details before proceeding...", processUserAccDialog, loading_prog, loading_verified_img, loading_txt);
                        }
                    } else {
                        processUserDataStatus(processUserAccDialog, loading_prog, loading_verified_img, loading_txt);
                    }
                });
        Log.e("page_error", "processUserDataStatus end....");
    }

    private void proceedForAddingDetails(String loading_string, Dialog processUserAccDialog, @NonNull ProgressBar loading_prog, @NonNull ImageView loading_verified_img, @NonNull TextView loading_txt) {
        Log.e("page_error", "proceedForAddingDetails start....");
        loading_prog.setVisibility(View.GONE);
        loading_verified_img.setVisibility(View.VISIBLE);
        loading_txt.setText(loading_string);
        AdditionalDetailsFragment additionalDetailsFragment = new AdditionalDetailsFragment();
        Bundle args = new Bundle();
        args.putString("reg_phonenum", phonenum);
        args.putString("reg_countrycode", countrycode);
        args.putString("reg_selecteduser", selecteduser);
        additionalDetailsFragment.setArguments(args);

        new Handler().postDelayed(() -> {
            processUserAccDialog.dismiss();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            transaction.replace(R.id.signin_maincontainer, additionalDetailsFragment);
            transaction.commit();
        }, 1000);
        Log.e("page_error", "proceedForAddingDetails end....");
    }

    private void addUserData() {
        Log.e("page_error", "addUserData Start....");
        Map<String, Object> userdata = new HashMap<>();
        userdata.put("owner_id", firebaseUser.getUid());
        userdata.put("isDetailsAdded", false);

        db.collection(selecteduser)
                .document(firebaseUser.getUid())
                .set(userdata)
                .addOnFailureListener(e -> {
                    addUserData();
                });
        Log.e("page_error", "addUserData End....");
    }

    private void returnBackSigninFrag() {
        Snackbar.make(otp_main_container, "Unable to send OTP, Please try again !!", Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> getFragmentManager().popBackStack(), 1000);
    }

    private void otpResendDelayCountDown() {
        new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                otp_resend_lay.setVisibility(View.VISIBLE);
                otp_progressbar.setVisibility(View.GONE);
                otp_countdown_resendotp_lay.setVisibility(View.GONE);
                mTimeLeftInMillis = 30000;
            }
        }.start();
    }

    @SuppressLint("SetTextI18n")
    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        String sec_txt = (seconds < 2) ? " sec" : " secs";
        otp_countdown_time.setText(timeLeftFormatted + sec_txt);
    }

}