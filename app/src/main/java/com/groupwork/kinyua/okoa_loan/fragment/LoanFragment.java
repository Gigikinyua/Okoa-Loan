package com.groupwork.kinyua.okoa_loan.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.groupwork.kinyua.okoa_loan.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class LoanFragment extends Fragment {

    View view;
    Spinner purpose_Spinner;
    Spinner security_Spinner;
    String amount,duration,purpose,security;

    EditText edtamount,edduration;

    Button submit;

    private FirebaseDatabase database;
    private DatabaseReference customerRef;


    public LoanFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_loan, container, false);

        submit=view.findViewById(R.id.sbmt);
        edtamount=view.findViewById(R.id.loanEditText);
        edduration=view.findViewById(R.id.durationEditText);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        database = FirebaseDatabase.getInstance();
        customerRef = database.getReference("User");

         purpose_Spinner = (Spinner) view.findViewById(R.id.purposeSpinner);

        List<String> categories = new ArrayList<>();
        categories.add("Business"); categories.add("Medical bill"); categories.add("Personal");
        categories.add("Education"); categories.add("Other");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        purpose_Spinner.setAdapter(dataAdapter);

         security_Spinner = (Spinner) view.findViewById(R.id.securitySpinner);

        List<String> securityCategories = new ArrayList<>();
        securityCategories.add("Electronics"); securityCategories.add("Title deed");
        securityCategories.add("Job payslip"); securityCategories.add("Household items");

        ArrayAdapter<String> secureAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, securityCategories);
        secureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        security_Spinner.setAdapter(secureAdapter);



        return view;
    }

    //method to get data
    public void getData(
            )
    {
        amount=edtamount.getText().toString().trim();
        duration=edduration.getText().toString().trim();
        purpose=purpose_Spinner.getSelectedItem().toString();
        security=security_Spinner.getSelectedItem().toString();
        postData(amount,duration,purpose,security);
    }

    //methosd
    public  void postData(String amount,String duration,String purpose,String security){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference mRef=firebaseDatabase.getReference().child("User").child(getPhone()).child("loan").push();
         mRef.child("amount").setValue(amount);
         mRef.child("duration").setValue(duration);
         mRef.child("security").setValue(security);
         mRef.child("purpose").setValue(purpose);


    }

    //method to get the phone
    public String  getPhone(){
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
            String myPhone = sharedPreferences.getString("phone", null);
            return  myPhone;
        }catch (Exception e){

        }
        return null;
    }

}
