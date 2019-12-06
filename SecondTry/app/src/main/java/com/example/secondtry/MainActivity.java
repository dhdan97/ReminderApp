package com.example.secondtry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ListView reminderListView;
    String[] list = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reminderListView = findViewById(R.id.reminderList);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);

        reminderListView.setAdapter(adapter);

        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "" + list[position], Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addReminder(View view){

        //create an Intent to start the newReminder Activity
        Intent reminderIntent = new Intent(this, NewReminderActivity.class);

        startActivity(reminderIntent);
    }

    public void goToSettings(View view){

        Intent settingsIntent = new Intent(this, SettingsActivity.class);

        startActivity(settingsIntent);
    }
}
