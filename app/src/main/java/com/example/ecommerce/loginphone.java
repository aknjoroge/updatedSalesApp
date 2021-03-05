package com.example.ecommerce;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class loginphone extends Fragment {

    EditText phone,password;
    CheckBox remlogin;
    Button login;
    TextView forgotpassword,resendcode;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_loginphone, container, false);

        phone=(view).findViewById(R.id.userloginphone);
        password=(view).findViewById(R.id.userloginpassword);
        remlogin=(view).findViewById(R.id.checkBoxphone);
        login=(view).findViewById(R.id.phoneloginbtn);
        forgotpassword=(view).findViewById(R.id.forgotpasswordtxt2);
        resendcode=(view).findViewById(R.id.resendphoneverification);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkfields()){

                } else{

                }

            }
        });







        return view;
    }

    private boolean checkfields() {
        if(TextUtils.isEmpty(phone.getText().toString())){
            phone.setError("Phone needed");
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