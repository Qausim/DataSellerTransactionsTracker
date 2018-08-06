package com.example.android.datasellertransactionstracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.android.datasellertransactionstracker.data.TransactionContract.*;
import com.example.android.datasellertransactionstracker.data.TransactionDbHelper;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Get start intent
        Intent starterIntent = getIntent();
        // If starter intent has key id
        if (starterIntent.hasExtra(getString(R.string.id))) {
            int transactionId = starterIntent.getIntExtra(getString(R.string.id));

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Transaction getTransactionById(int id) {
        // Get db helper
        TransactionDbHelper dbHelper = new TransactionDbHelper(this);
        // Get readable database
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        // String array for the projections
        String[] projections = {
                TransactionEntry._ID,
                TransactionEntry.NAME,
                TransactionEntry.PHONE,
                TransactionEntry.UNIT,
                TransactionEntry.COST,
                TransactionEntry.PAYMENT_STATE,
                TransactionEntry.TIME,
                TransactionEntry.DATE,
                TransactionEntry.TITLE,
                TransactionEntry.DESCRIPTION
        };

        // Specify selection
        String selection = TransactionEntry._ID + " = ?";
        // Specify selection args
        String[] selectionArgs = {String.valueOf(id)};
        // Query database using id
        Cursor cursor = database.query(TransactionEntry.TABLE_NAME,
                projections,
                selection,
                selectionArgs,
                null,
                null,
                null);

        // Get the index of each column
        int idColIndex = cursor.getColumnIndex(TransactionEntry._ID);
        int nameColIndex = cursor.getColumnIndex(TransactionEntry.NAME);
        int phoneColIndex = cursor.getColumnIndex(TransactionEntry.PHONE);
        int unitColIndex = cursor.getColumnIndex(TransactionEntry.UNIT);
        int costColIndex = cursor.getColumnIndex(TransactionEntry.COST);
        int titleColIndex = cursor.getColumnIndex(TransactionEntry.TITLE);
        int paymentStateColIndex = cursor.getColumnIndex(TransactionEntry.PAYMENT_STATE);
        int descriptionColIndex = cursor.getColumnIndex(TransactionEntry.DESCRIPTION);
        int dateColIndex = cursor.getColumnIndex(TransactionEntry.DATE);
        int timeColIndex = cursor.getColumnIndex(TransactionEntry.TIME);

        try {
            // Iterate through the cursor
            while (cursor.moveToNext()) {
                // Get the value in the columns of each row
                int currentId = cursor.getInt(idColIndex);
                String currentName = cursor.getString(nameColIndex);
                String currentPhone = cursor.getString(phoneColIndex);
                int currentUnit = cursor.getInt(unitColIndex);
                int currentCost = cursor.getInt(costColIndex);
                int currentTitle = cursor.getInt(titleColIndex);
                int currentPaymentState = cursor.getInt(paymentStateColIndex);
                String currentDescription = cursor.getString(descriptionColIndex);
                String currentDate = cursor.getString(dateColIndex);
                String currentTime = cursor.getString(timeColIndex);

            }
        }
    }
}
