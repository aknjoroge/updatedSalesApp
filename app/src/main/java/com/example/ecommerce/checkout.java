package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jaeger.library.StatusBarUtil;
import com.squareup.picasso.Picasso;

import java.sql.Array;
import java.util.ArrayList;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;

public class checkout extends AppCompatActivity {
    ImageView settingdp;
    String takenews;
    TextView town,estate,changeloc,shipping,cart,total;
    TextView formenuone,formenutwo,formenuthree;
    FirebaseFirestore fStore;
    String initialbalance;
    String userid;
    Uri imageuri;
    ImageButton call;
    Button topayment;

    public RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String locationone,locationtwo,locationthree,locationfour,locationfive,locationsix;
    String generallocation;
    String setprice;

    Button setforname,setforphone,setforpass,setforemail,setforlocation;
    FirebaseAuth fAuth;
    FirebaseUser using;
    ProgressDialog loadBar;

    StorageReference storageReference;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    private static  class CustomEventListener implements CustomActivityOnCrash.EventListener{
        @Override
        public void onLaunchErrorActivity() {

        }

        @Override
        public void onRestartAppFromErrorActivity() {
        }

        @Override
        public void onCloseAppFromErrorActivity() {

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        StatusBarUtil.setTransparent(this);

        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
                .enabled(true) //default: true
                .showErrorDetails(false) //default: true
                .showRestartButton(false) //default: true
                .logErrorOnRestart(false) //default: true
                .trackActivities(false) //default: false
                .minTimeBetweenCrashesMs(2000) //default: 3000
                .errorDrawable(null) //default: bug image
                .restartActivity(home.class) //default: null (your app's launch activity)
                .errorActivity(ErrorActivity.class) //default: null (default error activity)
                .eventListener(new CustomEventListener()) //default: null
                .apply();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        using=fAuth.getCurrentUser();
        userid = fAuth.getCurrentUser().getUid();


        town=findViewById(R.id.checkouttowntxt);
        total=findViewById(R.id.checkouttotaltxt);
        topayment=findViewById(R.id.topaybtn);

        getbalance();
        autochangeprofile2();
        shippingprices();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setlocationprice();
            }
        },200);


        estate=findViewById(R.id.checkoutestatetxt);
        loadBar=new ProgressDialog(this,R.style.ProgressbarStyle);
        loadBar.setMessage("Reading Data");
        loadBar.setCanceledOnTouchOutside(false);
        loadBar.show();



        shipping=findViewById(R.id.checkoutshippingfee);
        cart=findViewById(R.id.checkoutfromcarttxt);

        recyclerView=findViewById(R.id.checkoutrecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        formenuone=findViewById(R.id.menuone);
        formenutwo=findViewById(R.id.menutwo);
        formenuthree=findViewById(R.id.menuthree);
        formenuone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        formenutwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(checkout.this,payment.class);
                intent.putExtra("shipping",shipping.getText().toString());
                intent.putExtra("cart",cart.getText().toString());
                intent.putExtra("total",total.getText().toString());

                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
        formenuthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(R.id.linearsum), "Fill in Payment Page First", Snackbar.LENGTH_LONG)
                       .setAction("Action", null).show();
            }
        });




        changeloc=findViewById(R.id.checkoutchangetxt);

        changeloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
confirm();

            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    loadBar.hide();



                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            loadaction();
                        }
                    },500);


                }catch (Exception e){
                    loadBar.hide();
                    Toast.makeText(checkout.this, "error: "+e, Toast.LENGTH_SHORT).show();
                }
            }
        },3000);





        topayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(checkout.this,payment.class);
                intent.putExtra("shipping",shipping.getText().toString());
                intent.putExtra("cart",cart.getText().toString());
                intent.putExtra("total",total.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });



    }
    public void getbalance() {

        final DocumentReference documentReference = fStore.collection("CartList")
                .document("cartamounts").collection("general").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                initialbalance=documentSnapshot.getString("cartamount");


            }
        });
    }

    private void shippingprices() {

        final DocumentReference documentReference = fStore.collection("locations").document("prices");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


               locationone=documentSnapshot.getString("Nairobi");
                 locationtwo=documentSnapshot.getString("Ruaka");
                locationthree=documentSnapshot.getString("Kikuyu");
                locationfour=documentSnapshot.getString("Dagoretti");
                locationfive=documentSnapshot.getString("Uthiru");
                locationsix=documentSnapshot.getString("Wangige");

            }
        });


    }

    private void autochangeprofile2() {
        final DocumentReference documentReference = fStore.collection("users").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                String locatake=documentSnapshot.getString("loation");
                generallocation=locatake;
                String locatake2=documentSnapshot.getString("Estate");
                town.setText(locatake);
                estate.setText(locatake2);
            }
        });






    }

    private void totalcalculations() {
        shipping.setText(setprice);
        cart.setText(initialbalance);

        int one= Integer.parseInt(setprice);
        int two=Integer.parseInt(initialbalance);
        int three =one+two;
        String amount=String.valueOf(three);
        total.setText(amount);


    }

    private void setlocationprice() {

        switch (generallocation){
            case "Nairobi":
                 setprice=locationone;
                break;
            case "Ruaka":
                 setprice=locationtwo;
                break;
            case "Kikuyu":
                 setprice=locationthree;
                break;
            case "Dagoretti":
                 setprice=locationfour;
                break;
            case "Uthiru":
                 setprice=locationfive;
                break;
            case "Wangige":
                 setprice=locationsix;
                break;


        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                totalcalculations();
            }
        },500);



    }

    private void confirm() {

        android.app.AlertDialog dialog = new AlertDialog.Builder(this,R.style.AlertDialogStyle)
                .setTitle("Change Location Address?")
                .setMessage("Continue")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(),changepassword.class));
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();


    }


    private void loadaction() {
        //To load products to the page

        FirestoreRecyclerOptions<forcheckout> options = new FirestoreRecyclerOptions.Builder<forcheckout>()
                .setQuery(fStore.collection("CartList").document("individual").collection("items").document(userid).collection("Cartiems"),forcheckout.class).build();
        FirestoreRecyclerAdapter<forcheckout,checkoutviewholder> adapter= new FirestoreRecyclerAdapter<forcheckout, checkoutviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull checkoutviewholder holder, int position, @NonNull final forcheckout model) {
                holder.txtname.setText("Item: "+model.getName()+".");
                holder.txtprice.setText(model.getPrice()+"/=");
                holder.txtamount.setText("No: "+model.getAmount()+".");



            }

            @NonNull
            @Override
            public checkoutviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.checkoutitem,parent,false);
                checkoutviewholder holder =new checkoutviewholder(view);
                return holder;



            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

}