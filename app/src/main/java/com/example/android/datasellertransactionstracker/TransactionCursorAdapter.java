package com.example.android.datasellertransactionstracker;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.android.datasellertransactionstracker.data.TransactionContract.*;

/**
 * Created by Qausim on 8/11/2018.
 * CursorAdapter class governing data supply to the listview of MainActivity
 */

public class TransactionCursorAdapter extends CursorAdapter {
    // The UI components
    private TextView nameTextView, summaryTextView;

    public TransactionCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Item layout.xml id
        int itemLayoutId = R.layout.transaction_item_layout;
        // Inflate the layout
        return LayoutInflater.from(context)
                .inflate(itemLayoutId, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Bind the UI components objects
        nameTextView = view.findViewById(R.id.tv_name);
        summaryTextView = view.findViewById(R.id.tv_summary);

        // Get data from the cursor
        String name = cursor.getString(cursor.getColumnIndex(TransactionEntry.NAME));
        String unit = cursor.getString(cursor.getColumnIndex(TransactionEntry.UNIT));
        int paymentState = cursor.getInt(cursor.getColumnIndex(TransactionEntry.PAYMENT_STATE));
        int title = cursor.getInt(cursor.getColumnIndex(TransactionEntry.TITLE));

        // If it is a transaction with a customer
        if (title == TransactionEntry.CUSTOMER) {
            // If the customer has paid
            if (paymentState == TransactionEntry.PAID) {
                // Then set the distinct background color depicting a transaction with a customer
                // that has been paid for
                view.setBackgroundColor(context.getResources().getColor(R.color
                        .customer_paid_background_color));
                // Set the summary text that informs the customer has paid
                // and what s/he bought
                summaryTextView.setText(context.getResources().getString(R.string.bought) + " " +
                        unit + " " + context.getResources().getString(R.string.has_paid));
            } else {// If customer has not paid
                // Then set the distinct background color depicting a transaction with a customer
                // that has not been paid for
                view.setBackgroundColor(context.getResources().getColor(R.color
                        .customer_pending_background_color));
                // Set the summary text that informs the customer has not paid
                // and what s/he bought
                summaryTextView.setText(context.getResources().getString(R.string.bought) + " " +
                        unit + " " + context.getResources().getString(R.string.yet_to_pay));
            }
        } else {// If the transaction is with a service provider
            // If user has paid
            if (paymentState == TransactionEntry.PAID) {
                // Then set the distinct background color depicting a transaction with a service
                // provider that has been paid for
                view.setBackgroundColor(context.getResources().getColor(R.color
                        .seller_paid_background_color));
                // Set the summary text that informs the user has paid
                // and what s/he bought
                summaryTextView.setText(context.getResources().getString(R.string.seller_bought) +
                        " " + unit + " " + context.getResources().getString(R.string.seller_has_paid));
            } else {// If user has not paid
                // Then set the distinct background color depicting a transaction with a service
                // provider that has not been paid for
                view.setBackgroundColor(context.getResources().getColor(R.color
                        .seller_pending_background_color));
                // Set the summary text that informs the user has not paid
                // and what s/he bought
                summaryTextView.setText(context.getResources().getString(R.string.seller_bought) +
                        " " + unit + " " + context.getResources().getString(R.string
                        .seller_yet_to_pay));
            }
        }
        // Set the name in the name TextView
        nameTextView.setText(name);
    }
}