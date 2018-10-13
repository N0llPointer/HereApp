package com.nollpointer.hereapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.here.android.mpa.common.GeoCoordinate;
import com.nollpointer.hereapp.fragments.LoginFragment;
import com.nollpointer.hereapp.fragments.MapsFragment;
import com.nollpointer.hereapp.fragments.OrderFragment;

import java.util.ArrayList;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "HereApp";

    private MapsFragment mapsFragment;
    private LoginFragment loginFragment;
    private OrderFragment orderFragment;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference shopsDatabaseReference;

    private ArrayList<Order> orders;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        getWindow().setStatusBarColor(Color.TRANSPARENT);

        int permissionWrite = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionRecord = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionRecord != PackageManager.PERMISSION_GRANTED && permissionWrite != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }

        mapsFragment = new MapsFragment();
        loginFragment = new LoginFragment();

        firebaseDatabase = FirebaseDatabase.getInstance();
        shopsDatabaseReference = firebaseDatabase.getReference().child("shops");

        orders = new ArrayList<>();

        //initOrders();

        getSupportFragmentManager().beginTransaction().add(R.id.main_framelayout,loginFragment).commit();
    }

    public void initOrders(){
        TreeMap<String,Integer> map = new TreeMap<>();

        map.put("Яйца",10);
        map.put("Пиво",2);
        map.put("Хлеб",1);

        Order order = new Order("ул. Пушкина, 12, кв. 101",new GeoCoordinate(45.041334, 41.962644),map);

        orders.add(order);
        orders.add(order);
        orders.add(order);

    }

    public Order getOrder(int pos){
        return orders.get(pos);
    }

    public void showMapsFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout,mapsFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if(!mapsFragment.onBackPressed())
            super.onBackPressed();
    }

    public void showAddress(GeoCoordinate coordinate){
        mapsFragment.showAddress(coordinate);
    }

    public void addViewToMainFrameLayout(View view){
        ((FrameLayout) findViewById(R.id.main_framelayout)).addView(view);
    }
}
