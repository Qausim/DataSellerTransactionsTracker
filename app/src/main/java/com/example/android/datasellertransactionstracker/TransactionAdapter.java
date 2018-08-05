package com.example.android.datasellertransactionstracker;

/**
 * Created by HP on 8/5/2018.
 */

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.android.datasellertransactionstracker.data.TransactionContract.*;
/**
 * Adapter class for a list of transactions to displayed in the RecyclerView.
 * */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    ArrayList<Transaction> transactions;
    TransactionItemClickListener mOnClickListener;

    public TransactionAdapter(ArrayList<Transaction> transactions, TransactionItemClickListener
                              listener) {
        this.transactions = transactions;
        this.mOnClickListener = listener;
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int transactionItemLayoutId = R.layout.transaction_item_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachToParentImmediately = false;

        View view = inflater.inflate(transactionItemLayoutId, parent, attachToParentImmediately);

        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        // Get the current transaction
        Transaction currentTransaction = transactions.get(position);
        // Set its id
        holder.nameTextView.setText(currentTransaction.getName());
        // Set its cost
        holder.costTextView.setText(String.valueOf(currentTransaction.getCost()));

        // Get the paymentState
        switch (currentTransaction.getPaymentState()) {
            case TransactionEntry.PAID:
                holder.paymentStateTextView.setText(Resources.getSystem().getString(R.string.paid));
                break;
            case TransactionEntry.PENDING:
                holder.paymentStateTextView.setText(Resources.getSystem().getString(R.string
                        .pending));
        }
        // Get the date
        long dateTime = currentTransaction.getDate();
        // Create a simple format for formatting the date and time
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:MM a");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd mmm, yyyy");

        // Create a new Date object using passing date in as an argument.
        Date date = new Date(dateTime);
        // Format the time in the form 11:33 am, and the date in the form 03 Aug, 2012
        String timeString = timeFormat.format(date);
        String dateString = dateFormat.format(date);
        // Set the content of the timeTextView
        holder.timeTextView.setText(timeString);
        // Set the content of the dateTextView
        holder.dateTextView.setText(dateString);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameTextView, costTextView, paymentStateTextView, timeTextView, dateTextView;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_name);
            costTextView = itemView.findViewById(R.id.tv_cost);
            paymentStateTextView = itemView.findViewById(R.id.tv_payment_state);
            timeTextView = itemView.findViewById(R.id.tv_time);
            dateTextView = itemView.findViewById(R.id.tv_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedItemIndex = getAdapterPosition();
            mOnClickListener.onTransactionItemClicked(clickedItemIndex);

        }
    }

    public interface TransactionItemClickListener {
        void onTransactionItemClicked(int itemIndex);
    }
}
