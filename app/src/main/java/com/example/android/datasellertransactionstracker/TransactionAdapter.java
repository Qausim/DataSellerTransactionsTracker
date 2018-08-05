package com.example.android.datasellertransactionstracker;

/**
 * Created by HP on 8/5/2018.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Adapter class for a list of transactions to displayed in the RecyclerView.
 * */
public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {

        public TransactionViewHolder(View itemView) {
            super(itemView);
        }
    }
}
