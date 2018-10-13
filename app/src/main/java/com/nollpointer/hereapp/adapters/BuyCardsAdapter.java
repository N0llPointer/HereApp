package com.nollpointer.hereapp.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nollpointer.hereapp.R;

import java.util.ArrayList;
import java.util.List;

public class BuyCardsAdapter extends RecyclerView.Adapter<BuyCardsAdapter.ViewHolder>{
    private List<String> products;
    private List<Integer> counts;

    public BuyCardsAdapter(List<String> products, List<Integer> counts) {
        this.products = products;
        this.counts = counts;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;

        ViewHolder(CardView c) {
            super(c);
            mCardView = c;
        }

    }

    @Override
    public BuyCardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView c = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_card, parent, false);
        return new BuyCardsAdapter.ViewHolder(c);
    }

    @Override
    public void onBindViewHolder(BuyCardsAdapter.ViewHolder holder, final int position) {
        CardView card = holder.mCardView;
        TextView text = card.findViewById(R.id.buy_card_title);
        text.setText(products.get(position));
        text = card.findViewById(R.id.buy_card_count);
        text.setText(counts.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

}