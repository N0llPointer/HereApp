package com.nollpointer.hereapp;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.util.Arrays;
import java.util.Random;
import java.util.TreeMap;

import static android.media.MediaCodec.MetricsConstants.MODE;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "HereApp";

    private MapsFragment mapsFragment;
    private LoginFragment loginFragment;
    private OrderFragment orderFragment;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference shopsDatabaseReference;

    private ArrayList<Order> orders;

    private boolean isFromIntent = false;
    private boolean isTestRoute;

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

        SharedPreferences prefs = this.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        isTestRoute = prefs.getBoolean("ShowTestRouteInfo",false) || getIntent().getDoubleExtra("Coords",0) != 0;
        Log.wtf(TAG, "onCreate: " + isTestRoute);

        mapsFragment = new MapsFragment();
        loginFragment = new LoginFragment();

        firebaseDatabase = FirebaseDatabase.getInstance();
        shopsDatabaseReference = firebaseDatabase.getReference().child("shops");

        orders = new ArrayList<>();

        //initOrders();

        new setNewPreferences(isTestRoute).doInBackground(this);


        if(isTestRoute){
            getSupportFragmentManager().beginTransaction().add(R.id.main_framelayout,mapsFragment).commit();
            //isFromIntent = true;
            return;
        }

        getSupportFragmentManager().beginTransaction().add(R.id.main_framelayout,loginFragment).commit();
    }

    public boolean isTestRoute(){
        return isTestRoute;
    }

    public void initOrders(){
        //TreeMap<String,Integer> map = new TreeMap<>();

//        map.put("Яйца",10);
//        map.put("Пиво",2);
//        map.put("Хлеб",1);

        Order order0 = new Order("ул. Пушкина, 12, кв. 666",new GeoCoordinate(45.037049, 41.983381),getMap());
        Order order1 = new Order("ул. Макарова, 13, кв. 12",new GeoCoordinate(45.046667, 41.963441),getMap());
        Order order2 = new Order("ул. Мира, 14, кв. 44",new GeoCoordinate(45.033755, 41.936952),getMap());
        Order order3 = new Order("ул. Пирогова, 15, кв. 1",new GeoCoordinate(45.035776, 41.951661),getMap());
        Order order4 = new Order("ул. Тельмана, 166, кв. 989",new GeoCoordinate(45.034540, 41.963892),getMap());
        orders.add(order0);
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        orders.add(order4);

    }

    public ArrayList<Order> getOrders(){
        return orders;
    }

    public boolean isFromIntent(){
        return isFromIntent;
    }

    private TreeMap<String,Integer> getMap(){

        Random random = new Random();
        TreeMap<String,Integer> treeMap = new TreeMap<>();
        String prods = "Яйца Молоко Пиво Колбаса Сосиски Хлеб Чипсы Кефир Чай Свинина Курица Майонез Сыр Помидоры Картофель Яблоки Кетчуп Рыба Апельсины Кофе Водка Грибы Макароны Каша Рис";
        String[] positions = prods.toLowerCase().split(" ");

        int max = random.nextInt(6) + 3;

        for(int i=0;i<max;i++){
            treeMap.put(positions[random.nextInt(positions.length)],random.nextInt(10));
        }

        return treeMap;

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

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    protected static class setNewPreferences extends AsyncTask<MainActivity,Void,Void> {
        boolean isTest;

        setNewPreferences(boolean mode){
            this.isTest = mode;
        }

        @Override
        protected Void doInBackground(MainActivity... mainActivities) {
            SharedPreferences.Editor editor = mainActivities[0].getSharedPreferences("SETTINGS",Context.MODE_PRIVATE).edit();
            editor.putBoolean("ShowTestRouteInfo",false);
            editor.apply();
            return null;
        }
    }

}
