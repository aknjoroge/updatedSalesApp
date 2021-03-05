package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import io.paperdb.Paper;


public class loginemail extends Fragment {
EditText email,password;
CheckBox remlogin;
ProgressBar progressBar;
Button login;
TextView forgotpassword,resendcode;
    private FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_loginemail, container, false);

        email=(view).findViewById(R.id.loginemailtxt);
        password=(view).findViewById(R.id.loginpasswordtxt);
        remlogin=(view).findViewById(R.id.checkBox);
        progressBar=(view).findViewById(R.id.emailloginprogressabar);
        login=(view).findViewById(R.id.emailloginbtn);
        forgotpassword=(view).findViewById(R.id.forgotpasswordtxt);
        resendcode=(view).findViewById(R.id.resendmailverification);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        Paper.init(getContext());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(!checkfields()){

              } else{
progressBar.setVisibility(View.VISIBLE);
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
                              Toast.makeText(getContext(), "Login Success", Toast.LENGTH_SHORT).show();
                              startActivity(new Intent(getContext(),emailverification.class));
                          } else {

                              Toast.makeText(getContext(), "An error occured"
                                      + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                              progressBar.setVisibility(View.INVISIBLE);

                          }

                      }
                  });



                }

            }
        });








        return  view;
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