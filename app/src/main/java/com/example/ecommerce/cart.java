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
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class cart extends AppCompatActivity {
    public RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseUser using;
    String usercname=" ",usercprice= " ",usercno= " ";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String generalstate;
    String initialbalance;
    String keptkey;
    ImageButton call;
    String nameoforder =" ";
    Button tocheck;

    String setttprice;
    ImageView himage;
    String userid;
    int totalprice=0;
    TextView pname,pmail,fortotalview;
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        fAuth = FirebaseAuth.getInstance();
        recyclerView=findViewById(R.id.cartrecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        call=findViewById(R.id.placecallcartimage);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callone();
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        using=fAuth.getCurrentUser();
        tocheck=findViewById(R.id.checkoutbtn);
        tocheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (generalstate.equals("present")){
                    Snackbar.make(findViewById(R.id.cartlayout), "YOU HAVE A PENDING ORDER WAIT UNTIL ORDER IS COMPLETED", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fortotalview.setText("You have Pending Order");
                    tocheck.setVisibility(View.INVISIBLE);

                    return;

                }else{

                }


                if(totalprice == 0){
                    Toast.makeText(cart.this, "First add item To cart", Toast.LENGTH_SHORT).show();
                    tocheck.setVisibility(View.INVISIBLE);
                    fortotalview.setText("Cart is Empty");
                }else {

                    String setinpricea=setttprice;
                    setttprice= null;
                    totalprice=0;
                    Intent intent = new Intent(cart.this, checkout.class);

                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
            }
        });
        fortotalview=findViewById(R.id.carttotaltxt);


        fStore = FirebaseFirestore.getInstance();
        userid = fAuth.getCurrentUser().getUid();
        try {
            getorderstate();
            getbalance();
            loaddocs();
        }catch (Exception e){
            Toast.makeText(this, "Error: "+e, Toast.LENGTH_LONG).show();
        }


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

    public void getbalance() {

        final DocumentReference documentReference = fStore.collection("CartList")
                .document("cartamounts").collection("general").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                initialbalance=documentSnapshot.getString("cartamount");
                fortotalview.setText("Total: "+initialbalance);
                int genprice= Integer.parseInt(initialbalance);
                totalprice =genprice;

            }
        });
        tocheck.setVisibility(View.VISIBLE);
    }

    private void loaddocs() {

        FirestoreRecyclerOptions<forcart> options = new FirestoreRecyclerOptions.Builder<forcart>()
                .setQuery(fStore.collection("CartList").document("all").collection("peruser").document(userid).collection("orders"),forcart.class).build();
        FirestoreRecyclerAdapter<forcart,cartviewholder> adapter= new FirestoreRecyclerAdapter<forcart, cartviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull cartviewholder holder, int position, @NonNull final forcart model) {

                holder.txtpname.setText("Name: "+model.getName()+".");
                holder.txtpprice.setText("Price: "+model.getPrice()+"/=");

                keptkey=model.getKey();
                // int priceacf= (Integer.parseInt(model.getPrice()));

                usercname = usercname +"Item Name:"+model.getName()+"\n";
                usercprice = usercprice +"Item Price:"+model.getPrice();
                usercno = usercno +"Item Amount:"+model.getAmount()+"\n";

                nameoforder =nameoforder + model.getName() +", ";

                String curentprice =model.getPrice();
                String currentamoungt= model.getAmount();

                int perquant=((Integer.parseInt(model.getPrice()))) * Integer.parseInt(model.getAmount());

                //total value
//                String forqtotal= String.valueOf(perquant);
//                totalprice=totalprice+perquant;
//                setttprice= String.valueOf(totalprice);



                holder.txtpamount.setText("Amount: "+model.getAmount());

                holder.remove.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        removeamount(model.getAmount(),model.getPrice(),model.getKey());



                    }
                });
            }

            @NonNull
            @Override
            public cartviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cartitem,parent,false);
                cartviewholder holder =new cartviewholder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void removeamount(final String amount, final String price, final String key) {

        android.app.AlertDialog dialog = new AlertDialog.Builder(this,R.style.AlertDialogStyle)
                .setTitle("Cart Opptions")
                .setMessage("Remove Product?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletesingleitem(key);
                        int revprice=Integer.parseInt(price);
                        int revamount =Integer.parseInt(amount);
                        int sum = revamount*revprice;
                        totalprice=totalprice-sum;
                        String rem=String.valueOf(totalprice);
                        fortotalview.setText("Total: "+rem);

                        Map<String,Object> newsum =new HashMap<>();
                        newsum.put("cartamount",rem);
                        final DocumentReference documentReference = fStore.collection("CartList")
                                .document("cartamounts").collection("general").document(userid);

                        documentReference.set(newsum, SetOptions.merge());

                        fStore.collection("CartList").document(userid).collection("orders").document(key)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Snackbar.make(findViewById(R.id.cartlayout), "Item Deleted", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                        //Toast.makeText(cart.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                                        loaddocs();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(cart.this, "Error deleting document", Toast.LENGTH_SHORT).show();
                                    }
                                });


                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();




    }



    private void deletesingleitem(String keymain) {


        fStore.collection("CartList").document(userid).collection("Cartiems").document(keymain)
                .delete();

    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            loaddocs();
        }catch (Exception e){
            Toast.makeText(this, "Error: "+e, Toast.LENGTH_LONG).show();
        }

    }
    public void callone(){
        android.app.AlertDialog dialog = new AlertDialog.Builder(this,R.style.AlertDialogStyle)
                .setTitle("CALL TO PLACE ORDER?")
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
                        Toast.makeText(cart.this, "Call Cancelled", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }


}