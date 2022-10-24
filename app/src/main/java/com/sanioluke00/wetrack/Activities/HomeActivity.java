package com.sanioluke00.wetrack.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.sanioluke00.wetrack.Fragments.AttendanceFragment;
import com.sanioluke00.wetrack.Fragments.HomeFragment;
import com.sanioluke00.wetrack.Fragments.ProfileFragment;
import com.sanioluke00.wetrack.Fragments.WorkersFragment;
import com.sanioluke00.wetrack.R;

public class HomeActivity extends AppCompatActivity {

    View home_mainlay, home_fragcontainer;
    ChipNavigationBar home_bottom_navbar;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        home_mainlay = findViewById(R.id.home_mainlay);
        home_fragcontainer = findViewById(R.id.home_fragcontainer);
        home_bottom_navbar = findViewById(R.id.home_bottom_navbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        getSupportFragmentManager().beginTransaction().add(R.id.home_mainlay, new HomeFragment()).commit();
        home_bottom_navbar.setItemSelected(R.id.btm_home,true);
        home_bottom_navbar.setOnItemSelectedListener(bottom_navlistner);

    }

    @SuppressLint("NonConstantResourceId")
    private final ChipNavigationBar.OnItemSelectedListener bottom_navlistner = i -> {
        Fragment selectedfragment = null;

        switch (i) {
            case R.id.btm_home:
                selectedfragment = new HomeFragment();
                break;

            case R.id.btm_attendance:
                selectedfragment = new AttendanceFragment();
                break;

            case R.id.btm_workers:
                selectedfragment = new WorkersFragment();
                break;

            case R.id.btm_profile:
                selectedfragment = new ProfileFragment();
                break;

            default:
                break;
        }

        // functions.darkstatusbardesign(MainActivity.this);
        getSupportFragmentManager().beginTransaction().replace(R.id.home_mainlay, selectedfragment).commit();

    };

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();
        } else {
            Snackbar.make(home_mainlay, "Press back again to Exit", Snackbar.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}