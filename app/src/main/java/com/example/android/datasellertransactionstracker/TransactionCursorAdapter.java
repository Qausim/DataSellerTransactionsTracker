package com.example.android.datasellertransactionstracker;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.R;
import com.example.android.datasellertransactionstracker.data.TransactionContract.*;

/**
 * Created by HP on 8/11/2018.
 */

public class TransactionCursorAdapter extends CursorAdapter {
    TextView nameTextView, summaryTextView;
    public TransactionCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int itemLayoutId = R.layout.
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

        String paymentStateString;
        switch (paymentState) {
            case TransactionEntry.PAID:
                paymentStateString = Resources.getSystem().getString(R.string.paid);
                view.setBackgroundColor(Resources.getSystem().getColor(R.color
                        .paid_background_color));
                summaryTextView.setText(Resources.getSystem().getString(android.R.string.bought
            ) + unit + Resources.getSystem().getString(R.string.has_paid));
                break;
            case TransactionEntry.PENDING:
                paymentStateString = Resources.getSystem().getString(R.string.pending);
                view.setBackgroundColor(Resources.getSystem().getColor(R.color
                        .pending_background_color));
                summaryTextView.setText(Resources.getSystem().getString(android.R.string.bought
                ) + " " + unit + " " + Resources.getSystem().getString(R.string.yet_to_pay));
                break;
        }

        nameTextView.setText(name);
    }
}
