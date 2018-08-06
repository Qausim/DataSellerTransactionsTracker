package com.example.android.datasellertransactionstracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.datasellertransactionstracker.data.TransactionContract.*;
import com.example.android.datasellertransactionstracker.data.TransactionDbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTransactionActivity extends AppCompatActivity {
    // Declaring the various UI components
    private EditText nameEditText, phoneEditText, unitEditText, costEditText, descriptionEditText;
    private Spinner mPaymentStateSpinner, mTitleSpinner;
    // Array adapters for the payment state spinner and title spinner each
    private ArrayAdapter titleSpinnerAdapter, paymentStateSpinnerAdapter;
    // Integer variable stores the state of the title spinner selected.
    private int mTitle;
    // Integer variable stores the state of the payment state spinner selected.
    private int mPaymentState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        // Bind the UI components
        nameEditText = findViewById(R.id.ed_name);
        phoneEditText = findViewById(R.id.ed_phone);
        unitEditText = findViewById(R.id.ed_unit);
        costEditText = findViewById(R.id.ed_cost);
        descriptionEditText = findViewById(R.id.ed_description);
        mPaymentStateSpinner = findViewById(R.id.payment_state_spinner);
        mTitleSpinner = findViewById(R.id.title_spinner);

        // Initialize the array adapters
         titleSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.title_options_array, android.R.layout.simple_dropdown_item_1line);

         paymentStateSpinnerAdapter = ArrayAdapter.createFromResource(this,
                 R.array.payment_state_options_array, android.R.layout.simple_dropdown_item_1line);

         // Set dropdown view resource on the array adapters
        titleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        paymentStateSpinnerAdapter.setDropDownViewResource(android.R.layout
                .simple_dropdown_item_1line);

        // set array adapters on the title spinner
        mTitleSpinner.setAdapter(titleSpinnerAdapter);
        // Set item selection listener on the title spinner
        mTitleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get selection
                String selection = (String) parent.getItemAtPosition(position);
                // Check the state of the selection
                // If customer
                if (selection == getString(R.string.customer)) {
                    // Set mTitle to 1
                    mTitle = TransactionEntry.CUSTOMER;
                } else if (selection == getString(R.string.service_provider)) {
                    // If service provider
                    // Set mTitle to 2
                    mTitle = TransactionEntry.SERVICE_PROVIDER;
                } else {// Else set mTitle to 0
                    mTitle = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mTitle = 0;
            }
        });
        // Set adapter on the payment state spinner
        mPaymentStateSpinner.setAdapter(paymentStateSpinnerAdapter);
        // Set item selected listener
        mPaymentStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get selection
                String selection = (String) parent.getItemAtPosition(position);
                // If selection == paid
                if (selection == getString(R.string.paid)) {
                    // Set mPaymentState to 3
                    mPaymentState = TransactionEntry.PAID;
                } else if (selection == getString(R.string.pending)) {// If payment is pending
                    mPaymentState = TransactionEntry.PENDING;
                } else {// Else
                    mPaymentState = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mPaymentState = 0;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_transaction_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Check selected item
        switch (item.getItemId()) {
            // If save
            case R.id.action_save:
                // If name was inputed
                boolean nameEntered = !TextUtils.isEmpty(nameEditText.getText()) && nameEditText
                        .getText().toString().trim() != "";
                // If phone number was entered
                boolean phoneEntered = !TextUtils.isEmpty(phoneEditText.getText()) && phoneEditText
                        .getText().toString().trim() != "";
                // If unit was inputed
                boolean unitEntered = !TextUtils.isEmpty(unitEditText.getText()) && unitEditText
                        .getText().toString().trim() != "";
                // If cost was entered
                boolean costEntered = !TextUtils.isEmpty(costEditText.getText()) && costEditText
                        .getText().toString().trim() != "";
                // If description was entered
                boolean descriptionEntered = !TextUtils.isEmpty(descriptionEditText.getText()) &&
                        descriptionEditText.getText().toString().trim() != "";
                // If payment state selected
                boolean paymentStateSelected = mPaymentState == TransactionEntry.PENDING ||
                        mPaymentState == TransactionEntry.PAID;
                // If title selected
                boolean titleSelected = mTitle == TransactionEntry.CUSTOMER || mTitle ==
                        TransactionEntry.SERVICE_PROVIDER;
                // If all above is true
                if (nameEntered && phoneEntered && unitEntered && costEntered && descriptionEntered &&
                        paymentStateSelected && titleSelected) {
                    try {
                        // Get the various components
                        String name = nameEditText.getText().toString().trim();
                        String phone = phoneEditText.getText().toString().trim();
                        String unit = unitEditText.getText().toString().trim();
                        int cost = Integer.parseInt(costEditText.getText().toString().trim());
                        String description = descriptionEditText.getText().toString();

                        // Insert new entry into database
                        insertIntoDatabase(name, phone, unit, cost, description, mTitle, mPaymentState);

                    } catch (Exception e){
                        Toast.makeText(getApplicationContext(), getString(R.string
                                .check_type_error_message), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string
                            .populate_fields_toast_message), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.action_cancel:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertIntoDatabase(String name, String phone, String unit, int cost, String description,
                                   int title, int paymentState) {
        // Get current date
        Date date = Calendar.getInstance().getTime();
        // Set simple date format for both time and date
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:MM a");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");

        // Format the time and date
        String timeString = timeFormat.format(date);
        String dateString = dateFormat.format(date);
        // Create an instance of the database helper class
        TransactionDbHelper mDbHelper = new TransactionDbHelper(getApplicationContext());
        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Instantiate a ContentValue object
        ContentValues values = new ContentValues();

        // Put values into it
        values.put(TransactionEntry.NAME, name);
        values.put(TransactionEntry.PHONE, phone);
        values.put(TransactionEntry.UNIT, unit);
        values.put(TransactionEntry.COST, cost);
        values.put(TransactionEntry.DESCRIPTION, description);
        values.put(TransactionEntry.TITLE, title);
        values.put(TransactionEntry.PAYMENT_STATE, paymentState);
        values.put(TransactionEntry.DATE, dateString);
        values.put(TransactionEntry.TIME, timeString);

        // Insert into database
        long newRowId = database.insert(TransactionEntry.TABLE_NAME, null, values);

        // If insert was not successfully
        if (newRowId < 0) {
            // Make a toast that insertion was not successful
            Toast.makeText(getApplicationContext(), getString(R.string
                            .database_insert_error_message), Toast.LENGTH_LONG).show();
        } else {// Else if successful
            /*Toast.makeText(getApplicationContext(), getString(R.string
                    .database_insert_success_message), Toast.LENGTH_LONG).show(); */
            // Go back home
            finish();

        }
    }
}
