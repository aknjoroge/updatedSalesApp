package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class productdetails extends AppCompatActivity {
    ImageView detailimage;
    TextView txtdetailname,txtdetailprice,txtdetailcategory;
    String takeprice;
    String productid;
    String takename;
    String cartkey;
    String amounts;
    ElegantNumberButton add;
    ImageButton call;
    public RecyclerView recyclerView;
    String nametake;
    String pricetake;
    RecyclerView.LayoutManager layoutManager;
    String pathsa;
    String takecat;
    String usernamea;
    String userphonea; String usermaila;
    Button tocart;
    String htmls;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    String cdate,ctime;
    FirebaseUser using;
    String userid;
    FirebaseFirestore fStore;
    String pid,cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetails);

        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        fStore = FirebaseFirestore.getInstance();
        detailimage=findViewById(R.id.detailproductimage);
        txtdetailname=findViewById(R.id.detailproductname);
        txtdetailprice=findViewById(R.id.detailproductprice);
        call=findViewById(R.id.placecallimage);
        add=findViewById(R.id.elegandadd);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
callone();
            }
        });

        recyclerView=findViewById(R.id.recyclerproductdetail);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        using=fAuth.getCurrentUser();

        txtdetailcategory=findViewById(R.id.detailproductcategory);

        userid = fAuth.getCurrentUser().getUid();
        tocart=findViewById(R.id.addtocartbtn);


        pid=getIntent().getStringExtra("pid");
        cid=getIntent().getStringExtra("cid");

        try{
            uploaddate();
            loadaction();
        }catch (Exception e){
            Toast.makeText(this, "Error: "+e, Toast.LENGTH_LONG).show();
        }


        tocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar= Calendar.getInstance();
                SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd,yyyy");
                cdate =currentdate.format(calendar.getTime());

                SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
                ctime =currenttime.format(calendar.getTime());

                cartkey= ctime+cdate;
                takename=txtdetailname.getText().toString();
                takeprice= txtdetailprice.getText().toString();
                takecat=txtdetailcategory.getText().toString();
                productid=pid;
                amounts=add.getNumber();
                final Map<String,Object> pdets =new HashMap<>();
                pdets.put("name",takename);
                pdets.put("time",ctime);
                pdets.put("date",cdate);
                pdets.put("key",cartkey);
                pdets.put("amount",amounts);
                pdets.put("price",takeprice);
                pdets.put("category",takecat);
                pdets.put("productid",pid);


                fStore.collection("CartList").document("orders").collection(userid).document(cartkey).set(pdets).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Calendar calendar= Calendar.getInstance();
                        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd,yyyy");
                        cdate =currentdate.format(calendar.getTime());

                        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
                        ctime =currenttime.format(calendar.getTime());

                        cartkey= ctime+cdate;

                        Map<String,Object> cartitem = new HashMap<>();
                        cartitem.put("key",cartkey);
                        cartitem.put("name",takename);
                        cartitem.put("price",takeprice);
                        cartitem.put("date",cdate);
                        cartitem.put("time",ctime);
                        fStore.collection("CartList").document("orders").collection(userid).document(cartkey).set(cartitem).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(productdetails.this, "Added to cart Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),cart.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(productdetails.this, "Cart list item failed to upload", Toast.LENGTH_LONG).show();
                            }
                        });






                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(productdetails.this, "error updating Data one"+e.toString(), Toast.LENGTH_SHORT).show();

                    }
                });




            }
        });


    }


    public void uploaddate() {

        final DocumentReference documentReference = fStore.collection("Products").document("all").collection(cid).document(pid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                 nametake=documentSnapshot.getString("name");
                txtdetailname.setText(nametake);
                 pricetake =documentSnapshot.getString("price");
                String cattake =documentSnapshot.getString("category");
                String phonetake =documentSnapshot.getString("phone");
                String emailtake =documentSnapshot.getString("mail");

                txtdetailcategory.setText("Category: "+cattake);
                txtdetailprice.setText(pricetake);
                htmls=documentSnapshot.getString("filepath");
                // Toast.makeText(productdetails.this, htmls, Toast.LENGTH_SHORT).show();
                Picasso.get().load(htmls).into(detailimage);



            }
        });
    }


    private void loadaction() {
        //To load products to the page

        FirestoreRecyclerOptions<forcategory> options = new FirestoreRecyclerOptions.Builder<forcategory>()
                .setQuery(fStore.collection("Category").document("uploads").collection("all"),forcategory.class).build();
        FirestoreRecyclerAdapter<forcategory,categoryviewholder> adapter= new FirestoreRecyclerAdapter<forcategory, categoryviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull categoryviewholder holder, int position, @NonNull final forcategory model) {
                holder.catname.setText(model.getName()+".");



                Picasso.get().load(model.getFilepath()).into(holder.mainimage);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(productdetails.this,home.class);
                        intent.putExtra("categoryname",model.getCategory());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public categoryviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.categoryitem,parent,false);
                categoryviewholder holder =new categoryviewholder(view);
                return holder;



            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    public void callone(){
        android.app.AlertDialog dialog = new AlertDialog.Builder(this,R.style.AlertDialogStyle)
                .setTitle("PLACE CALL")
                .setMessage("Contact Support On :+254710664418?")

                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:+254710664418"));
                        startActivity(intent);

                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(productdetails.this, "Call Cancelled", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

}