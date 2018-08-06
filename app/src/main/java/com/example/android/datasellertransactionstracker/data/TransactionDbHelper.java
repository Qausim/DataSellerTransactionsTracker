/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.datasellertransactionstracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.datasellertransactionstracker.data.TransactionContract.TransactionEntry;

/**
 * Created by Qausim on 8/3/2018.
 */

/**
 * Database helper class for the transactions database
 */
public class TransactionDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "data_business.db";
    private static final int DATABASE_VERSION = 2;

    public TransactionDbHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // String contains the SQL statement to create a new table transaction
        String SQL_CREATE_TRANSACTIONS_TABLE =
                "CREATE TABLE " + TransactionEntry.TABLE_NAME + "(" +

                        TransactionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "       +

                        TransactionEntry.NAME + " TEXT NOT NULL, "                          +

                        TransactionEntry.TITLE + " INTEGER NOT NULL, "                      +

                        TransactionEntry.DATE + " TEXT NOT NULL, "                          +

                        TransactionEntry.TIME + " TEXT NOT NULL, "                          +

                        TransactionEntry.UNIT + " TEXT NOT NULL, "                          +

                        TransactionEntry.COST + " INTEGER NOT NULL, "                       +

                        TransactionEntry.PHONE + " TEXT NOT NULL, "                      +

                        TransactionEntry.PAYMENT_STATE + " INTEGER NOT NULL, "              +

                        TransactionEntry.DESCRIPTION + " TEXT NOT NULL"                     +

                        ");";
        // Execute the statement.
        db.execSQL(SQL_CREATE_TRANSACTIONS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // String contains the SQL statement to delete the transactions table
        String SQL_DELETE_TRANSACTIONS_TABLE =
                "DROP TABLE IF EXIST " + TransactionEntry.TABLE_NAME + ";";
        // Execute the statement
        db.execSQL(SQL_DELETE_TRANSACTIONS_TABLE);

        // Create the table again.
        onCreate(db);
    }
}
