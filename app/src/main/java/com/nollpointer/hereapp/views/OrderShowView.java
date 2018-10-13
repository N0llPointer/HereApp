package com.nollpointer.hereapp.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nollpointer.hereapp.MainActivity;
import com.nollpointer.hereapp.Order;
import com.nollpointer.hereapp.R;
import com.nollpointer.hereapp.adapters.BuyCardsAdapter;

import java.util.ArrayList;

public class OrderShowView extends LinearLayout {

    public static interface Listener{
        void onClosed();

        void onChoose(Order order);
    }

    private Listener listener;
    private MainActivity activity;

    private RecyclerView recycler;
    private Toolbar toolbar;

    private TextView address;

    private Order order;

    public OrderShowView(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.fragment_order,this);

        activity = ((MainActivity) context);

        recycler = findViewById(R.id.order_fragment_recycler_view);
        address = findViewById(R.id.order_fragment_address);
        toolbar = findViewById(R.id.order_fragment_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(GONE);
                listener.onClosed();
            }
        });

        Button button = findViewById(R.id.order_fragment_choose);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onChoose(order);
                hide();
                listener.onClosed();
            }
        });



        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.showAddress(order.getCoordinates());
                hide();
                listener.onClosed();
            }
        });

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));


        setVisibility(GONE);

    }

    public void setListener(Listener listener){
        this.listener = listener;
    }


    public void setInfo(Order order){
        this.order = order;

        address.setText(order.getAddress());

        ArrayList<String> list = new ArrayList<>();
        list.addAll(order.getProducts().keySet());

        ArrayList<Integer> counts = new ArrayList<>();
        counts.addAll(order.getProducts().values());

        recycler.setAdapter(new BuyCardsAdapter(list,counts));


        setVisibility(VISIBLE);
    }


    public void hide(){
        setVisibility(GONE);
    }

    public boolean isShown(){
        return getVisibility() == VISIBLE;
    }

}
