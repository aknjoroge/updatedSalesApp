package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

public class changelocation extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {
    Spinner changelocations;
    EditText settown;
    String loc;
    String stakenews, stakenew;
    FirebaseFirestore fStore;
    String userid;
    FirebaseAuth fAuth;
    StorageReference storageReference;
    Button updatelocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changelocation);
        StatusBarUtil.setTransparent(this);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userid = fAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
        settown = findViewById(R.id.txtchangeslocation);
        updatelocation = findViewById(R.id.setsignupbtn);
        updatelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(settown.getText().toString())) {
                    Toast.makeText(changelocation.this, "Set a location", Toast.LENGTH_SHORT).show();
                } else {
                    String ltown = settown.getText().toString();
                    String mtown = loc;

                    Map<String, Object> usermaps = new HashMap<>();
                    usermaps.put("Main City", mtown);
                    usermaps.put("local Town", ltown);
                    final DocumentReference documentReference = fStore.collection("users").document(userid);

                    documentReference.set(usermaps, SetOptions.merge());

                    Toast.makeText(changelocation.this, "update succesfull", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), account.class));
                }
            }
        });
        changelocations = findViewById(R.id.setspinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.locations, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        changelocations.setAdapter(arrayAdapter);
        changelocations.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        loc = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, loc, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}