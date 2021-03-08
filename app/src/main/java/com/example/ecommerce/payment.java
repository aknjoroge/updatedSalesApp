package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class payment extends AppCompatActivity {
String cart,total,shippping;
EditText voucher;
Button next;
TextView formenuone,formenutwo,formenuthree;
    String vouchernumber="none";
Switch aSwitch;
TextView forshipping,forcart,fortotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        cart=getIntent().getStringExtra("cart");
        total=getIntent().getStringExtra("total");
        shippping=getIntent().getStringExtra("shipping");

        voucher=findViewById(R.id.voucherboxplace);
        next=findViewById(R.id.tocheckbtn);



        formenuone=findViewById(R.id.menuonea);
        formenutwo=findViewById(R.id.menutwob);
        formenuthree=findViewById(R.id.menuthreec);
        formenuone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
startActivity(new Intent(getApplicationContext(),cart.class));
            }
        });
        formenutwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        formenuthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(payment.this,finalcheckout.class);
                intent.putExtra("shipping",forshipping.getText().toString());
                intent.putExtra("cart",forcart.getText().toString());
                intent.putExtra("total",fortotal.getText().toString());
                startActivity(intent);
            }
        });

        forshipping=findViewById(R.id.paymentshippingfee);
        forcart=findViewById(R.id.paymentfromcarttxt);
        fortotal=findViewById(R.id.paymenttotaltxt);
        aSwitch=findViewById(R.id.mpesaswitch);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(aSwitch.isChecked()){
                    Toast.makeText(payment.this, "mpesa", Toast.LENGTH_SHORT).show();
                }if(!aSwitch.isChecked()){
                    Toast.makeText(payment.this, "none", Toast.LENGTH_SHORT).show();
                }
                else {

                }
            }
        });



        fortotal.setText(total);
        forcart.setText(cart);
        forshipping.setText(shippping);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(voucher.getText().toString())){
                    vouchernumber=voucher.getText().toString();
                }else {

                }

                Toast.makeText(payment.this, "No Voucher ", Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(payment.this,finalcheckout.class);
                intent.putExtra("shipping",forshipping.getText().toString());
                intent.putExtra("cart",forcart.getText().toString());
                intent.putExtra("total",fortotal.getText().toString());
                startActivity(intent);

            }
        });

    }
}