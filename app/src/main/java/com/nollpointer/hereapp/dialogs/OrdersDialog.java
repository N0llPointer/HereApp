package com.nollpointer.hereapp.dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nollpointer.hereapp.R;
import com.nollpointer.hereapp.adapters.OrderDialogAdapter;

public class OrdersDialog extends BottomSheetDialogFragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.orders_dialog_layout,container,false);
        RecyclerView recyclerView = mainView.findViewById(R.id.order_dialog_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new OrderDialogAdapter());
        return mainView;
    }
}
