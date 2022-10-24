package com.sanioluke00.wetrack.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sanioluke00.wetrack.DataModels.Functions;
import com.sanioluke00.wetrack.R;

public class HomeFragment extends Fragment {

    ImageButton homefrag_addmanager, homefrag_addemployee;
    TextView homefrag_business_name;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;

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
        
        String company_path_str= new Functions().getSharedPrefsValue(getContext(),"user_data","company_path","string",null);
        if(company_path_str!=null){

            Log.e("comapny_path","The company path is "+company_path_str);
            /*company_path_str.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                String name= task.getResult().getString("company_name");
                                homefrag_business_name.setText(name);
                            }
                        }
                    });*/
        }

        return v;
    }

}