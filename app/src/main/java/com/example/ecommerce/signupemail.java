package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;


public class signupemail extends Fragment implements AdapterView.OnItemSelectedListener{
    EditText name,estate,email,password;
    CheckBox remember;
    Spinner forlocation;

    String userlocation;
    String userid;
    Button signup;
    private FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_signupemail, container, false);
        name=(view).findViewById(R.id.signupusernameformaik);
        estate=(view).findViewById(R.id.signupuserestateformail);
        email=(view).findViewById(R.id.signupusermail);
        password=(view).findViewById(R.id.signupuserpasswordformail);
        remember=(view).findViewById(R.id.checkBox3);
        forlocation=(view).findViewById(R.id.locationspinnerformail);
        signup=(view).findViewById(R.id.signupemail);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        Paper.init(getContext());
        ArrayAdapter<CharSequence> arrayAdapter =ArrayAdapter.createFromResource(getContext(),R.array.locations,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        forlocation.setAdapter(arrayAdapter);
        forlocation.setOnItemSelectedListener(this);




signup.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(!Check_fields()){

        }else {

            final String username = name.getText().toString();
            final String userestate = estate.getText().toString();
            final String userpassword = password.getText().toString();
            final String useremail = email.getText().toString();
            final String getuserlocation = userlocation;

            fAuth.createUserWithEmailAndPassword(useremail, userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        FirebaseUser fuser = fAuth.getCurrentUser();
                        fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Error sending verification , Contact Admin", Toast.LENGTH_LONG).show();
                            }
                        });


                        Toast.makeText(getContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                        userid = fAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("users").document(userid);

                        Map<String, Object> user = new HashMap<>();
                        user.put("name", username);
                        user.put("pass", userpassword);
                        user.put("Estate", userestate);
                        user.put("loation", getuserlocation);
                        user.put("mail", useremail);

                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void avoid) {


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "", Toast.LENGTH_LONG).show();

                            }
                        });


                        fAuth.signInWithEmailAndPassword(useremail, userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (remember.isChecked()) {

                                        Paper.book().write(prevalent.userpasskey, userpassword);
                                        Paper.book().write(prevalent.useremailkey, useremail);

                                    }
                                    Toast.makeText(getContext(), "Login Success", Toast.LENGTH_SHORT).show();
                                   startActivity(new Intent(getContext(),emailverification.class));
                                } else {

                                    Toast.makeText(getContext(), "An error occured"
                                            + task.getException().getMessage(), Toast.LENGTH_SHORT).show();


                                }

                            }
                        });

                    } else {
                        Toast.makeText(getContext(), "An error occured" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

            }





});










        return view;
    }

    private boolean Check_fields() {
        if(TextUtils.isEmpty(name.getText().toString())){
            name.setError("Enter Your Full Name");
            return false;
        }
        if(TextUtils.isEmpty(estate.getText().toString())){

            estate.setError("Field is Required");

return false;

        }
        if( TextUtils.isEmpty(email.getText().toString())){
            email.setError("Field is Required");
            return false;
        }
        if( TextUtils.isEmpty(password.getText().toString())){
            password.setError("Field is Required");
            return false;
        }
        if(userlocation.equals("Choose Location")){
            Toast.makeText(getContext(), "Choose a location", Toast.LENGTH_SHORT).show();
           return false;
        }
        else {
            return true;
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        userlocation = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}