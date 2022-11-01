package com.sanioluke00.wetrack.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sanioluke00.wetrack.DataModels.Functions;
import com.sanioluke00.wetrack.R;

public class PersonalInfoActivity extends AppCompatActivity {

    TextView mprofile_fullname, mprofile_gender, mprofile_emailid, mprofile_contactno;
    TextView mprofile_comp_name, mprofile_comp_location, mprofile_comp_services, mprofile_comp_contactno, mprofile_comp_year_exp;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;
    Functions functions= new Functions();
    String curr_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        mprofile_fullname= findViewById(R.id.mprofile_fullname);
        mprofile_gender= findViewById(R.id.mprofile_gender);
        mprofile_emailid= findViewById(R.id.mprofile_emailid);
        mprofile_contactno= findViewById(R.id.mprofile_contactno);

        mprofile_comp_name= findViewById(R.id.mprofile_comp_name);
        mprofile_comp_location= findViewById(R.id.mprofile_comp_location);
        mprofile_comp_services= findViewById(R.id.mprofile_comp_services);
        mprofile_comp_contactno= findViewById(R.id.mprofile_comp_contactno);
        mprofile_comp_year_exp= findViewById(R.id.mprofile_comp_year_exp);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
        db= FirebaseFirestore.getInstance();
        boolean log_status= functions.getSharedPrefsValue(getApplicationContext(),"user_data","login_status","boolean",false);
        curr_user= functions.getSharedPrefsValue(getApplicationContext(), "user_data", "ptype", "string",null);

        if(log_status && curr_user!=null){
            if(curr_user.equals("Owners")){
                mprofile_fullname.setText(functions.getSharedPrefsValue(getApplicationContext(), "user_data", "full_name", "string", "Name"));
                String code= functions.getSharedPrefsValue(getApplicationContext(), "user_data", "country_code", "string", "None");
                String phone=functions.getSharedPrefsValue(getApplicationContext(), "user_data", "contact_num", "string", "None");
                mprofile_contactno.setText(code+"-"+phone);
                mprofile_emailid.setText(functions.getSharedPrefsValue(getApplicationContext(), "user_data", "email_id", "string", "None"));
                mprofile_gender.setText(functions.getSharedPrefsValue(getApplicationContext(), "user_data", "gender", "string", "None"));
                displayCompanyFullDetails();
            }
            else{
                mprofile_gender.setVisibility(View.GONE);
                mprofile_emailid.setVisibility(View.GONE);
                mprofile_fullname.setText(functions.getSharedPrefsValue(getApplicationContext(),"user_data","pname","string","None"));
                String contact= functions.getSharedPrefsValue(getApplicationContext(),"user_data","pcountrycode","string","None")
                        +"-"+ functions.getSharedPrefsValue(getApplicationContext(),"user_data","pcontact","string","None");
                mprofile_contactno.setText(contact);
                displayCompanyFullDetails();
            }
        }
    }

    private void displayCompanyFullDetails(){
        String comp_path= functions.getSharedPrefsValue(getApplicationContext(), "user_data", "company_path", "string",null);
        if(comp_path!=null){
            db.document(comp_path)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            mprofile_comp_name.setText(task.getResult().getString("company_name"));
                            mprofile_comp_location.setText(task.getResult().getString("company_location"));
                            mprofile_comp_services.setText(task.getResult().getString("company_services"));
                            mprofile_comp_contactno.setText(task.getResult().getString("company_contactnum"));
                            mprofile_comp_year_exp.setText(task.getResult().getString("company_year_exp"));
                        }
                        else{
                            displayNoDataMessages();
                        }
                    });
        }
        else{
            displayNoDataMessages();
        }
    }

    private void displayNoDataMessages(){
        mprofile_comp_name.setText("No Data");
        mprofile_comp_location.setText("No Data");
        mprofile_comp_services.setText("No Data");
        mprofile_comp_contactno.setText("No Data");
        mprofile_comp_year_exp.setText("No Data");
    }
}