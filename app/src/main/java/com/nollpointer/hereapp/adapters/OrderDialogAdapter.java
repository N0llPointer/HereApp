package com.nollpointer.hereapp.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nollpointer.hereapp.R;

public class OrderDialogAdapter extends RecyclerView.Adapter<OrderDialogAdapter.ViewHolder> {
    Listener listener;

    public static interface Listener {
        void onClick(int position);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;

        ViewHolder(CardView c) {
            super(c);
            mCardView = c;
        }

    }

    @Override
    public OrderDialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView c = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card, parent, false);
        return new OrderDialogAdapter.ViewHolder(c);
    }

    @Override
    public void onBindViewHolder(OrderDialogAdapter.ViewHolder holder, final int position) {
        CardView card = holder.mCardView;
        TextView text = card.findViewById(R.id.order_dialog_title);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}