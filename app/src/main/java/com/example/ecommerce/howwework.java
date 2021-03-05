package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class howwework extends AppCompatActivity {
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_down,R.anim.slide_out_down);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howwework);
    }
}