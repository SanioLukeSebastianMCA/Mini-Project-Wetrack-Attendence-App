package com.sanioluke00.wetrack.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sanioluke00.wetrack.DataModels.Functions;
import com.sanioluke00.wetrack.R;

public class HomeFragment extends Fragment {

    ImageButton homefrag_addmanager, homefrag_addemployee;
    TextView homefrag_business_name;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;
    Functions functions= new Functions();

    public HomeFragment() {  }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_home, container, false);

//        homefrag_addemployee= v.findViewById(R.id.homefrag_addemployee);
//        homefrag_addmanager= v.findViewById(R.id.homefrag_addmanager);
        homefrag_business_name= v.findViewById(R.id.homefrag_business_name);
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
        db= FirebaseFirestore.getInstance();

        boolean log_status= functions.getSharedPrefsValue(getContext(),"user_data","login_status","boolean",false);
        String curr_user= functions.getSharedPrefsValue(getContext(), "user_data", "ptype", "string",null);
        String comp_path= functions.getSharedPrefsValue(getContext(), "user_data", "company_path", "string",null);
        if(log_status && curr_user!=null && comp_path!=null){
            db.document(comp_path)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            homefrag_business_name.setText(task.getResult().getString("company_name"));
                        }
                    });
        }

        return v;
    }

}