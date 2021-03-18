package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class signuppagethree extends AppCompatActivity {
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    String name,email,location,estate;
    EditText password;
    CheckBox remember,terms;

    String userid;
    Button signup;
    private FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ProgressDialog loadBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signuppagethree);


        name=getIntent().getStringExtra("username");
        email=getIntent().getStringExtra("useremail");
        location=getIntent().getStringExtra("userlocation");
        estate=getIntent().getStringExtra("userestate");
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        Paper.init(this);

        loadBar2=new ProgressDialog(this,R.style.ProgressbarStyle);
        loadBar2.setTitle("CHECKING CREDENTIALS.");
        loadBar2.setMessage("Working...");
        loadBar2.setCanceledOnTouchOutside(false);
        loadBar2.setIcon(R.drawable.login_24);
        loadBar2.hide();


        password=findViewById(R.id.signupuserpasswordformail);
        remember=findViewById(R.id.checkBox3);
        terms=findViewById(R.id.checkBoxagree);
        signup=findViewById(R.id.signupemail);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Check_fields()) {

                } else {
                    loadBar2.show();
                    final String userpassword = password.getText().toString();


                    fAuth.createUserWithEmailAndPassword(email, userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser fuser = fAuth.getCurrentUser();
                                fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        loadBar2.hide();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        loadBar2.hide();
                                        Toast.makeText(signuppagethree.this, "Error sending verification , Contact Admin", Toast.LENGTH_LONG).show();
                                    }
                                });


                                Toast.makeText(signuppagethree.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                userid = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(userid);

                                Map<String, Object> user = new HashMap<>();
                                user.put("name", name);
                                user.put("pass", userpassword);
                                user.put("Estate", estate);
                                user.put("loation", location);
                                user.put("mail", email);


                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void avoid) {


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(signuppagethree.this, "", Toast.LENGTH_LONG).show();

                                    }
                                });


                                DocumentReference document = fStore.collection("CartList")
                                        .document("cartamounts").collection("general").document(userid);
                                Map<String,Object> amount = new HashMap<>();
                                amount.put("cartamount","0");
                                document.set(amount).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(signuppagethree.this,"amount not initialized", Toast.LENGTH_SHORT).show();
                                    }
                                });


                                DocumentReference documenttwo = fStore.collection("CartList")
                                        .document("orderdetails").collection(userid).document("state");
                                Map<String,Object> state =new HashMap<>();
                                state.put("orders","none");
                                documenttwo.set(state).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(signuppagethree.this,"State not Set", Toast.LENGTH_SHORT).show();
                                    }
                                });


                                fAuth.signInWithEmailAndPassword(email, userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            if (remember.isChecked()) {

                                                Paper.book().write(prevalent.userpasskey, userpassword);
                                                Paper.book().write(prevalent.useremailkey, email);

                                            }
                                            loadBar2.hide();
                                            Toast.makeText(signuppagethree.this, "Login Success", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),emailverification.class));
                                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                                        } else {

                                            Toast.makeText(signuppagethree.this, "An error occured"
                                                    + task.getException().getMessage(), Toast.LENGTH_SHORT).show();


                                        }

                                    }
                                });

                            } else {
                                loadBar2.hide();
                                Toast.makeText(signuppagethree.this, "An error occured" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });


                }
            }
        });
    }
    private boolean Check_fields() {

        if( TextUtils.isEmpty(password.getText().toString())){
            password.setError("Field is Required");
            return false;
        }

        if (!terms.isChecked()){
            Toast.makeText(this, "please agree to our terms and policies", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }

    }
}