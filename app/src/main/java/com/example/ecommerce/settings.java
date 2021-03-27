package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jaeger.library.StatusBarUtil;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class settings extends AppCompatActivity {
Button dells,updates,toaccount;
    FirebaseUser using;
    String userid;
    FirebaseFirestore fStore;
    TextView name;
    ImageView settingsdp;
    ImageView pit,tuwashow,techkey;
    String takechecklock;
    FirebaseAuth fAuth;
    StorageReference storageReference;
    Switch forlocks,darkmode;
    ProgressDialog loadBar;

    @Override
    protected void onStart() {
        super.onStart();
        loadBar.hide();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Paper.init(this);
        StatusBarUtil.setTransparent(this);

        fAuth = FirebaseAuth.getInstance();
        darkmode=findViewById(R.id.dakmodeswitch);
        fStore=FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        using = fAuth.getCurrentUser();
        userid=fAuth.getCurrentUser().getUid();

        loadBar=new ProgressDialog(this,R.style.ProgressbarStyle);
        loadBar.setTitle("");
        loadBar.setMessage("Checking for updates...");
        loadBar.setIcon(R.drawable.update_alt_24);
        loadBar.setCanceledOnTouchOutside(false);

        loadBar.hide();

        updates=findViewById(R.id.checkupdatebtn);
        toaccount=findViewById(R.id.accountsbtn);

        toaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),account.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
        updates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadBar.show();
              new Handler().postDelayed(new Runnable() {
                  @Override
                  public void run() {
                      loadBar.hide();
                      Toast.makeText(settings.this, "No updates available", Toast.LENGTH_SHORT).show();
                  }
              },4000);
            }
        });

        pit=findViewById(R.id.porkpitimg);
        tuwashow=findViewById(R.id.tuwashowmimg);
        techkey=findViewById(R.id.techkeyimg);

        pit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Snackbar snackbar= Snackbar.make(findViewById(R.id.settinglayout), "THE PORK PIT", Snackbar.LENGTH_LONG)
                        .setAction("Action", null);
                View snackbarView = snackbar.getView();
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
                params.topMargin =50;
                params.gravity = Gravity.TOP;
                snackbarView.setLayoutParams(params);
                snackbar.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        snackbar.dismiss();
                    }
                },2000);


            }
        });

        tuwashow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Snackbar snackbar= Snackbar.make(findViewById(R.id.settinglayout), " TUWASHOW AFRICA", Snackbar.LENGTH_LONG)
                        .setAction("Action", null);
                View snackbarView = snackbar.getView();

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
                params.topMargin =50;
                params.gravity = Gravity.TOP;
                snackbarView.setLayoutParams(params);
                snackbar.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        snackbar.dismiss();
                    }
                },2000);


            }
        });

        techkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Snackbar snackbar= Snackbar.make(findViewById(R.id.settinglayout), " TECHKEY Ltd", Snackbar.LENGTH_LONG)
                        .setAction("Action", null);
                View snackbarView = snackbar.getView();

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
                params.topMargin =50;

                params.gravity = Gravity.TOP;
                snackbarView.setLayoutParams(params);
                snackbar.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        snackbar.dismiss();
                    }
                },2000);

            }
        });

        name=findViewById(R.id.insettingname);
        settingsdp=findViewById(R.id.dptwoinsetting);
        forlocks=findViewById(R.id.lockswitch1);
        Paper.init(this);
        takechecklock = Paper.book().read(prevalent.lockstatkey);
        if(takechecklock.equals("locked")){
            forlocks.setChecked(true);
        }else {

        }

        darkmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (darkmode.isChecked()){
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else {
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                }
            }
        });

        dells = findViewById(R.id.delbtn);
        dells.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dellaccounts();
                // Toast.makeText(accounts.this, " Not Yet Initialized", Toast.LENGTH_SHORT).show();
            }
        });

        forlocks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(forlocks.isChecked()){

                    Toast.makeText(settings.this, "App locked", Toast.LENGTH_SHORT).show();
                    String sec="locked";
                    Paper.book().write(prevalent.lockstatkey,sec);
                }
                else{
                    Toast.makeText(settings.this, "App unlocked", Toast.LENGTH_SHORT).show();
                    String sec="unlocked";
                    Paper.book().write(prevalent.lockstatkey,sec);
                }
            }
        });
        autochangeprofile2();
        loadprofilepic();
    }

    public void loadprofilepic(){


        StorageReference profileref = storageReference.child("Userprofile/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //  Toast.makeText(settings.this, "Loading profile", Toast.LENGTH_SHORT).show();
                Picasso.get().load(uri).into(settingsdp);
                // Picasso.get().load(uri).into(nav_image);

            }
        });

    }
    private void autochangeprofile2() {
        final DocumentReference documentReference = fStore.collection("users").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String nametake=documentSnapshot.getString("name");

                name.setText(nametake);


            }
        });
    }

    public void dellaccounts() {

        final EditText dellac = new EditText(this);
        android.app.AlertDialog dialog = new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                .setTitle("DELETE ACCOUNT AND LOOSE DATA!!!")
                .setMessage("Enter The Word 'DELETE' TO CONFIRM")
                .setView(dellac)
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(dellac.getText().toString())) {
                            String in = dellac.getText().toString();
                            if (in.equals("DELETE")) {
                                using.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(settings.this, "YOU HAVE BENN DELETED CLOSING APP", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(settings.this, "Delete was Not Successfull", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                Toast.makeText(settings.this, "DELETE not typed Correctly", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(settings.this, "Field is empty", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(settings.this, "Deleting Cancelled", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();

    }

}