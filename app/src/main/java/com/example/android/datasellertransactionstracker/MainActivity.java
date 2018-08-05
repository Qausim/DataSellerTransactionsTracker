package com.example.android.datasellertransactionstracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
