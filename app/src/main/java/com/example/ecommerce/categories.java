package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class categories extends AppCompatActivity {
    public RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseUser using;
    FirebaseAuth fAuth;
    StorageReference storageReference;
    FirebaseFirestore fStore;
    String keptkey;

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        //firebese declarations
        fAuth = FirebaseAuth.getInstance();
        using=fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        // initializing recycler view to be linear layout
        recyclerView=findViewById(R.id.recyclermenucategory);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        using=fAuth.getCurrentUser();
try {
    loadaction();
}catch (Exception e){

    Toast.makeText(this, "error" +e, Toast.LENGTH_LONG).show();
}




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
                        Intent intent=new Intent(categories.this,home.class);
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



}