package com.groupwork.kinyua.okoa_loan.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.groupwork.kinyua.okoa_loan.R;

public class ProfileActivity extends AppCompatActivity {
    String fname="",lname="",phone="",id="";
    EditText edtfname,edtlname,edtphone,edtid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        edtfname=findViewById(R.id.firstNameEditText);
        edtlname=findViewById(R.id.lastNameEditText);
        edtid=findViewById(R.id.idNumberEditText);
        edtphone=findViewById(R.id.phoneEditText);
        edtphone.setText(getPhone());

        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);


        getUser();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    //method to get the user data
    public void getUser(){
        FirebaseDatabase mDatabase=FirebaseDatabase.getInstance();
        DatabaseReference mRf=mDatabase.getReference().child("User").child(getPhone());
        mRf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               fname= dataSnapshot.child("firstname").getValue().toString();
               lname= dataSnapshot.child("lastname").getValue().toString();
                id=dataSnapshot.child("nationalId").getValue().toString();

                uiBind();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void uiBind() {
        edtid.setText(id);
        edtlname.setText(lname);
        edtfname.setText(fname);
    }

    public String  getPhone(){
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
            String myPhone = sharedPreferences.getString("phone", null);
            return  myPhone;
        }catch (Exception e){
            Toast.makeText(this, "error occured", Toast.LENGTH_SHORT).show();
        }
        return "failed";
    }
}
