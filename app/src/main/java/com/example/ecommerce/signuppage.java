package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;

public class signuppage extends AppCompatActivity {

    EditText name,email;
    Button next;
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signuppage);
        name=findViewById(R.id.signupusernameformaik);
        email=findViewById(R.id.signupusermail);
        next=findViewById(R.id.signupnextbtn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Check_fields()){

                }else {
                    final String username = name.getText().toString();
                    final String useremail = email.getText().toString();

                    Intent intent=new Intent(signuppage.this,signuppagetwo.class);
                    intent.putExtra("username",username);
                    intent.putExtra("useremail",useremail);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);



                }

            }
        });


    }

    private boolean Check_fields() {
        if(TextUtils.isEmpty(name.getText().toString())){
            name.setError("Enter Your Full Name");
            return false;
        }

        if( TextUtils.isEmpty(email.getText().toString())){
            email.setError("Field is Required");
            return false;
        }

        else {
            return true;
        }

    }
}