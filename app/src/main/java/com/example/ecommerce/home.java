package com.example.ecommerce;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.jaeger.library.StatusBarUtil;
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
    FloatingActionMenu fmain;
    FloatingActionButton floatcart,floatsetting,floatfeed;
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
    protected void onStart() {
        super.onStart();
fmain.close(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Paper.init(this);
        StatusBarUtil.setTransparent(this);




         aq=getIntent().getStringExtra("categoryname");



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

       fmain=  findViewById(R.id.floatmenu);

       try {
           floatcart=findViewById(R.id.fcartbtn);
           floatfeed=findViewById(R.id.ffeedbtn);
           floatsetting=findViewById(R.id.fsettingbtn);

       }catch (Exception e){
           Toast.makeText(this, "error"+e, Toast.LENGTH_LONG).show();
       }





        floatfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),feed.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        floatsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),settings.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
        floatcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),cart.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });



//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//              startActivity(new Intent(getApplicationContext(),cart.class));
//                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
//            }
//        });


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

    private void logout() {
        android.app.AlertDialog dialog = new AlertDialog.Builder(this,R.style.AlertDialogStyle)
                .setTitle("LOG OUT")
                .setMessage("Hope You Remember Your Password!!")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Paper.book().write(prevalent.userpasskey,"");
                        Paper.book().write(prevalent.useremailkey,"");
                        startActivity(new Intent(getApplicationContext(),start.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
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
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        return true;
//    }
    @Override
    public void onBackPressed() {


        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        if(nav.isDrawerOpen(GravityCompat.START)){
            nav.closeDrawer(GravityCompat.START);
        }else {

        }
        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "click BACK again to exit", Toast.LENGTH_SHORT).show();



        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_cart:

//                Snackbar.make(findViewById(R.id.recyclermenumain), "techkey", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(new Intent(getApplicationContext(),cart.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                break;
            case R.id.nav_feed:
                startActivity(new Intent(getApplicationContext(),feed.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case  R.id.nav_settings:
                startActivity(new Intent(getApplicationContext(),settings.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.nav_about:
                startActivity(new Intent(getApplicationContext(),about.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.nav_category:
                startActivity(new Intent(getApplicationContext(),categories.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.nav_logout:
               logout();
                break;
            case R.id.nav_offer:

                Intent intent=new Intent(home.this,offers.class);
                intent.putExtra("categoryname","Lunch");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                break;

        }
        nav.closeDrawer(GravityCompat.START);

        return true;
    }

}