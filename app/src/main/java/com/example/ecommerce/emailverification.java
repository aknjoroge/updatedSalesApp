package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaeger.library.StatusBarUtil;

public class emailverification extends AppCompatActivity {
TextView code,emailwarning;
String userid;
ProgressBar progressBar;
    ImageView tuwashow,techkey;
    FirebaseAuth fAuth;
    Button togmail;
    ProgressDialog loadBar;
    FirebaseUser using;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailverification);
        StatusBarUtil.setTransparent(this);
        code=findViewById(R.id.resendcodetxt);
emailwarning=findViewById(R.id.textView6);
progressBar=findViewById(R.id.emaiwarnprogressbar);
togmail=findViewById(R.id.closeappbtn);

        tuwashow=findViewById(R.id.tuwashowmimg2);
        techkey=findViewById(R.id.techkeyimg2);
        fAuth = FirebaseAuth.getInstance();
        using=fAuth.getCurrentUser();
        loadBar=new ProgressDialog(this,R.style.ProgressbarStyle);
        loadBar.setTitle("RESENDING VERIFICATION.");
        loadBar.setMessage("please wait...");
        loadBar.setCanceledOnTouchOutside(false);
        loadBar.hide();

        userid = fAuth.getCurrentUser().getUid();
        ;
        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadBar.show();
                using.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                      loadBar.hide();
                        Toast.makeText(emailverification.this, "Verification Email Sent ", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadBar.hide();
                        Toast.makeText(emailverification.this, "Error sending verification , Contact Admin", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

      

        tuwashow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(R.id.verifylayout), "TUWASHOW AFRICA", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        techkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(R.id.verifylayout), "TECHKEY Ltd", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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