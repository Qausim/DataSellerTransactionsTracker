package com.example.android.datasellertransactionstracker;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.datasellertransactionstracker.data.TransactionContract.*;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Member variable for the Adapter
    private TransactionCursorAdapter mAdapter;
    // Member variable for the floatingActionButton
    private FloatingActionButton floatingActionButton;
    // Member variable of the database helper class

    // Cursor loader id for transactions
    private final int TRANSACTIONS_LOADER_ID = 15;

    // A list view object
    private ListView transactionListView;
    // A LinearLayout object
    LinearLayout emptyListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind the UI components
        floatingActionButton = findViewById(R.id.fab_add_transaction);
        transactionListView = findViewById(R.id.transaction_list_view);
        emptyListLayout = findViewById(R.id.empty_list_layout);

        // Set the empty view on the listview
        transactionListView.setEmptyView(emptyListLayout);

        // Instantiate th cursor adapter
        mAdapter = new TransactionCursorAdapter(this, null);

        // Set on item click listener on listView
        transactionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create item Uri from item id
                Uri itemUri = ContentUris.withAppendedId(TransactionEntry.CONTENT_URI, id);
                // Declare an intent for navigating to the DetailsActivity
                // Set the item uri as data on the intent
                Intent detailsIntent = new Intent(MainActivity.this,
                        DetailsActivity.class);
                // Set the item uri as data on the intent
                detailsIntent.setData(itemUri);
                // Start DetailsActivity
                startActivity(detailsIntent);
            }
        });
        // Set the cursor adapter on listView
        transactionListView.setAdapter(mAdapter);

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
        // Initialize CursorLoader
        getSupportLoaderManager().initLoader(TRANSACTIONS_LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //case R.id.action_search_by_name:
                //return true;
            //case R.id.action_search_by_number:
                //return true;
            case R.id.action_delete_entries:
                // If user opts to delete all entries
                // Build an alert dialog to ascertain this option
                AlertDialog.Builder alertBuilder;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    alertBuilder = new AlertDialog.Builder(this, android.R.style
                            .Theme_DeviceDefault_Light_Dialog);
                } else {
                    alertBuilder = new AlertDialog.Builder(this);
                }
                alertBuilder
                        // Set the alert title and message using string resources
                        .setTitle(R.string.delete_entries_title)
                        .setMessage(R.string.delete_entries)
                        // Set the posititve and negative buttons and its implementations
                        .setPositiveButton(android.R.string.yes, new DialogInterface
                                .OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Then proceed with deletion
                                int rowsDeleted = getContentResolver().delete(TransactionEntry
                                        .CONTENT_URI, // The addressed Uri
                                        null, // Select all
                                        null);
                                // Inform the user of the number of rows deleted
                                Toast.makeText(getApplicationContext(), rowsDeleted + " " +
                                        R.string.entries_deleted_toast_message, Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }).setNegativeButton(android.R.string.no, new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // If the deletion is canceled then do nothing and return
                        return;
                    }
                }).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Check the value of id if it is same as TRANSACTIONS_LOADER_ID
        switch (id) {
            case TRANSACTIONS_LOADER_ID:
                // Set the columns to return
                String[] projection = {
                        TransactionEntry._ID,
                        TransactionEntry.NAME,
                        TransactionEntry.UNIT,
                        TransactionEntry.PAYMENT_STATE,
                        TransactionEntry.TITLE
                };
                //String selection = TransactionEntry.TITLE + " = ?";
                //String[] selectionArgs = {String.valueOf(TransactionEntry.CUSTOMER)};
                // Sort by their id in descending order
                String sortBy = TransactionEntry._ID + " DESC";

                // Return a CursorLoader object
                return new CursorLoader(getApplicationContext(),
                        TransactionEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        sortBy);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap adapter on the cursor returned from loading
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // When loader is reset, set adapter on a null data
        mAdapter.swapCursor(null);
    }
}
