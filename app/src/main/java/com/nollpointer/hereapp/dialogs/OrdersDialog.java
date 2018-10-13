package com.nollpointer.hereapp.dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nollpointer.hereapp.Order;
import com.nollpointer.hereapp.R;
import com.nollpointer.hereapp.adapters.OrderDialogAdapter;

import java.util.ArrayList;

public class OrdersDialog extends BottomSheetDialogFragment{

    OrderDialogAdapter.Listener listener;
    RecyclerView recyclerView;

    ArrayList<Order> orders;

    public void setListener(OrderDialogAdapter.Listener listener){
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.orders_dialog_layout,container,false);
        recyclerView = mainView.findViewById(R.id.order_dialog_recycler);

        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());

        DividerItemDecoration dividerItemDecoration =new DividerItemDecoration(recyclerView.getContext(),
                linearLayout.getOrientation());

        //dividerItemDecoration.setDrawable(g);


        recyclerView.setLayoutManager(linearLayout);
        recyclerView.addItemDecoration(dividerItemDecoration);

        OrderDialogAdapter adapter = new OrderDialogAdapter(orders);
        adapter.setListener(listener);
        recyclerView.setAdapter(adapter);

        return mainView;
    }


    public void setInfo(ArrayList<Order> list){
        this.orders = list;
    }

}
