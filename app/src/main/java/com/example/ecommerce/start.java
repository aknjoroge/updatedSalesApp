package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jaeger.library.StatusBarUtil;

import io.paperdb.Paper;

public class start extends AppCompatActivity {
Button tologin,tosignup;
    FirebaseAuth fAuth;
TextView howwe;
    int CODE_AUTHENTICATION_VERIFICATION=241;
ProgressDialog loadBar;
String takeusermail,takeuserpass,takeuserphone,takephonepass,takechecklock;

    @Override
    protected void onStart() {
        super.onStart();
        //loadBar.hide();
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        StatusBarUtil.setTransparent(this);
        Paper.init(this);

        fAuth = FirebaseAuth.getInstance();

        tologin=findViewById(R.id.loginbutton);
        tosignup=findViewById(R.id.signupbutton);
        howwe=findViewById(R.id.howtxt);

        loadBar=new ProgressDialog(this,R.style.ProgressbarStyle);
        loadBar.setMessage("Logging in please wait...");
        loadBar.setCanceledOnTouchOutside(false);
        loadBar.hide();


        takeusermail = Paper.book().read(prevalent.useremailkey);
        takeuserpass = Paper.book().read(prevalent.userpasskey);

        takeuserphone = Paper.book().read(prevalent.phonekey);
        takephonepass = Paper.book().read(prevalent.phonepasskey);

        takechecklock = Paper.book().read(prevalent.lockstatkey);


        try {
            if (takechecklock.equals("unlocked")) {
         checkuserdatatwo();

                checkuserdata();
            }
            if (takechecklock.equals("locked")) {

callpassword();
            }

        }catch (Exception e){

            Toast.makeText(this, "error: "+e, Toast.LENGTH_SHORT).show();
        }




        howwe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomsheet bottomSheet = new bottomsheet();
                bottomSheet.show(getSupportFragmentManager(), "bottomsheet");
//                startActivity(new Intent(getApplicationContext(),howwework.class));
//                overridePendingTransition(R.anim.slide_up,R.anim.slide_out_up);
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

    private void callpassword() {
        KeyguardManager km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        if(km.isKeyguardSecure()) {
            Intent i = km.createConfirmDeviceCredentialIntent("Authentication required", "password");
            startActivityForResult(i, CODE_AUTHENTICATION_VERIFICATION);
        }
        else{
            Toast.makeText(start.this, "No any security setup done by user(pattern or password or pin or fingerprint", Toast.LENGTH_SHORT).show();
            //alert to continue without password

            AlertDialog dialog = new AlertDialog.Builder(this,R.style.AlertDialogStyle)
                    .setTitle("No Security Found")
                    .setMessage("Continue?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkuserdatatwo();
                            checkuserdata();


                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(start.this, "App wont Launch,Restart and click Yes ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==CODE_AUTHENTICATION_VERIFICATION)
        {

            checkuserdatatwo();
            checkuserdata();
        }
        else
        {
            Toast.makeText(this, "Failure: Unable to verify user's identity", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkuserdatatwo() {

        if (takeuserphone != "" && takephonepass != "") {

            if (!TextUtils.isEmpty(takeuserphone) && !TextUtils.isEmpty(takephonepass)) {
                loadBar.show();

                fAuth.signInWithEmailAndPassword(takeuserphone,takephonepass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        loadBar.hide();
                        startActivity(new Intent(getApplicationContext(),phoneverification.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadBar.hide();
                        startActivity(new Intent(getApplicationContext(),loginpage.class));
                        Toast.makeText(start.this, "Auto Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        }else {
            loadBar.hide();
        }


    }

    private void checkuserdata() {
        if (takeusermail != "" && takeuserpass != "") {

            if (!TextUtils.isEmpty(takeusermail) && !TextUtils.isEmpty(takeuserpass)) {
                loadBar.show();
                fAuth.signInWithEmailAndPassword(takeusermail,takeuserpass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        startActivity(new Intent(getApplicationContext(),emailverification.class));
                        loadBar.hide();
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        startActivity(new Intent(getApplicationContext(),loginpage.class));
                        loadBar.hide();
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        Toast.makeText(start.this, "Auto Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }else {

        }

    }
}