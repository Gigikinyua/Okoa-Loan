package com.groupwork.kinyua.okoa_loan.users;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.groupwork.kinyua.okoa_loan.R;
import com.groupwork.kinyua.okoa_loan.activity.MainActivity;
import com.groupwork.kinyua.okoa_loan.utility.User;


public class SignupActivity extends AppCompatActivity {
    private EditText  mFirstname, mLastname, mNational;
    private Button  btnSignUp;
    private FirebaseDatabase database;
    private DatabaseReference customerRef;
    String phonestr="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // get Firebase auth instance
        database = FirebaseDatabase.getInstance();
        customerRef = database.getReference("User");

        phonestr=getPhone();

        Toolbar toolbar = findViewById(R.id.signupToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(), PhoneAuthActivity.class));
            }
        });


        btnSignUp = findViewById(R.id.sign_up_button);
        mLastname = findViewById(R.id.lastNameEditText);
        mFirstname = findViewById(R.id.firstNameEditText);
        mNational = findViewById(R.id.IDEditText);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lastname = mLastname.getText().toString().trim();
                String firstname = mFirstname.getText().toString().trim();
                String nationalId = mNational.getText().toString().trim();


                if (TextUtils.isEmpty(firstname)) {
                    Toast.makeText(getApplicationContext(), "Enter First name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(lastname)) {
                    Toast.makeText(getApplicationContext(), "Enter Last Name!", Toast.LENGTH_SHORT).show();
                    return;
                }

               if (TextUtils.isEmpty(nationalId)) {
                    Toast.makeText(getApplicationContext(), "Enter ID number!", Toast.LENGTH_SHORT).show();
                    return;
                }

              //create
                User user = new User(firstname,lastname, nationalId);
                customerRef.child(phonestr).setValue(user, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if( databaseError == null){
                            Toast.makeText(getApplicationContext(),"user saved",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplicationContext(),"an error has occured"+databaseError.getMessage(),Toast.LENGTH_LONG).show();


                        }
                    }
                });

                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });
    }


    //method to get the phone
    public String  getPhone(){
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
            String myPhone = sharedPreferences.getString("phone", null);
            return  myPhone;
        }catch (Exception e){
            Toast.makeText(this, "error occured", Toast.LENGTH_SHORT).show();
        }
        return null;
    }


}
