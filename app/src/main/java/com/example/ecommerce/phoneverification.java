package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class phoneverification extends AppCompatActivity {

    String userid;
    FirebaseFirestore fStore;
    String codebysystem;
    PinView pinView;
    Button verify;
String name,estate,password,phone;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneverification);


        name=getIntent().getStringExtra("username");
        estate=getIntent().getStringExtra("estate");
        password=getIntent().getStringExtra("password");
        phone=getIntent().getStringExtra("phone").trim();




        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userid = fAuth.getCurrentUser().getUid();
        pinView=findViewById(R.id.phonecode);
        verify=findViewById(R.id.verifycodebtn);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifymaually();
            }
        });

        phoneaccount(phone);
    }

    private void verifymaually() {
        String input =pinView.getText().toString();
        if(!TextUtils.isEmpty(input)){
            verifycode(input);
        }else {
            Toast.makeText(this, "Enter Code", Toast.LENGTH_SHORT).show();

        }
    }


    private void phoneaccount( String phoneNo) {
        try {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNo,
                    60,
                    TimeUnit.SECONDS,
                    this,
                    fcallbacks

            );
//            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(fAuth)
//                    .setPhoneNumber(phone)
//                    .setTimeout(60L, TimeUnit.SECONDS)
//                    .setActivity(this)
//                    .setCallbacks(fcallbacks)
//                    .build();
//            PhoneAuthProvider.verifyPhoneNumber(options);
        }catch (Exception e){
            Toast.makeText(this, "error main "+e, Toast.LENGTH_SHORT).show();
        }


    }

    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks fcallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            try {
                String code= phoneAuthCredential.getSmsCode();
                if(code!= null){
                    pinView.setText(code);
                    verifycode(code);
                }
            }catch (Exception e){
                Toast.makeText(phoneverification.this, "error four", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codebysystem=s;
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(phoneverification.this, "Error in verifying"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifycode(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codebysystem,code);
            signincretedntial(credential);
        }catch (Exception e){
            Toast.makeText(this, "Manual Test failed", Toast.LENGTH_SHORT).show();
        }


    }

    private void signincretedntial(PhoneAuthCredential credential) {
        try {
            fAuth.signInWithCredential(credential)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(phoneverification.this, " okay", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(phoneverification.this, "verification Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "exception three", Toast.LENGTH_SHORT).show();
        }



    }
}