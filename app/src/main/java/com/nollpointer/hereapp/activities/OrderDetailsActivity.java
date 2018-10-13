package com.nollpointer.hereapp.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nollpointer.hereapp.MainActivity;
import com.nollpointer.hereapp.Order;
import com.nollpointer.hereapp.R;
import com.nollpointer.hereapp.adapters.BuyCardsAdapter;
import com.nollpointer.hereapp.views.OrderShowView;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private Toolbar toolbar;

    private TextView address;

    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        recycler = findViewById(R.id.order_fragment_recycler_view);
        address = findViewById(R.id.order_fragment_address);
        toolbar = findViewById(R.id.order_fragment_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button button = findViewById(R.id.order_fragment_choose);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        recycler.setLayoutManager(new LinearLayoutManager(this));

    }


    public void setInfo(Order order){
        this.order = order;

        address.setText(order.getAddress());

        ArrayList<String> list = new ArrayList<>();
        list.addAll(order.getProducts().keySet());

        ArrayList<Integer> counts = new ArrayList<>();
        counts.addAll(order.getProducts().values());

        recycler.setAdapter(new BuyCardsAdapter(list,counts));

    }


}
