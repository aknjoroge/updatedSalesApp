package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class finalcheckout extends AppCompatActivity {
String cart,total,shippping;
TextView finalshipping,finalcart,finaltotal;
    FirebaseFirestore fStore;
    Button finalpay;
    TextView town,estate;
    TextView name,phone;
    String userid;
    FirebaseAuth fAuth;
    ProgressDialog loadBar;
    FirebaseUser using;
    TextView formenuone,formenutwo,formenuthree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalcheckout);

        cart=getIntent().getStringExtra("cart");
        total=getIntent().getStringExtra("total");
        shippping=getIntent().getStringExtra("shipping");

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        using=fAuth.getCurrentUser();
        userid = fAuth.getCurrentUser().getUid();

        finalshipping=findViewById(R.id.finalshippingfee);
        finalcart=findViewById(R.id.finalfromcarttxt);
        finaltotal=findViewById(R.id.finaltotaltxt);
        town=findViewById(R.id.finaltown);
        estate=findViewById(R.id.finalestate);
        name=findViewById(R.id.finalusername);
        phone=findViewById(R.id.finaluserphone);
        finalpay=findViewById(R.id.paybtn);

        loadBar=new ProgressDialog(this);
        loadBar.setTitle("VALIDATING.");
        loadBar.setIcon(R.drawable.security_24);
        loadBar.setMessage("please wait...");
        loadBar.setCanceledOnTouchOutside(false);
        loadBar.hide();

        finalpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process();
            }
        });

        finalshipping.setText(shippping);
        finalcart.setText(cart);
        finaltotal.setText(total);

        formenuone=findViewById(R.id.menuonex);
        formenutwo=findViewById(R.id.menutwoy);
        formenuthree=findViewById(R.id.menuthreez);
        formenuone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),cart.class));
            }
        });
        formenutwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(R.id.finallinear), "Can only Navigate to Exit", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        formenuthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

autochangeprofile2();
    }

    private void process() {
loadBar.show();

   new Handler().postDelayed(new Runnable() {
       @Override
       public void run() {
           if(TextUtils.isEmpty(phone.getText().toString())){
               loadBar.hide();
               registerphone();

           }
           else {
               validated();
           }
       }
   },1000);


    }

    private void validated() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadBar.hide();
                endvalidation();
            }
        },1000);


    }
    public void endvalidation(){
        Toast.makeText(this, "PROCESS HAS ENDED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
        Snackbar.make(findViewById(R.id.finallinear), "DONE", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void autochangeprofile2() {
        final DocumentReference documentReference = fStore.collection("users").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                String locatake=documentSnapshot.getString("loation");
                String locatake2=documentSnapshot.getString("Estate");
                String getname=documentSnapshot.getString("name");
                String getphone=documentSnapshot.getString("phone");
                name.setText(getname);
                phone.setText(getphone);
                town.setText(locatake);
                estate.setText(locatake2);
            }
        });



    }
    public void registerphone() {

        final EditText dellac = new EditText(this);
        android.app.AlertDialog dialog = new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                .setTitle("ENTER A PHONE NUMBER WE CAN CONTACT YOU WITH")
                .setMessage("Your Phone:")
                .setView(dellac)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(dellac.getText().toString())) {

                            String in = dellac.getText().toString();

                            Map<String,Object> usermaps =new HashMap<>();
                            usermaps.put("phone",in);
                            final DocumentReference documentReference = fStore.collection("users").document(userid);

                            documentReference.set(usermaps, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Snackbar.make(findViewById(R.id.finallinear), "number Saved", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    autochangeprofile2();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(finalcheckout.this, "An Error in saving phone occurred", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            Snackbar.make(findViewById(R.id.finallinear), "Phone number is Required", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Snackbar.make(findViewById(R.id.finallinear), "Phone number is Required", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    }
                })
                .show();

    }

}