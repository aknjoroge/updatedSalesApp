package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jaeger.library.StatusBarUtil;

public class loginpage extends AppCompatActivity {
Button showphone,showemail;

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
        showphone=findViewById(R.id.phoneloginbtn);
        showemail=findViewById(R.id.mailloginbtn);
        showphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment phone =new loginphone();
                getSupportFragmentManager().beginTransaction().replace(R.id.framelogin,phone).commit();
                showphone.setTextColor(getResources().getColor(R.color.white));
                showemail.setTextColor(getResources().getColor(R.color.grey));

            }
        });

        showemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment mail =new loginemail();
                showphone.setTextColor(getResources().getColor(R.color.grey));
                showemail.setTextColor(getResources().getColor(R.color.white));
                getSupportFragmentManager().beginTransaction().replace(R.id.framelogin,mail).commit();
            }
        });
    }
}