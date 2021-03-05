package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class emailverification extends AppCompatActivity {
TextView code,emailwarning;
String userid;
ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseUser using;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailverification);
        code=findViewById(R.id.resendcodetxt);
emailwarning=findViewById(R.id.textView6);
progressBar=findViewById(R.id.emaiwarnprogressbar);
        fAuth = FirebaseAuth.getInstance();
        using=fAuth.getCurrentUser();

        userid = fAuth.getCurrentUser().getUid();
        ;
        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                using.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(emailverification.this, "Verification Email Sent ", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(emailverification.this, "Error sending verification , Contact Admin", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });




        checkverification();
    }



    public void checkverification(){


        if(!using.isEmailVerified()){
emailwarning.setVisibility(View.VISIBLE);
progressBar.setVisibility(View.VISIBLE);

        }else {
            emailwarning.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
startActivity(new Intent(getApplicationContext(),categories.class));
        }


    }
}