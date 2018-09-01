package com.example.android.datasellertransactionstracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.datasellertransactionstracker.data.TransactionContract.*;

public class DetailsActivity extends AppCompatActivity {

    // Log tag for the activity
    private static final String TAG = DetailsActivity.class.getSimpleName();
    // UI component objects
    private TextView nameTextView, phoneTextView, titleTextView, unitTextView, costTextView,
    paymentStateTextView, descriptionTextView;

    // The Uri of this particular entry will be contained in
    Uri itemUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Bind UI components to their respective objects
        nameTextView = findViewById(R.id.tv_name2);
        phoneTextView = findViewById(R.id.tv_phone);
        titleTextView = findViewById(R.id.tv_title);
        unitTextView = findViewById(R.id.tv_unit);
        costTextView = findViewById(R.id.tv_cost2);
        paymentStateTextView = findViewById(R.id.tv_payment_state2);
        descriptionTextView = findViewById(R.id.tv_description);

        // Set navigate home action bar arrow
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Get the intent that started the activity
        Intent starterIntent = getIntent();
        // Get the item uri from the intent
        itemUri = starterIntent.getData();
    }

    @Override
    protected void onStart() {
        // In onStart setup the UI with data for each components
        // So when user navigates away and back changes to the data would be effected in the UI
        super.onStart();
        setUpUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get the id of the option clicked
        switch (item.getItemId()) {

            case R.id.action_delete:
                // If the user chose to delete
                // Create an alert to ascertain his/her choice
                AlertDialog.Builder alertDialogBuider;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    alertDialogBuider = new AlertDialog.Builder(this,
                            android.R.style.Theme_DeviceDefault_Light_Dialog);
                } else {
                    alertDialogBuider = new AlertDialog.Builder(this);
                }
                alertDialogBuider
                        // Set the title of the dialog
                        .setTitle(R.string.delete_entry_title)
                        // Set the message of the dialog
                        .setMessage(R.string.delete_entry)
                        // Set the negative option of the dialog and its implementation
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                                return;
                            }
                        })
                        // Set the positive option and proceed with deletion
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int rowsDeleted = getContentResolver().delete(itemUri,
                                        null,
                                        null);
                                // Go back
                                finish();
                            }
                        }).show();

                break;
            case R.id.action_edit:
                // If the user chose to edit
                // Then start the AddTransactionActivity as an editor activity
                Intent editorStarterIntent = new Intent(DetailsActivity.this,
                        AddTransactionActivity.class);
                // Add the item's Uri to the intent
                editorStarterIntent.setData(itemUri);
                // Start AddTransactionActivity
                startActivity(editorStarterIntent);
                break;
            case android.R.id.home:
                // If option chosen is to go home
                // Then navigate up the stack of activities
                NavUtils.navigateUpFromSameTask(this);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Handles UI updates of UI components with existing data in the database
     * */
    private void setUpUI() {
        Log.d(TAG, "Row Uri" + itemUri);
        // String array for the projections
        String[] projections = {
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
        // Query the db for data
        Cursor cursor = getContentResolver().query(itemUri,
                projections,
                null,
                null,
                null);

        // Get the index of each column
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
                String currentName = cursor.getString(nameColIndex);
                String currentPhone = cursor.getString(phoneColIndex);
                String currentUnit = cursor.getString(unitColIndex);
                int currentCost = cursor.getInt(costColIndex);
                int currentTitle = cursor.getInt(titleColIndex);
                int currentPaymentState = cursor.getInt(paymentStateColIndex);
                String currentDescription = cursor.getString(descriptionColIndex);
                String currentDate = cursor.getString(dateColIndex);
                String currentTime = cursor.getString(timeColIndex);

                // Set the content of each TextView
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

                // Set up the string for describing comments on the transaction
                String descString = getString(R.string.created_updated) + " " + currentTime + ", " +
                        currentDate + "\n\n" + currentDescription;
                descriptionTextView.setText(descString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the cursor
            cursor.close();
        }
    }
}
