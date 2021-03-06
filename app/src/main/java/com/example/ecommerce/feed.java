package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class feed extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    @Override
    protected void onStart() {
        super.onStart();
        bottomNavigation.show(2,true);
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



    }
}