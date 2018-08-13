package com.example.android.datasellertransactionstracker.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.android.datasellertransactionstracker.R;
import com.example.android.datasellertransactionstracker.data.TransactionContract.*;


/**
 * Created by HP on 8/10/2018.
 * The content provider for the database operations
 */

public class TransactionProvider extends ContentProvider {
    // Declare a global database helper object
    TransactionDbHelper mDbHelper;

    // A Toast object
    Toast toast;

    // Create unique ids for matching Uris
    public static final int TRANSACTIONS = 15;
    public static final int TRANSACTIONS_ID = 16;

    // Initialize a uri matcher object
    public static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer, gets called first anytime something is called from this class
    static {
        mUriMatcher.addURI(TransactionContract.CONTENT_AUTHORITY,
                TransactionContract.PATH_TRANSACTIONS, TRANSACTIONS);
        mUriMatcher.addURI(TransactionContract.CONTENT_AUTHORITY,
                TransactionContract.PATH_TRANSACTIONS + "/#", TRANSACTIONS_ID);
    }
    @Override
    public boolean onCreate() {
        // Initialize a database helper object here
        mDbHelper = new TransactionDbHelper(getContext());
        // Return true
        return true;
    }

    /**
     * Handles query operations.
     * @param uri is the uri to be queried
     * @param projection is an array containing columns we want in our result
     * @param selection if we desire some specific rows
     * @param selectionArgs the values we will be checking the rows we picking out against
     * @param sortOrder how we want results ordered
     * @return a cursor.
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get a readable version of the database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // Match the Uri
        int match = mUriMatcher.match(uri);

        // Cursor object to hold values required
        Cursor cursor;

        // Make decisions on how access the database using the match
        switch (match) {
            // If the whole table is to queried
            case TRANSACTIONS:
                cursor = database.query(TransactionEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            // Else if the a specific row is queried
            case TRANSACTIONS_ID:
                // Its selection is then the id column
                selection = TransactionEntry._ID + " = ?";
                // And its id is the value we seek
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                // We query thus
                cursor = database.query(TransactionEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, // Don't group
                        null,
                        null /* Dont order */);
                break;
            default:
                throw new IllegalArgumentException("No query access for the Uri " + uri + " with match" +
                        match);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

        /**
         * @return the MIME type of the a Uri. */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // Match the Uri
        final int match = mUriMatcher.match(uri);
        // Make decisions based on the value of match
        switch (match) {
            // if the whole table
            case TRANSACTIONS:
                return TransactionEntry.TRANSACTIONS_LIST_TYPE;
            // If a specific row
            case TRANSACTIONS_ID:
                return TransactionEntry.TRANSACTIONS_ITEM_TYPE;
            // If the Uri is a strange one
            default:
                throw new IllegalArgumentException("No access allowed for Uri" + uri);
        }
    }

    /**
     * Handles insertion operations on the database
     * @param uri is the Uri address
     * @param values the values to be inserted into the columns of the database for the new row
     * @return the Uri address of the new row
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        // Sanity check components of values
        String name = values.getAsString(TransactionEntry.NAME);
        String unit = values.getAsString(TransactionEntry.UNIT);
        String comments = values.getAsString(TransactionEntry.DESCRIPTION);
        String phone = values.getAsString(TransactionEntry.PHONE);

        int cost;

        try {
            cost = values.getAsInteger(TransactionEntry.COST);
        } catch (Exception e) {
            toast = Toast.makeText(getContext(), Resources.getSystem().getString(R.string
                    .wrong_input_type_error_message), Toast.LENGTH_LONG);
            toast.show();
            return null;
        }


        if (TextUtils.isEmpty(name)) {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(getContext(), Resources.getSystem().getString(R.string
                    .empty_name_error_message), Toast.LENGTH_LONG);
            toast.show();
            return null;
        }

        if (TextUtils.isEmpty(phone)) {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(getContext(), Resources.getSystem().getString(R.string
                    .empty_phone_error_message), Toast.LENGTH_LONG);
            toast.show();
            return null;
        }
        if (TextUtils.isEmpty(comments)) {
            comments = getContext().getString(R.string.transaction_successful_comment);
            values.remove(TransactionEntry.DESCRIPTION);
            values.put(TransactionEntry.DESCRIPTION, comments);
        }

        // Writable database instance
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // Variable for the new inserted row id
        Long newRowId;

        // Match the Uri
        final int match = mUriMatcher.match(uri);
        // Make decisions based on the value of match
        switch (match) {
            // Only the whole table can support insertions
            case TRANSACTIONS:
                newRowId = database.insert(TransactionEntry.TABLE_NAME,
                        null,
                        values);
                // If insertion is successful
                if (!(newRowId < 0)){
                    // Notify change
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                Uri newRowUri = Uri.withAppendedPath(uri, String.valueOf(newRowId));
                return newRowUri;
            default:
                throw new IllegalArgumentException("Insertion not allowed for Uri " + uri);
        }
    }

    /**
     * Handles deletion operation on the database.
     * */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get a writable instance of the database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Match the Uri
        int match = mUriMatcher.match(uri);

        // Rows deleted
        int rowsDeleted;
        // Make decisions based on the value of match
        switch (match) {
            // If the whole table
            case TRANSACTIONS:
                // Delete all the entries specified
                rowsDeleted = database.delete(TransactionEntry.TABLE_NAME, selection,
                        selectionArgs);
                // Notify change
                getContext().getContentResolver().notifyChange(uri, null);
                // Return the number of rows deleted
                return rowsDeleted;
            // If a specific row is to be deleted
            case TRANSACTIONS_ID:
                // Get the of the row
                long rowId = ContentUris.parseId(uri);
                selection = TransactionEntry._ID + " = ?";
                selectionArgs = new String[] {String.valueOf(rowId)};
                rowsDeleted = database.delete(TransactionEntry.TABLE_NAME, selection,
                        selectionArgs);
                // Notify change
                getContext().getContentResolver().notifyChange(uri, null);
                // Return the number of rows deleted
                return rowsDeleted;
            // For strange uris
            default:
                throw new IllegalArgumentException("No access for Uri " + uri);
        }
    }

    /**
     * Handles item updates. */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        // A writable database object
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Sanity check components of values
        String name = values.getAsString(TransactionEntry.NAME);
        String unit = values.getAsString(TransactionEntry.UNIT);
        String comments = values.getAsString(TransactionEntry.DESCRIPTION);
        String phone = values.getAsString(TransactionEntry.PHONE);

        int cost;

        try {
            cost = values.getAsInteger(TransactionEntry.COST);
        } catch (Exception e) {
            toast = Toast.makeText(getContext(), Resources.getSystem().getString(R.string
                    .wrong_input_type_error_message), Toast.LENGTH_LONG);
            toast.show();
            return 0;
        }


        if (TextUtils.isEmpty(name)) {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(getContext(), Resources.getSystem().getString(R.string
                    .empty_name_error_message), Toast.LENGTH_LONG);
            toast.show();
            return 0;
        }

        if (TextUtils.isEmpty(phone)) {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(getContext(), Resources.getSystem().getString(R.string
                    .empty_phone_error_message), Toast.LENGTH_LONG);
            toast.show();
            return 0;
        }
        if (TextUtils.isEmpty(comments)) {
            comments = Resources.getSystem().getString(R.string.transaction_successful_comment);
            values.remove(TransactionEntry.DESCRIPTION);
            values.put(TransactionEntry.DESCRIPTION, comments);
        }

        // Match the Uri
        int match = mUriMatcher.match(uri);

        // Updates is only allowed for a row at time
        switch (match) {
            case TRANSACTIONS_ID:
                // Get the id of the row
                long rowId = ContentUris.parseId(uri);
                selection = TransactionEntry._ID + " = ?";
                selectionArgs = new String[] {String.valueOf(rowId)};
                int rowsUpdated = database.update(TransactionEntry.TABLE_NAME, values,
                        selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return rowsUpdated;
            default:
                throw new IllegalArgumentException("Illegal Uri" + uri);
        }
    }
}
