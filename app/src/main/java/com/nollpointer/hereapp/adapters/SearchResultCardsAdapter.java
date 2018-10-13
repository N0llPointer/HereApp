package com.nollpointer.hereapp.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nollpointer.hereapp.R;

public class SearchResultCardsAdapter extends RecyclerView.Adapter<SearchResultCardsAdapter.ViewHolder> {
//
//    public interface Listener {
//        void onClick(int position);
//    }
//
//    public void setListener(Listener listener) {
//        mListener = listener;
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;

        ViewHolder(CardView c) {
            super(c);
            mCardView = c;
        }

    }

    @Override
    public SearchResultCardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView c = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_card, parent, false);
        return new ViewHolder(c);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CardView card = holder.mCardView;
        TextView text = card.findViewById(R.id.search_card_text);
        text.setText("Some search result");
    }

    @Override
    public int getItemCount() {
        return 1;
    }

}

