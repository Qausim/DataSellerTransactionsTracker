package com.example.android.datasellertransactionstracker;

import android.content.Context;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.datasellertransactionstracker.data.TransactionDbHelper;
import com.example.android.datasellertransactionstracker.data.TransactionContract.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TransactionAdapter.TransactionItemClickListener{

    // Member variable for the RecyclerView;
    private RecyclerView mRecylerView;
    // Member variable for the Adapter
    private RecyclerView.Adapter mAdapter;
    // Member variable for LayoutManager
    RecyclerView.LayoutManager mLayoutManager;
    // Member variable for the floatingActionButton
    private FloatingActionButton floatingActionButton;
    // Member variable of the database helper class
    private TransactionDbHelper mDbHelper;
    // Container for Transaction objects;
    ArrayList<Transaction> transactions;
    public static String paid, pending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecylerView = findViewById(R.id.rv_transactions);
        // The recycler view size may change
        mRecylerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        mRecylerView.setLayoutManager(mLayoutManager);
        floatingActionButton = findViewById(R.id.fab_add_transaction);
        transactions = new ArrayList<>();
        mDbHelper = new TransactionDbHelper(this);
        paid = getString(R.string.paid);
        pending = getString(R.string.pending);

        // Set click listener on the floatingActionButton
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = MainActivity.this;
                Class destination = AddTransactionActivity.class;

                Intent intent = new Intent(context, destination);
                startActivity(intent);
            }
        });
    }

    /* Override onStart to and query the database from there handle changes to the database when we
     return from another activity.
      */
    @Override
    protected void onStart() {
        super.onStart();
        if (!transactions.isEmpty()) {
            transactions.clear();
        }
        populateRecyclerViewWithData();
    }

    /**
     * Queries the database and  populates the recycler view
     */
    private void populateRecyclerViewWithData() {
        // Get the readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // Create a string array for projections
        String[] projections = {TransactionEntry._ID,
                TransactionEntry.NAME,
                TransactionEntry.PHONE,
                TransactionEntry.UNIT,
                TransactionEntry.COST,
                TransactionEntry.TITLE,
                TransactionEntry.PAYMENT_STATE,
                TransactionEntry.DESCRIPTION,
                TransactionEntry.DATE,
                TransactionEntry.TIME};

        // String variable for the selection
        String selection = TransactionEntry.TITLE + " = ?";
        // String array for selectionArgs
        String[] selectionArgs = {String.valueOf(TransactionEntry.CUSTOMER)};
        // String variable for the sort order.
        // Query database and store the cursor in a cursor object;
        Cursor cursor = database.query(TransactionEntry.TABLE_NAME,
                projections,
                selection,
                selectionArgs ,
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
            // If cursor is not null
            if (cursor != null) {
                // If there is a first line
                // Move cursor to that line
                if(cursor.moveToFirst()) {
                    do {
                        // Iterate throught the cursor
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

                        // Add a new transaction object to transactions
                        transactions.add(new Transaction(currentName, currentId, currentTitle,
                                currentPhone, currentUnit, currentCost, currentPaymentState,
                                currentDescription, currentDate, currentTime));
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close cursor
            cursor.close();
        }

        if (!transactions.isEmpty()) {
            mAdapter = new TransactionAdapter(transactions, this);
            mRecylerView.setAdapter(mAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search_by_name:
                return true;
            case R.id.action_search_by_number:
                return true;
            case R.id.action_delete_entries:
                return true;
            case R.id.action_view_purchase_history:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTransactionItemClicked(int itemIndex) {
        // Get the clicked transaction
        Transaction currentTransaction = transactions.get(itemIndex);
        // Get the id of the clicked transaction
        int currentTransactionId = currentTransaction.getId();

        // MainActivity is the parent of the new activity to be started
        Context parent = MainActivity.this;
        // The destination activity is DetailsActivity
        Class destination = DetailsActivity.class;

        // Create starter intent
        Intent starterIntent = new Intent(parent, destination);
        // Put the transaction id in the intent
        starterIntent.putExtra(getString(R.string.id), currentTransactionId);
        // Start the activity
        startActivity(starterIntent);
    }
}
