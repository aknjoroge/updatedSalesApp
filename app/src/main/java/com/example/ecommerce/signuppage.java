package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class signuppage extends AppCompatActivity {
Button sigupmail,signupphone;
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signuppage);
        sigupmail=findViewById(R.id.mailsignupbtn);
        signupphone=findViewById(R.id.phonesignupbtn);

        sigupmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sigupmail.setTextColor(getResources().getColor(R.color.white));
                signupphone.setTextColor(getResources().getColor(R.color.grey));
                Fragment mail =new signupemail();
                getSupportFragmentManager().beginTransaction().replace(R.id.framesignup,mail).commit();
            }
        });
        signupphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment phone =new signupphone();
                sigupmail.setTextColor(getResources().getColor(R.color.grey));
                signupphone.setTextColor(getResources().getColor(R.color.white));
                getSupportFragmentManager().beginTransaction().replace(R.id.framesignup,phone).commit();
            }
        });

    }
}