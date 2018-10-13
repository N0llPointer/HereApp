package com.nollpointer.hereapp.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nollpointer.hereapp.R;
import com.nollpointer.hereapp.adapters.BuyCardsAdapter;

import java.util.ArrayList;
import java.util.Arrays;


public class BuyFragment extends Fragment {
    private View mainView;
    private RecyclerView recycler;
    private FloatingActionButton fab;

    private ArrayList<String> products = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView =  inflater.inflate(R.layout.fragment_buy, container, false);
        fillList();
        recycler = mainView.findViewById(R.id.buy_recycler_view);
        fab = mainView.findViewById(R.id.buy_inside_cart);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(new BuyCardsAdapter(products));
        return mainView;
    }

    private void fillList(){
        String prods = "Яйца Молоко Пиво Колбаса Сосиски Хлеб Чипсы Кефир Чай Свинина Курица Майонез Сыр Помидоры Картофель Яблоки Кетчуп Рыба Апельсины Кофе Водка Грибы Макароны Каша Рис";
        String[] positions = prods.toLowerCase().split(" ");
        products.addAll(Arrays.asList(positions));

    }


}
