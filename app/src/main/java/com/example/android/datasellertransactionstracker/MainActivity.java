package com.example.android.datasellertransactionstracker;

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

        // Set click listener on the floatingActionButton
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do nothing for now
                return;
            }
        });
    }

    /* Override onStart to and query the database from there handle changes to the database when we
     return from another activity.
      */
    @Override
    protected void onStart() {
        super.onStart();
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
                TransactionEntry.DATE};

        // String variable for the selection
        String selection = TransactionEntry.TITLE + " = ?";
        // String array for selectionArgs
        String[] selectionArgs = {String.valueOf(TransactionEntry.CUSTOMER)};
        // String variable for the sort order.
        String sortOrder = TransactionEntry.DATE + " ASC";
        // Query database and store the cursor in a cursor object;
        Cursor cursor = database.query(TransactionEntry.TABLE_NAME,
                projections,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

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

        try {
            // Iterate through the cursor if query was successful
            while (cursor.moveToNext()) {
                // Get the value in the columns of each row
                int currentId = cursor.getInt(idColIndex);
                String currentName = cursor.getString(nameColIndex);
                int currentPhone = cursor.getInt(phoneColIndex);
                int currentUnit = cursor.getInt(unitColIndex);
                int currentCost = cursor.getInt(costColIndex);
                int currentTitle = cursor.getInt(titleColIndex);
                int currentPaymentState = cursor.getInt(paymentStateColIndex);
                String currentDescription = cursor.getString(descriptionColIndex);
                long currentDate = Long.parseLong(String.valueOf(cursor.getInt(dateColIndex)));

                // Add a new transaction object to transactions
                transactions.add(new Transaction(currentName, currentId, currentTitle,
                        currentPhone, currentUnit, currentCost, currentPaymentState,
                        currentDescription, currentDate));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        if (!transactions.isEmpty()) {
            mAdapter = new TransactionAdapter(transactions, this);
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
        Toast.makeText(this, "Item " + itemIndex + " clicked", Toast.LENGTH_LONG)
                .show();
    }
}
