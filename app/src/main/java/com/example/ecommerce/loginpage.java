package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaeger.library.StatusBarUtil;

import io.paperdb.Paper;

public class loginpage extends AppCompatActivity {
    EditText email,password;
    CheckBox remlogin;

    Button login;
    TextView forgotpassword,resendcode;
    ProgressDialog loadBar2;
    private FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        StatusBarUtil.setTransparent(this);


        email=findViewById(R.id.loginemailtxt);
        password=findViewById(R.id.loginpasswordtxt);
        remlogin=findViewById(R.id.checkBox);
        loadBar2=new ProgressDialog(this,R.style.ProgressbarStyle);
        loadBar2.setTitle("CHECKING CREDENTIALS.");
        loadBar2.setMessage("Working...");
        loadBar2.setCanceledOnTouchOutside(false);
        loadBar2.setIcon(R.drawable.login_24);
        loadBar2.hide();

        login=findViewById(R.id.emailloginbtn);
        forgotpassword=findViewById(R.id.forgotpasswordtxt);
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendlink();
            }
        });
        resendcode=findViewById(R.id.resendmailverification);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        Paper.init(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkfields()){

                } else{
                    loadBar2.show();
                    final String useremail=email.getText().toString();
                    final String userpassword=password.getText().toString();

                    fAuth.signInWithEmailAndPassword(useremail, userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (remlogin.isChecked()) {

                                    Paper.book().write(prevalent.userpasskey, userpassword);
                                    Paper.book().write(prevalent.useremailkey, useremail);

                                }
                                loadBar2.hide();
                                Toast.makeText(loginpage.this, "Login Success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(loginpage.this,emailverification.class));

                            } else {
                                loadBar2.hide();
                                Toast.makeText(loginpage.this, "An error occured"
                                        + task.getException().getMessage(), Toast.LENGTH_SHORT).show();


                            }

                        }
                    });



                }

            }
        });


    }
    private void sendlink() {
        final EditText dellac = new EditText(this);
        android.app.AlertDialog dialog = new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                .setTitle("ENTER EMAIL TO SEND RESET LINK TO")
                .setMessage("Send Reset link to your Email Account")
                .setView(dellac)
                .setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(dellac.getText().toString())) {
                            String in = dellac.getText().toString();
                            fAuth.sendPasswordResetEmail(in).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(loginpage.this, "LINK SENT", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(loginpage.this, "Sending link failed Check your internet", Toast.LENGTH_SHORT).show();
                                }
                            });




                        } else {
                            Toast.makeText(loginpage.this, "Field is empty", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private boolean checkfields() {
        if(TextUtils.isEmpty(email.getText().toString())){
            email.setError("Email needed");
            return false;
        }
        if(TextUtils.isEmpty(password.getText().toString())){
            password.setError("your password is needed");
            return false;
        }
        else {
            return true;
        }
    }



}