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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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
import com.google.firebase.storage.StorageReference;
import com.jaeger.library.StatusBarUtil;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class chickendetails extends AppCompatActivity {
    ImageView detailimage;
    TextView txtdetailname,txtdetailprice,txtdetailcategory;
    String takeprice;
    String productid;
    String takename;
    Button half,full;
    String generalstate;
    String cartkey;
    String amounts;
    String priceone,pricetwo;
    ElegantNumberButton add;
    Button call;
    String initialbalance;
    public RecyclerView recyclerView;
    String nametake;
    String pricetake;
    RecyclerView.LayoutManager layoutManager;
    String pathsa;
    String quantity="none";
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
    String pid;
    ProgressDialog loadBar;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chickendetails);
        StatusBarUtil.setTransparent(this);
        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        fStore = FirebaseFirestore.getInstance();
        detailimage=findViewById(R.id.detailproductimage);
        txtdetailname=findViewById(R.id.detailproductname);
        txtdetailprice=findViewById(R.id.detailproductprice);
        call=findViewById(R.id.placecallimage);
        add=findViewById(R.id.elegandadd);
        half=findViewById(R.id.halfbutton);
        full=findViewById(R.id.fullbutton);

        half.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity="half";
                half.setTextColor(getResources().getColor(R.color.white));
                full.setTextColor(getResources().getColor(R.color.grey));
                half.setTextSize(18);
                full.setTextSize(14);
            }
        });
        full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity="full";
                half.setTextColor(getResources().getColor(R.color.grey));
                full.setTextColor(getResources().getColor(R.color.white));
                full.setTextSize(18);
                half.setTextSize(14);

            }
        });


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callone();
            }
        });

        loadBar=new ProgressDialog(this,R.style.ProgressbarStyle);
        loadBar.setTitle("ADDING TO CART.");
        loadBar.setMessage("Adding item to cart...");
        loadBar.setIcon(R.drawable.cart_24);
        loadBar.setCanceledOnTouchOutside(false);
        loadBar.hide();
        recyclerView=findViewById(R.id.recyclerproductdetail);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        using=fAuth.getCurrentUser();

        txtdetailcategory=findViewById(R.id.detailproductcategory);

        userid = fAuth.getCurrentUser().getUid();
        tocart=findViewById(R.id.addtocartbtn);


        pid=getIntent().getStringExtra("pid");


        getprices();

        try{
            getbalance();
            uploaddate();
            loadaction();
            getorderstate();
        }catch (Exception e){
            Toast.makeText(this, "Error: "+e, Toast.LENGTH_LONG).show();
        }


        tocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (generalstate.equals("present")){

                    final Snackbar snackbar= Snackbar.make(findViewById(R.id.checkdetailslayout), "YOU HAVE A " +
                            "PENDING ORDER WAIT UNTIL ORDER IS COMPLETED", Snackbar.LENGTH_LONG)
                            .setAction("Action", null);
                    View snackbarView = snackbar.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
                    params.topMargin =50;
                    params.gravity = Gravity.TOP;
                    snackbarView.setLayoutParams(params);
                    snackbar.show();

                    loadBar.hide();

                    return;

                }
                if(quantity.equals("none")){

                    final Snackbar snackbar= Snackbar.make(findViewById(R.id.checkdetailslayout), "SELECT A QUANTITY TO PURCHASE", Snackbar.LENGTH_LONG)
                            .setAction("Action", null);
                    View snackbarView = snackbar.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
                    params.topMargin =50;
                    params.gravity = Gravity.TOP;
                    snackbarView.setLayoutParams(params);
                    snackbar.show();


                    return;

                }
                if(quantity.equals("half")){
                    takeprice= priceone;

                }
                if(quantity.equals("full")){
                    takeprice= pricetwo;
                }


                loadBar.show();
                getbalance();

                Calendar calendar= Calendar.getInstance();
                SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd,yyyy");
                cdate =currentdate.format(calendar.getTime());

                SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
                ctime =currenttime.format(calendar.getTime());

                cartkey= ctime+cdate;
                takename=txtdetailname.getText().toString();

                takecat=txtdetailcategory.getText().toString();
                productid=pid;
                amounts=add.getNumber();

                //item math
                try {
                    int initial=Integer.parseInt(initialbalance);
                    int cartpice = Integer.parseInt(takeprice);
                    int cartamount=Integer.parseInt(amounts);
                    int sum = cartpice*cartamount;
                    int finalsum = initial+sum;
                    updatesum(finalsum);

                }catch (Exception e){
                    loadBar.hide();

                    Toast.makeText(chickendetails.this, "math error do not continue", Toast.LENGTH_SHORT).show();
                    return;
                }






                final Map<String,Object> pdets =new HashMap<>();
                pdets.put("name",takename);
                pdets.put("time",ctime);
                pdets.put("date",cdate);
                pdets.put("key",cartkey);
                pdets.put("amount",amounts);
                pdets.put("price",takeprice);
                pdets.put("category",takecat);
                pdets.put("productid",pid);


                fStore.collection("CartList").document("all").collection("peruser").document(userid).collection("orders")
                        .document(cartkey).set(pdets).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Calendar calendar= Calendar.getInstance();
                        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd,yyyy");
                        cdate =currentdate.format(calendar.getTime());

                        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
                        ctime =currenttime.format(calendar.getTime());



                        Map<String,Object> cartitem = new HashMap<>();
                        cartitem.put("key",cartkey);
                        cartitem.put("name",takename);
                        cartitem.put("amount",amounts);
                        cartitem.put("price",takeprice);
                        cartitem.put("date",cdate);
                        cartitem.put("time",ctime);
                        fStore.collection("CartList").document("individual").collection("items")
                                .document(userid).collection("Cartiems").document(cartkey).set(cartitem).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                loadBar.hide();
                                Toast.makeText(chickendetails.this, "Added to cart Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(chickendetails.this,chickenhome.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(chickendetails.this, "Cart list item failed to upload", Toast.LENGTH_LONG).show();
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(chickendetails.this, "error updating Data one"+e.toString(), Toast.LENGTH_SHORT).show();

                    }
                });




            }
        });


    }


    private void getprices() {
        //
        final DocumentReference documentReference =  fStore.collection("Products")
                .document("allforchicken").collection("Chicken").document(pid);;
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                priceone=documentSnapshot.getString("priceone");
                pricetwo=documentSnapshot.getString("pricetwo");
                half.setText("HALF: "+priceone +"/=");
                full.setText("FULL: "+pricetwo +"/=");

            }
        });

    }

    public void getorderstate() {

        final DocumentReference documentReference = fStore.collection("CartList")
                .document("orderdetails").collection(userid).document("state");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                generalstate=documentSnapshot.getString("orders");


            }
        });
    }

    private void updatesum(int finalsum) {
        try {
            int data = finalsum;
            String updatesum =String.valueOf(data);

            Map<String,Object> newsum =new HashMap<>();
            newsum.put("cartamount",updatesum);
            final DocumentReference documentReference = fStore.collection("CartList")
                    .document("cartamounts").collection("general").document(userid);

            documentReference.set(newsum, SetOptions.merge());



        }catch (Exception e){
            Toast.makeText(this, "update error", Toast.LENGTH_SHORT).show();
        }


    }


    public void uploaddate() {

        final DocumentReference documentReference =  fStore.collection("Products").document("allforchicken").collection("Chicken").document(pid);
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
                        Intent intent=new Intent(chickendetails.this,home.class);
                        intent.putExtra("categoryname",model.getCategory());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

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
                .setIcon(R.drawable.call_24)
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
                        Toast.makeText(chickendetails.this, "Call Cancelled", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

}