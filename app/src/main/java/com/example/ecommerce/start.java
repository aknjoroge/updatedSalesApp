package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;

public class start extends AppCompatActivity {
Button tologin,tosignup;
    FirebaseAuth fAuth;
TextView howwe;
ProgressDialog loadBar;
String takeusermail,takeuserpass,takeuserphone,takephonepass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        tologin=findViewById(R.id.loginbutton);
        tosignup=findViewById(R.id.signupbutton);
        howwe=findViewById(R.id.howtxt);
        fAuth = FirebaseAuth.getInstance();
        loadBar=new ProgressDialog(this);
loadBar.setTitle("LOADING.");
loadBar.setMessage("Logging in please wait...");
loadBar.setCanceledOnTouchOutside(false);
        Paper.init(this);

        takeusermail = Paper.book().read(prevalent.useremailkey);
        takeuserpass = Paper.book().read(prevalent.userpasskey);

        takeuserphone = Paper.book().read(prevalent.phonekey);
        takephonepass = Paper.book().read(prevalent.phonepasskey);
        checkuserdatatwo();
        checkuserdata();






        howwe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),howwework.class));
                overridePendingTransition(R.anim.slide_up,R.anim.slide_out_up);
            }
        });

        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),loginpage.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

            }
        });
        tosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),signuppage.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }

    private void checkuserdatatwo() {

        if (takeuserphone != "" && takephonepass != "") {
            loadBar.show();
            if (!TextUtils.isEmpty(takeuserphone) && !TextUtils.isEmpty(takephonepass)) {


                fAuth.signInWithEmailAndPassword(takeuserphone,takephonepass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
loadBar.hide();
                        startActivity(new Intent(getApplicationContext(),phoneverification.class));
                        loadBar.hide();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        startActivity(new Intent(getApplicationContext(),loginpage.class));
                        Toast.makeText(start.this, "Auto Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        }else {

        }


    }

    private void checkuserdata() {
        if (takeusermail != "" && takeuserpass != "") {
            loadBar.show();
            if (!TextUtils.isEmpty(takeusermail) && !TextUtils.isEmpty(takeuserpass)) {

                fAuth.signInWithEmailAndPassword(takeusermail,takeuserpass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        startActivity(new Intent(getApplicationContext(),emailverification.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        startActivity(new Intent(getApplicationContext(),loginpage.class));
                        Toast.makeText(start.this, "Auto Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }else {

        }

    }
}