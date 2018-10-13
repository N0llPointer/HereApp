package com.nollpointer.hereapp.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nollpointer.hereapp.MainActivity;
import com.nollpointer.hereapp.Order;
import com.nollpointer.hereapp.R;
import com.nollpointer.hereapp.adapters.BuyCardsAdapter;

import java.util.ArrayList;
import java.util.Arrays;


public class OrderFragment extends Fragment {
    private View mainView;
    private RecyclerView recycler;
    private Toolbar toolbar;

    private TextView address;

    private Order order;

    private ArrayList<String> products = new ArrayList<>();



    public static OrderFragment getInstance(Order order){
        OrderFragment fragment = new OrderFragment();

        fragment.order = order;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView =  inflater.inflate(R.layout.fragment_order, container, false);
        fillList();

        recycler = mainView.findViewById(R.id.order_fragment_recycler_view);
        address = mainView.findViewById(R.id.order_fragment_address);
        toolbar = mainView.findViewById(R.id.order_fragment_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).getSupportFragmentManager().popBackStack();
            }
        });

        address.setText(order.getAddress());

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).showAddress(order.getCoordinates());
            }
        });

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<String> list = new ArrayList<>();
        list.addAll(order.getProducts().keySet());

        recycler.setAdapter(new BuyCardsAdapter(list));
        return mainView;
    }

    private void fillList(){
        String prods = "Яйца Молоко Пиво Колбаса Сосиски Хлеб Чипсы Кефир Чай Свинина Курица Майонез Сыр Помидоры Картофель Яблоки Кетчуп Рыба Апельсины Кофе Водка Грибы Макароны Каша Рис";
        String[] positions = prods.toLowerCase().split(" ");
        products.addAll(Arrays.asList(positions));

    }

}
