package com.sanioluke00.wetrack.Activities;

import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;
import com.sanioluke00.wetrack.R;

public class RegDetailsActivity extends AppCompatActivity {

    private RelativeLayout rdetails_container;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_details);

        rdetails_container = findViewById(R.id.rdetails_container);
        FragmentManager fm = getSupportFragmentManager();

        /*if (sign_page_name == null) {
            fm.beginTransaction().add(R.id.details_container, new userdetailsfragment()).commit();
        } else if (sign_page_name.equals("ulocation")) {
            fm.beginTransaction().add(R.id.details_container, new ulocationfragment()).commit();
        } else if (sign_page_name.equals("uemail")) {
            fm.beginTransaction().add(R.id.details_container, new uemailverifyfragment()).commit();
        } else if (sign_page_name.equals("upreference")) {
            fm.beginTransaction().add(R.id.details_container, new upreferencesfragment()).commit();
        } else {
            fm.beginTransaction().add(R.id.details_container, new userdetailsfragment()).commit();
        }*/

    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {

            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                super.onBackPressed();
                finishAffinity();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        } else {
            Snackbar.make(rdetails_container, "Press back again to Exit", Snackbar.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}