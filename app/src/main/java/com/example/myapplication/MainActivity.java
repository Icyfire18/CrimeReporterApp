package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {

    EditText get_phoneNo,get_otp;
    Button button_getverificationcode,button_verify;
    private FirebaseAuth mAuth;
    private static final String TAG = "MyActivity";
    String codeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Signup");

        SharedPreferences preferences = getSharedPreferences("Signin", Context.MODE_PRIVATE);
        final SharedPreferences.Editor  editor = preferences.edit();

        if (preferences.contains("signin_id")) {
            startActivity(new Intent(MainActivity.this, MapsActivity.class));
            finish();
        }
        mAuth = FirebaseAuth.getInstance();

        get_otp = findViewById(R.id.get_otp);
        get_phoneNo = findViewById(R.id.get_phoneNo);

        findViewById(R.id.button_getverificationcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
            }
        });

        findViewById(R.id.button_verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifySignInCode();
            }
        });

    }

    private void verifySignInCode(){

        String code = get_otp.getText().toString();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //sign in the user open new activity
                            SharedPreferences preferences = getSharedPreferences("Signin", Context.MODE_PRIVATE);
                            final SharedPreferences.Editor  editor = preferences.edit();
                            editor.putBoolean("signin_id",true);
                            editor.commit();
                            Toast.makeText(getApplicationContext(),
                                    "Login Successfull",
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(MainActivity.this,MapsActivity.class));
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            finish();
                        } else {
                                ///error
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(),
                                    "Login error try again later",
                                    Toast.LENGTH_LONG).show();
                            }
                        }

                });
    }

    private void sendVerificationCode(){

        String phone = get_phoneNo.getText().toString();

        if(phone.isEmpty()){
            get_phoneNo.setError("Phone number is required");
            get_phoneNo.requestFocus();
            return;
        }

        if(phone.length() < 10){
            get_phoneNo.setError("Please enter a valid number");
            get_phoneNo.requestFocus();
            return;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                Toast.makeText(MainActivity.this, "Verification Completed", Toast.LENGTH_SHORT).show();

                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(MainActivity.this,"Verification failed",Toast.LENGTH_SHORT).show();
            Log.w(TAG, "onVerificationFailed", e);
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
            Log.d(TAG, "onCodeSent:" + s);
            Toast.makeText(MainActivity.this,"Code sent",Toast.LENGTH_SHORT).show();
        }
    };

}
