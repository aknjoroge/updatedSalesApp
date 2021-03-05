package com.example.ecommerce;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class signupphone extends Fragment implements AdapterView.OnItemSelectedListener{

    EditText name,estate,phone,password;
    CheckBox remember;
    Spinner forlocation;
    String userlocation;
    Button signup;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_signupphone, container, false);

        name=(view).findViewById(R.id.signupusernameforphone);
        estate=(view).findViewById(R.id.signupuserestateforphone);
        phone=(view).findViewById(R.id.userphonenumber);
        password=(view).findViewById(R.id.signupuserpasswordforphone);
        remember=(view).findViewById(R.id.checkBox2);
        forlocation=(view).findViewById(R.id.locationspinnerforphone);
        signup=(view).findViewById(R.id.signupphone);

        ArrayAdapter<CharSequence> arrayAdapter =ArrayAdapter.createFromResource(getContext(),R.array.locations,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        forlocation.setAdapter(arrayAdapter);
        forlocation.setOnItemSelectedListener(this);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Check_fields()){

                }else {
                    String username= name.getText().toString();
                    String userestate=estate.getText().toString();
                    String userpaassword=password.getText().toString();
                    String userphone=phone.getText().toString();

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
        if( TextUtils.isEmpty(phone.getText().toString())){
            phone.setError("Field is Required");
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