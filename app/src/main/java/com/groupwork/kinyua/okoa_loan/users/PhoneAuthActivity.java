package com.groupwork.kinyua.okoa_loan.users;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.groupwork.kinyua.okoa_loan.R;
import com.groupwork.kinyua.okoa_loan.activity.MainActivity;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PhoneAuth";
    private boolean mVerificationInProgress = false;

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;


    private EditText phoneText, codeEdt;
    private Button verifyButton;

    private TextView statusText, resendButton;

    private String phoneVerificationID;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    private FirebaseAuth auth;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_auth);

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        phone = "";
        phone = getPhone();

        phoneText = findViewById(R.id.phoneEditText);
        codeEdt = findViewById(R.id.codeEditText);
        verifyButton = findViewById(R.id.codeButton);
        resendButton = findViewById(R.id.ResendcodeButton);
        statusText = findViewById(R.id.statusText);

        verifyButton.setOnClickListener(this);
        resendButton.setOnClickListener(this);
        statusText.setText("Signed out");

        auth = FirebaseAuth.getInstance();


        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted: " + phoneAuthCredential);
                mVerificationInProgress = false;

                updateUI(STATE_VERIFY_SUCCESS, phoneAuthCredential);
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.w(TAG, "onVerificationFailed: ", e);
                mVerificationInProgress = false;

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    //invalid request
                    statusText.setText("Invalid Phone Number");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    //SMS quota exceeded
                    Log.d(TAG, "SMS Quota Exceeded");
                }

                updateUI(STATE_VERIFY_FAILED);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent: " + verificationId);

                phoneVerificationID = verificationId;
                resendToken = token;

                updateUI(STATE_CODE_SENT);
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        updateUI(user);

        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(phoneText.getText().toString());
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verificationCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verificationCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();

                            updateUI(STATE_SIGNIN_SUCCESS, user);

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                phoneText.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }

    private void signOut() {
        auth.signOut();
        updateUI(STATE_INITIALIZED);
    }

    private void updateUI(int uiState) {
        updateUI(uiState, auth.getCurrentUser(), null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user);
        } else {
            updateUI(STATE_INITIALIZED);
        }
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
        switch (uiState) {
            case STATE_INITIALIZED:
                // Initialized state, show only the phone number field and start button
                enableViews(verifyButton, phoneText);
                disableViews(resendButton, codeEdt,statusText);
                statusText.setVisibility(View.GONE);
                break;
            case STATE_CODE_SENT:
                // Code sent state, show the verification field, the
                verifyButton.setText("Verify Phone Number");
                phoneText.setVisibility(View.GONE);
                enableViews(codeEdt, resendButton, verifyButton);
//                disableViews(phoneText);
                statusText.setText("OTP code sent. Check your messages.");
                break;
            case STATE_VERIFY_FAILED:
                // Verification has failed, show all options
                enableViews(verifyButton, codeEdt, resendButton);
                statusText.setText("Verification failed.");
                break;
            case STATE_VERIFY_SUCCESS:
                // Verification has succeeded, proceed to firebase sign in
                disableViews(verifyButton, phoneText);

                statusText.setText("verification successful");

                break;
            case STATE_SIGNIN_FAILED:
                // No-op, handled by sign-in check
                statusText.setText("Sign in failed");
                break;
            case STATE_SIGNIN_SUCCESS:
                // Np-op, handled by sign-in check
                break;
        }

        if (user == null) {
            //            // Signed out

            phoneText.setVisibility(View.VISIBLE);
            verifyButton.setVisibility(View.VISIBLE);

            statusText.setText("signed out");
        } else {
            phonePref(phoneText.getText().toString().trim());

            try {
                if (!phone.equals("")) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                    finish();
                }
            } catch (Exception e) {
                startActivity(new Intent(getApplicationContext(), SignupActivity.class)
                );
                finish();
            }


        }
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = phoneText.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            phoneText.setError("Invalid phone number.");
        }
        return true;
    }

    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.codeButton:
                startPhoneNumberVerification(phoneText.getText().toString());
                if (!validatePhoneNumber()) {
                    return;
                }
                break;
            case R.id.ResendcodeButton:
                resendVerificationCode(phoneText.getText().toString(),resendToken);
                break;
        }
    }

    //code to store the sharedpreference
    public void phonePref(String phone) {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone", phone);
        editor.commit();
    }

    //method to get the phone
    public String getPhone() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
            String myPhone = sharedPreferences.getString("phone", null);
            return myPhone;
        } catch (Exception e) {
            Toast.makeText(this, "error occurred", Toast.LENGTH_SHORT).show();
        }
        return "failed";
    }
}
