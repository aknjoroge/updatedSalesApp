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
import com.jaeger.library.StatusBarUtil;

import java.util.concurrent.TimeUnit;

public class phoneverification extends AppCompatActivity {

    String userid;
    FirebaseFirestore fStore;
    String phonenumber;
    String codebysystem;
    PhoneAuthProvider.ForceResendingToken token;
    PinView pinView;
    Button verify;
    String contrycode="+254";
String name,estate,password,phone;
    private FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneverification);
        StatusBarUtil.setTransparent(this);
        fAuth = FirebaseAuth.getInstance();
        name=getIntent().getStringExtra("username");
        estate=getIntent().getStringExtra("estate");
        password=getIntent().getStringExtra("password");
        phone=getIntent().getStringExtra("phone").trim();




        fStore = FirebaseFirestore.getInstance();
        userid = fAuth.getCurrentUser().getUid();
        pinView=findViewById(R.id.phonecode);
        verify=findViewById(R.id.verifycodebtn);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    phoneaccount(phonenumber);
                }catch (Exception e){
                    Toast.makeText(phoneverification.this, "eroor "+e, Toast.LENGTH_LONG).show();
                }

                //verifymaually();
            }
        });

         phonenumber=contrycode+phone;
        Toast.makeText(this, phonenumber, Toast.LENGTH_LONG).show();

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

        Toast.makeText(this, "Entered here ", Toast.LENGTH_SHORT).show();
try {
    PhoneAuthOptions options =
            PhoneAuthOptions.newBuilder(fAuth)
                    .setPhoneNumber("+254 727732542")       // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(this)                 // Activity (for callback binding)
                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                    .build();
    PhoneAuthProvider.verifyPhoneNumber(options);
}catch (Exception e){
    Toast.makeText(this, "error" +e, Toast.LENGTH_SHORT).show();
}



    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks  mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
            Toast.makeText(phoneverification.this, "Timeout", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {

            String sms=credential.getSmsCode();
            if(sms!=null){
                pinView.setText(sms);

                signincretedntial(credential);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(phoneverification.this, "Error in creating account check internet", Toast.LENGTH_SHORT).show();

            // Show a message and update the UI
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            //super.onCodeSent(verificationId, token);
            codebysystem=verificationId;
            token=token;
            Toast.makeText(phoneverification.this, "sent code", Toast.LENGTH_SHORT).show();
        }
    };





    private void verifycode(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codebysystem, code);
            signincretedntial(credential);
        }catch (Exception e){
            Toast.makeText(this, "failed Building credentials check Your internet", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(phoneverification.this, "Failed singing in", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "signing in Error", Toast.LENGTH_SHORT).show();
        }



    }
}