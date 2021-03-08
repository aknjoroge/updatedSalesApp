package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class feed extends AppCompatActivity {
    DrawerLayout nav;
    FirebaseAuth fAuth;
    String htm;
    String loc ;
    String delkey;
    StorageReference storageReference;
    String globalrandomkey;
    public RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseUser using;
    FirebaseFirestore fStore;
    ImageView himage;
    String userid;
    TextView pname, pmail;

    MeowBottomNavigation bottomNavigation;
    @Override
    protected void onStart() {
        super.onStart();
        bottomNavigation.show(2,true);
        bottomNavigation.setCount(2,"1");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        bottomNavigation=findViewById(R.id.bottomnavnew);
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.category_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.feed_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.account_box_24));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

            }
        });
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()){
                    case 1:
                        startActivity(new Intent(getApplicationContext(),categories.class));
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(),feed.class));
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(),account.class));
                        break;

                }
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
//                switch (item.getId()){
//                    case 2:
//                        startActivity(new Intent(getApplicationContext(),feed.class));
//                        break;
//                }

            }
        });
        bottomNavigation.setCount(1,"4");



        fAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.feedrecysler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        using = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        userid = fAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();

        try{
          loaditems();

        }
        catch (Exception e){
            Toast.makeText(this, "Exception: "+e, Toast.LENGTH_SHORT).show();
        }

    }




    private void loaditems() {

        FirestoreRecyclerOptions<forfeed> options = new FirestoreRecyclerOptions.Builder<forfeed>()
                .setQuery(fStore.collection("feed").document("uploads").collection("forall"), forfeed.class).build();
        FirestoreRecyclerAdapter<forfeed, feedviewholder> adapter = new FirestoreRecyclerAdapter<forfeed, feedviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final feedviewholder holder, int position, @NonNull final forfeed model) {
                holder.name.setText("Name: " + model.getName());


                Picasso.get().load(model.getFilepath()).into(holder.profile);
                holder.likesno.setText(model.getLikes());
                globalrandomkey=model.getRandomKey();


                holder.viewmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(feed.this,home.class);
                        intent.putExtra("categoryname",model.getCategory());

                        startActivity(intent);
                    }
                });
                holder.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(findViewById(R.id.feedrecysler), "techkey is working on it", Snackbar.LENGTH_LONG)
                     .setAction("Action", null).show();
                    }
                });
                holder.like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(feed.this, "liked", Toast.LENGTH_SHORT).show();

                        String likestake=model.getLikes();
                        int getlikes=Integer.parseInt(likestake);
                        int add= getlikes+1;
                        String value=String.valueOf(add);
                        holder.likesno.setText(value);

                        try {
                           updatedata(add,model.getRandomKey());
                        }catch (Exception e){
                            Toast.makeText(feed.this, "error in likes: "+e, Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }

            @NonNull
            @Override
            public feedviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feeditem, parent, false);
                feedviewholder holder = new feedviewholder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }



    private void updatedata(int taken,String keys) {

        String takenew = String.valueOf(taken);


        Map<String,Object> usermaps =new HashMap<>();
        usermaps.put("likes",takenew);
        final DocumentReference documentReference = fStore.collection("feed").document("uploads")
                .collection("forall").document(keys);

        documentReference.set(usermaps, SetOptions.merge());



    }
}