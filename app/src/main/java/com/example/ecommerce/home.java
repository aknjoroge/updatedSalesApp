package com.example.ecommerce;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.paperdb.Paper;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
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
    String userid;
    MeowBottomNavigation bottomNavigation;
    String aq;
    TextView pname,pmail;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Paper.init(this);
        //bottom nav
        bottomNavigation=findViewById(R.id.bottomnavnew);
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.category_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.feed_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.account_box_24));

         aq=getIntent().getStringExtra("categoryname");
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

            }
        });
bottomNavigation.setCount(1,"4");
bottomNavigation.show(1,true);




        fAuth = FirebaseAuth.getInstance();
        using=fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        userid = fAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataloc=aq;

        recyclerView=findViewById(R.id.recyclermenumain);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        nav = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, nav, toolbar, R.string.nav_opener, R.string.nav_closer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toggle.getDrawerArrowDrawable().setColor(getColor(R.color.white));
        }
        nav.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);
        pname=headerview.findViewById(R.id.profilename);
        pmail=headerview.findViewById(R.id.profilemail);
        himage=headerview.findViewById(R.id.profileimage);

try{
    loadaction();
   loaddetails2();
    loadprofile();
}catch (Exception e){
    Toast.makeText(this, "Error: "+e, Toast.LENGTH_LONG).show();
}



    }

    private void loaddetails2() {
        final DocumentReference documentReference = fStore.collection("users").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                pname.setText(documentSnapshot.getString("name"));
                pmail.setText(documentSnapshot.getString("mail"));
            }
        });


    }
    public void loadprofile(){
        StorageReference profileref = storageReference.child("Userprofile/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(himage);
                // Picasso.get().load(uri).into(nav_image);

            }
        });
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
                        Intent intent=new Intent(home.this,productdetails.class);
                        intent.putExtra("pid",model.getRandomKey());
                        intent.putExtra("cid",model.getCategory());

                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public productviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.productitem,parent,false);
                productviewholder holder =new productviewholder(view);
                return holder;



            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        if(nav.isDrawerOpen(GravityCompat.START)){
            nav.closeDrawer(GravityCompat.START);
        }else {super.onBackPressed();}

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:

                Snackbar.make(findViewById(R.id.recyclermenumain), "techkey", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                break;
            case R.id.nav_gallery:
                Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
                break;

        }
        nav.closeDrawer(GravityCompat.START);

        return true;
    }

}