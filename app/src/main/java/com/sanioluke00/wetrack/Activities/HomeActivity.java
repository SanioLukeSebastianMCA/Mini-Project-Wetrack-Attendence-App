package com.sanioluke00.wetrack.Activities;

import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sanioluke00.wetrack.R;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    RelativeLayout home_mainlay;
    FirebaseFirestore db;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        home_mainlay = findViewById(R.id.home_mainlay);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishAffinity();
            /*int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                super.onBackPressed();
                finishAffinity();
            } else {
                getSupportFragmentManager().popBackStack();
            }*/
        } else {
            Snackbar.make(home_mainlay, "Press back again to Exit", Snackbar.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}