package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class signuppagetwo extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    String name,email;
    Spinner forlocation;
    Button next;

    String userlocation;
    EditText estate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signuppagetwo);

        name=getIntent().getStringExtra("username");
        email=getIntent().getStringExtra("useremail");

        estate=findViewById(R.id.signupuserestateformail);
        forlocation=findViewById(R.id.locationspinnerformail);
        next=findViewById(R.id.signupnextbtn2);
        ArrayAdapter<CharSequence> arrayAdapter =ArrayAdapter.createFromResource(this,R.array.locations,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        forlocation.setAdapter(arrayAdapter);
        forlocation.setOnItemSelectedListener(this);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Check_fields()){

                }else{

                    final String userestate = estate.getText().toString();
                    final String getuserlocation = userlocation;

                    Intent intent=new Intent(signuppagetwo.this,signuppagethree.class);
                    intent.putExtra("username",name);
                    intent.putExtra("useremail",email);
                    intent.putExtra("userlocation",getuserlocation);
                    intent.putExtra("userestate",userestate);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                }
            }
        });






    }

    private boolean Check_fields() {

        if(TextUtils.isEmpty(estate.getText().toString())){
            estate.setError("Field is Required");
            return false;
        }

        if(userlocation.equals("Choose Location")){
            Toast.makeText(this, "Choose a location", Toast.LENGTH_SHORT).show();
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