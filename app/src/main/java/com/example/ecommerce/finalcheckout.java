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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.jaeger.library.StatusBarUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class finalcheckout extends AppCompatActivity {
String cart,total,shippping,payment;
TextView finalshipping,finalcart,finaltotal;
    FirebaseFirestore fStore;
    Button finalpay;
    TextView town,estate;
    TextView name,phone;
    String userid;
    String getname;
    String cdate,ctime,cartkey;
    FirebaseAuth fAuth;
    ProgressDialog loadBar;
    String mailtake;
    FirebaseUser using;
    TextView formenuone,formenutwo,formenuthree;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalcheckout);
        StatusBarUtil.setTransparent(this);

        cart=getIntent().getStringExtra("cart");
        total=getIntent().getStringExtra("total");
        shippping=getIntent().getStringExtra("shipping");
        payment=getIntent().getStringExtra("paymentmethod");

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
                test();
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
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
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

    private void test() {

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
               deletecart();
               setordersatte();
               sendtoadmin();

               validated();
           }
       }
   },500);


    }

    private void deletecart() {

        fStore.collection("CartList").document("all").collection("peruser").document(userid).collection("orders")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        document.getData();
                        document.getId();
                        String info= document.getId();
                        fStore.collection("CartList").document("all")
                                .collection("peruser").document(userid).collection("orders")
                                .document(info).delete();


                    }

                }else {
                    Toast.makeText(finalcheckout.this, "task failed", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(finalcheckout.this, "data not replaced", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setordersatte() {

        Map<String,Object> state =new HashMap<>();
        state.put("orders","present");
        final DocumentReference documentReference = fStore.collection("CartList")
                .document("orderdetails").collection(userid).document("state");

        documentReference.set(state, SetOptions.merge());

        Map<String,Object> newsum =new HashMap<>();
        newsum.put("cartamount","0");

         DocumentReference doc = fStore.collection("CartList")
                .document("cartamounts").collection("general").document(userid);
         doc.set(newsum, SetOptions.merge());


    }

    private void sendtoadmin() {

        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd,yyyy");
        cdate =currentdate.format(calendar.getTime());

        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
        ctime =currenttime.format(calendar.getTime());

        cartkey= ctime+cdate;


        Map <String,Object> admin =new HashMap<>();
        admin.put("username",getname);
        admin.put("mail",mailtake);
        admin.put("Totalamount",total);
        admin.put("key",cartkey);
        admin.put("totalcartprice",cart);
        admin.put("shippingcharged",shippping);
        admin.put("paymentmethod",payment);
        admin.put("userid",userid);
        admin.put("date",cdate);
        admin.put("time",ctime);




        fStore.collection("AdminOrders").document("Foradmin")
                .collection("all").document(cartkey).set(admin).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(finalcheckout.this, "error adding to admin", Toast.LENGTH_SHORT).show();

            }
        });




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
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
        Snackbar.make(findViewById(R.id.finallinear), "ORDER PLACED", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        startActivity(new Intent(getApplicationContext(),categories.class));
    }

    private void autochangeprofile2() {
        final DocumentReference documentReference = fStore.collection("users").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                String locatake=documentSnapshot.getString("loation");
                String locatake2=documentSnapshot.getString("Estate");
                 mailtake =documentSnapshot.getString("mail");
                 getname=documentSnapshot.getString("name");
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