package com.example.android.datasellertransactionstracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.datasellertransactionstracker.data.TransactionContract.*;
import com.example.android.datasellertransactionstracker.data.TransactionDbHelper;

public class DetailsActivity extends AppCompatActivity {

    private TextView nameTextView, phoneTextView, titleTextView, unitTextView, costTextView,
    paymentStateTextView, descriptionTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        nameTextView = findViewById(R.id.tv_name2);
        phoneTextView = findViewById(R.id.tv_phone);
        titleTextView = findViewById(R.id.tv_title);
        unitTextView = findViewById(R.id.tv_unit);
        costTextView = findViewById(R.id.tv_cost2);
        paymentStateTextView = findViewById(R.id.tv_payment_state2);
        descriptionTextView = findViewById(R.id.tv_description);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        int transactionId = -1;
        // Get start intent
        Intent starterIntent = getIntent();
        // If starter intent has key id
        if (starterIntent.hasExtra(getString(R.string.id))) {
            transactionId = starterIntent.getIntExtra(getString(R.string.id), -1);
        }

        // If intent has no id
        if (transactionId == -1) {
            // Do nothing
            return;
        } else {
            // Else set up UI
            setUpUI(transactionId);
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

    private void setUpUI(int id) {
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
                String currentUnit = cursor.getString(unitColIndex);
                int currentCost = cursor.getInt(costColIndex);
                int currentTitle = cursor.getInt(titleColIndex);
                int currentPaymentState = cursor.getInt(paymentStateColIndex);
                String currentDescription = cursor.getString(descriptionColIndex);
                String currentDate = cursor.getString(dateColIndex);
                String currentTime = cursor.getString(timeColIndex);

                // Set the content of each text
                nameTextView.setText(currentName);
                phoneTextView.setText(currentPhone);
                unitTextView.setText(currentUnit);
                costTextView.setText(String.valueOf(currentCost));

                // Check the integer value of currentTitle to set the text accordingly
                if (currentTitle == TransactionEntry.CUSTOMER) {
                    titleTextView.setText(getString(R.string.customer));
                } else {
                    titleTextView.setText(getString(R.string.service_provider));
                }

                // Check the integer value of currentPaymentState to set the text accordingly
                if (currentPaymentState == TransactionEntry.PAID) {
                    paymentStateTextView.setText(getString(R.string.paid));
                } else {
                    paymentStateTextView.setText(getString(R.string.pending));
                }

                String descString = getString(R.string.created) + " " + currentTime + " " +
                        currentDate + "\n" + currentDescription;
                descriptionTextView.setText(descString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
    }
}
