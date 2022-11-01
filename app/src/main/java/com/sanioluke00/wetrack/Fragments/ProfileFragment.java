package com.sanioluke00.wetrack.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sanioluke00.wetrack.Activities.AccountInfoActivity;
import com.sanioluke00.wetrack.Activities.AppSettingsActivity;
import com.sanioluke00.wetrack.Activities.PersonalInfoActivity;
import com.sanioluke00.wetrack.Activities.SelectUserActivity;
import com.sanioluke00.wetrack.DataModels.Functions;
import com.sanioluke00.wetrack.R;


public class ProfileFragment extends Fragment {

    Button profilefrag_logout_btn;
    View profilefrag_mainlay;
    View prof_profile_settings, prof_acc_settings;
    View prof_app_settings, prof_aboutus, prof_faqs, prof_contactus, prof_feedback, prof_privacy;

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;


    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        profilefrag_mainlay= v.findViewById(R.id.profilefrag_mainlay);
        profilefrag_logout_btn = v.findViewById(R.id.profilefrag_logout_btn);

        prof_profile_settings= v.findViewById(R.id.prof_profile_settings);
        prof_acc_settings= v.findViewById(R.id.prof_acc_settings);
        prof_app_settings= v.findViewById(R.id.prof_app_settings);
        prof_aboutus= v.findViewById(R.id.prof_aboutus);
        prof_faqs= v.findViewById(R.id.prof_faqs);
        prof_contactus= v.findViewById(R.id.prof_contactus);
        prof_feedback= v.findViewById(R.id.prof_feedback);
        prof_privacy= v.findViewById(R.id.prof_privacy);

        prof_profile_settings.setOnClickListener(v1-> startActivity(new Intent(getContext(), PersonalInfoActivity.class)));

        prof_acc_settings.setOnClickListener(v1-> startActivity(new Intent(getContext(), AccountInfoActivity.class)));

        prof_app_settings.setOnClickListener(v1-> startActivity(new Intent(getContext(), AppSettingsActivity.class)));

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();

        profilefrag_logout_btn.setOnClickListener(v1 -> {
            new Functions().putSharedPrefsValue(getContext(), "user_data", "login_status", "boolean", false);
            verify();
            firebaseAuth.signOut();
            Snackbar.make(profilefrag_mainlay,"Logout Successful. Please login to an account.",Snackbar.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> startActivity(new Intent(getContext(), SelectUserActivity.class)),3000);
        });

        return v;
    }


    private void verify() {
        FirebaseAuth.AuthStateListener mAuthStateListner = firebaseAuth -> {
            FirebaseUser mFirebaseuser = firebaseAuth.getCurrentUser();
            if (mFirebaseuser == null) {
                Toast.makeText(getContext(), "You have already logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), SelectUserActivity.class));
            }
        };
    }

}