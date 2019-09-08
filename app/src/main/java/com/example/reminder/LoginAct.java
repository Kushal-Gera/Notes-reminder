package com.example.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginAct extends AppCompatActivity {
    //    private static final String TAG = "LoginAct";
    private static String VERIFICATION_ID;

    FirebaseAuth auth;

    EditText phone, otp;
    Button login, getCode;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        phone = findViewById(R.id.phone);
        otp = findViewById(R.id.otp);
        getCode = findViewById(R.id.getCode);
        login = findViewById(R.id.login_btn);
        login.setVisibility(View.INVISIBLE);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phone.getText().toString().trim();
                if (!TextUtils.isEmpty(number) && number.length() > 9) {

                    getVerificationCode('+' + "91" + number);

                    progressBar.setVisibility(View.VISIBLE);
                    login.setVisibility(View.VISIBLE);
                    getCode.setVisibility(View.INVISIBLE);
                } else {
                    phone.setError("Phone Number Required");
                    phone.requestFocus();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userCode = otp.getText().toString().trim();
                if (!TextUtils.isEmpty(userCode))
                    verifyCode(userCode);
            }
        });


    }

    private void verifyCode(String userCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VERIFICATION_ID, userCode);
        signInWith(credential);

    }

    private void signInWith(PhoneAuthCredential credential) {

        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginAct.this, MainActivity.class));
                            finish();

                        } else
                            Toast.makeText(LoginAct.this, "ERROR OCCURRED", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void getVerificationCode(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            String userCode = credential.getSmsCode();
            if (userCode != null) {
                otp.setText(userCode);
                verifyCode(userCode);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(LoginAct.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            VERIFICATION_ID = s;

        }


    };


}
