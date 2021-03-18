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
import com.jaeger.library.StatusBarUtil;

public class payment extends AppCompatActivity {
String cart,total,shippping,paymentmethod;
EditText voucher;
Button next;
TextView formenuone,formenutwo,formenuthree;
    String vouchernumber="none";
Switch mpesaSwitch,deliveryswitch;
String mpesa = "off",deliver ="off";
TextView forshipping,forcart,fortotal;
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        StatusBarUtil.setTransparent(this);
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
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

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
//                Intent intent=new Intent(payment.this,finalcheckout.class);
//                intent.putExtra("shipping",forshipping.getText().toString());
//                intent.putExtra("cart",forcart.getText().toString());
//                intent.putExtra("total",fortotal.getText().toString());
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        forshipping=findViewById(R.id.paymentshippingfee);
        forcart=findViewById(R.id.paymentfromcarttxt);
        fortotal=findViewById(R.id.paymenttotaltxt);
        mpesaSwitch=findViewById(R.id.mpesaswitch);
        deliveryswitch=findViewById(R.id.deliveryswitch);
        deliveryswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (deliveryswitch.isChecked()){
                    deliver="on";
                }else {
                    deliver="off";
                }
            }
        });
        mpesaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mpesaSwitch.isChecked()){
                   mpesa="on";
                }if(!mpesaSwitch.isChecked()){
                    mpesa="off";
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
                if (mpesa =="off" && deliver=="off"){
                    Snackbar.make(findViewById(R.id.paymentlayout), "Select a Payment scheme M-pesa OR Pay on Delivery", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                if (mpesa =="on" && deliver=="on"){
                    Snackbar.make(findViewById(R.id.paymentlayout), "ONLY ONE Payment scheme M-pesa OR Pay on Delivery is Required" , Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }


                if( mpesa =="on"){
                    paymentmethod="mpesa";
                }else {

                }
                if(deliver=="on"){
                    paymentmethod = "delivery";
                }else {

                }


                Intent intent=new Intent(payment.this,finalcheckout.class);
                intent.putExtra("shipping",forshipping.getText().toString());
                intent.putExtra("cart",forcart.getText().toString());
                intent.putExtra("total",fortotal.getText().toString());
                intent.putExtra("paymentmethod",paymentmethod);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

            }
        });

    }

}