package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
lanchermanager lanchermanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lanchermanager=new lanchermanager(this);
        Paper.init(this);
        if(lanchermanager.isFirstTime()){

            String sec="unlocked";
            Paper.book().write(prevalent.lockstatkey,sec);
            lanchermanager.setFirstLunch(false);
        }else{

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),start.class));
            }
        },3000);

    }
}