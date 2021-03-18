package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jaeger.library.StatusBarUtil;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class offers extends AppCompatActivity {
    DrawerLayout nav;
    FirebaseAuth fAuth;
    String htm;
    String dataloc ="";
    StorageReference storageReference;
    public RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseUser using;
    FirebaseFirestore fStore;
    ImageView himage;
    boolean doubleBackToExitPressedOnce = false;
    String userid;

    String aq;
    TextView pname,pmail;
    private AppBarConfiguration mAppBarConfiguration;


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        Paper.init(this);
        StatusBarUtil.setTransparent(this);
        //bottom nav
        aq=getIntent().getStringExtra("categoryname");

        fAuth = FirebaseAuth.getInstance();
        using=fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        userid = fAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
        dataloc=aq;

        recyclerView=findViewById(R.id.recyclermenuoffers);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        try{
            loadaction();

        }catch (Exception e){
            Toast.makeText(this, "Error: "+e, Toast.LENGTH_LONG).show();
        }

    }

    private void loadaction() {
        FirestoreRecyclerOptions<forproduct> options = new FirestoreRecyclerOptions.Builder<forproduct>()
                .setQuery(fStore.collection("Products").document("all").collection(dataloc),forproduct.class).build();
        FirestoreRecyclerAdapter<forproduct,productviewholder> adapter= new FirestoreRecyclerAdapter<forproduct, productviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull productviewholder holder, int position, @NonNull final forproduct model) {
                holder.txtpname.setText(model.getName()+".");
                holder.txtpprice.setText("Price: "+model.getPrice());
                holder.txtpdescription.setText("Category: "+model.getCategory());


                Picasso.get().load(model.getFilepath()).into(holder.pimage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(offers.this,productdetails.class);
                        intent.putExtra("pid",model.getRandomKey());
                        intent.putExtra("cid",model.getCategory());

                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                    }
                });
            }

            @NonNull
            @Override
            public productviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.offeritem,parent,false);
                productviewholder holder =new productviewholder(view);
                return holder;



            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();




    }

}