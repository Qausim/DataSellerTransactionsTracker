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
 */

public class TransactionCursorAdapter extends CursorAdapter {
    private TextView nameTextView, summaryTextView;
    public TransactionCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int itemLayoutId = R.layout.transaction_item_layout;
        return LayoutInflater.from(context)
                .inflate(itemLayoutId, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        nameTextView = view.findViewById(R.id.tv_name);
        summaryTextView = view.findViewById(R.id.tv_summary);

        String name = cursor.getString(cursor.getColumnIndex(TransactionEntry.NAME));
        String unit = cursor.getString(cursor.getColumnIndex(TransactionEntry.UNIT));
        int paymentState = cursor.getInt(cursor.getColumnIndex(TransactionEntry.PAYMENT_STATE));
        int title = cursor.getInt(cursor.getColumnIndex(TransactionEntry.TITLE));

        if (title == TransactionEntry.CUSTOMER) {
            if (paymentState == TransactionEntry.PAID) {
                view.setBackgroundColor(context.getResources().getColor(R.color
                        .customer_paid_background_color));
                summaryTextView.setText(context.getResources().getString(R.string.bought) + " " +
                        unit + " " + context.getResources().getString(R.string.has_paid));
            } else if (paymentState == TransactionEntry.PENDING) {
                view.setBackgroundColor(context.getResources().getColor(R.color
                        .customer_pending_background_color));
                summaryTextView.setText(context.getResources().getString(R.string.bought) + " " +
                        unit + " " + context.getResources().getString(R.string.yet_to_pay));
            }
        }
        if (title == TransactionEntry.SERVICE_PROVIDER) {
            if (paymentState == TransactionEntry.PAID) {
                view.setBackgroundColor(context.getResources().getColor(R.color
                        .seller_paid_background_color));
                summaryTextView.setText(context.getResources().getString(R.string.seller_bought) +
                        " " + unit + " " + context.getResources().getString(R.string.seller_has_paid));
            }
            if (paymentState == TransactionEntry.PENDING) {
                view.setBackgroundColor(context.getResources().getColor(R.color
                        .seller_pending_background_color));
                summaryTextView.setText(context.getResources().getString(R.string.seller_bought) +
                        " " + unit + " " + context.getResources().getString(R.string
                        .seller_yet_to_pay));
            }
        }

        nameTextView.setText(name);
    }
}