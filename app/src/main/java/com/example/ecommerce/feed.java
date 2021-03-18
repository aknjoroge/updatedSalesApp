package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.jaeger.library.StatusBarUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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


    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestperm();
            }
        },1000);


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        StatusBarUtil.setTransparent(this);



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

    public void requestperm(){
        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();

    }


    private void loaditems() {

        FirestoreRecyclerOptions<forfeed> options = new FirestoreRecyclerOptions.Builder<forfeed>()
                .setQuery(fStore.collection("feed").document("uploads").collection("forall"), forfeed.class).build();
        FirestoreRecyclerAdapter<forfeed, feedviewholder> adapter = new FirestoreRecyclerAdapter<forfeed, feedviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final feedviewholder holder, int position, @NonNull final forfeed model) {
                holder.name.setText(model.getName());


                Picasso.get().load(model.getFilepath()).into(holder.profile);
                holder.likesno.setText(model.getLikes());
                globalrandomkey=model.getRandomKey();


                holder.download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.app.AlertDialog dialog = new AlertDialog.Builder(feed.this,R.style.AlertDialogStyle)
                                .setMessage("Download image?")

                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                       downloadpic(holder.profile.getDrawable());
                                    }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();


                    }
                });

                holder.viewmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(feed.this,home.class);
                        intent.putExtra("categoryname",model.getCategory());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }
                });
                holder.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent= new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");

                        String sharebody="Download the E_commerce app from www.techkey.co.ke/app to get access to the best" +
                                "available sales system. ";
                        intent.putExtra(Intent.EXTRA_SUBJECT,"subject here");
                        intent.putExtra(Intent.EXTRA_TEXT,sharebody);
                        startActivity(Intent.createChooser(intent,"Share via"));


                    }
                });

                holder.like.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
                        String likestake = model.getLikes();
                            int getlikes = Integer.parseInt(likestake);
                            int add = getlikes + 1;
                            String value = String.valueOf(add);
                            holder.likesno.setText(value);
                            try {
                                updatedata(add, model.getRandomKey());
                            } catch (Exception e) {
                                Toast.makeText(feed.this, "error in likes: " + e, Toast.LENGTH_SHORT).show();
                            }
                        holder.like.setLiked(true);

                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {
                        String likestake = holder.likesno.getText().toString();
                            int getlikes = Integer.parseInt(likestake);
                            int minus = getlikes - 1;
                            String valuenew = String.valueOf(minus);
                            holder.likesno.setText(valuenew);
                            try {
                                updatedata(minus, model.getRandomKey());
                            } catch (Exception e) {
                                Toast.makeText(feed.this, "error in likes: " + e, Toast.LENGTH_SHORT).show();
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

    private void downloadpic(Drawable drawable) {
        OutputStream outStream = null;
        try {
            BitmapDrawable draw = (BitmapDrawable) drawable;
            Bitmap bitmap = draw.getBitmap();

            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/PorkPit_images");
            dir.mkdirs();

            String fileName = String.format("%d.jpg", System.currentTimeMillis());
            File outFile = new File(dir, fileName);
            outStream= new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

            outStream.flush();

            outStream.close();

        }catch (Exception e){

            (new AlertDialog.Builder(this)).setTitle("File Permission Not Granted!")
                    .setMessage("You Need To Enable The permissions in settings." +
                            "Go To Settings > Apps > Porkpit > Permissions : and turn on permissions.")
                    .setPositiveButton("open Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.parse("package:"+BuildConfig.APPLICATION_ID));
                                startActivity(intent);
                            }catch (Exception e){
                                Toast.makeText(feed.this, "Error "+e, Toast.LENGTH_SHORT).show();
                            }


                        }
                    }).show();

                    //.setPositiveButton("OK", null).create().show();


            return;
        }
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();


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