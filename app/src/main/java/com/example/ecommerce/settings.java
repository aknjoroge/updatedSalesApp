package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import io.paperdb.Paper;

public class settings extends AppCompatActivity {
Button dells;
    FirebaseUser using;
    String newpp;
    String takechecklock;
    FirebaseAuth fAuth;
    Switch forlocks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Paper.init(this);
        fAuth = FirebaseAuth.getInstance();

        using = fAuth.getCurrentUser();
        forlocks=findViewById(R.id.lockswitch);

        takechecklock = Paper.book().read(prevalent.lockstatkey);
        if(takechecklock.equals("locked")){
            forlocks.setChecked(true);
        }else {

        }

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