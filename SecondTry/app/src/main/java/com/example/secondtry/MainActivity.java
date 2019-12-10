package com.example.secondtry;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static DatabaseHelper reminderDb;//static database that can be edited through other activities
    private NotificationManagerCompat notificationManager;//for sending notifications

   private static final String NAME = "reminder_name";//for putting data in Intents
    private static final String MESSAGE = "reminder_message";

    ListView reminderListView;//list that is seen on app
     ArrayList<Reminder> mainList = new ArrayList<Reminder>();//list of reminders
     ArrayList<String> StringList = new ArrayList<String>();//list of names of reminders AND dates


    ArrayAdapter<String> adapter;//for listView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reminderDb = new DatabaseHelper(this);//create db instance

        notificationManager = NotificationManagerCompat.from(this);

        reminderListView = findViewById(R.id.reminderList);//ListView stuff
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, StringList);
        reminderListView.setAdapter(adapter);

        reminderListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {//when an item is long-clicked on the list view
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {//on item long click; create remove reminder dialog
                Toast.makeText(MainActivity.this, "" + mainList.get(position).getName() + ", " + mainList.get(position).getMessage(), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setCancelable(true);
                builder.setTitle("Delete Reminder?");
                builder.setMessage("Are you sure you want to delete this reminder?");

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setPositiveButton("Delete Reminder", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ID = String.valueOf(mainList.get(position).getId());
                        Integer deletedRows = reminderDb.deleteData(ID);
                        if(deletedRows > 0)
                            Toast.makeText(MainActivity.this, "" + "Reminder Deleted", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MainActivity.this, "" + "Deletion failed", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }
                });
                builder.show();
                return false;
            }


        });


        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//when an item is clicked in our listView
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editReminder = new Intent(MainActivity.this, EditReminderActivity.class);

                String currName = mainList.get(position).getName();
                String currMessage = mainList.get(position).getMessage();
                String currDate = mainList.get(position).getDate();
                String currTime = mainList.get(position).getTime();
                int currID = mainList.get(position).getId();

                editReminder.putExtra(NAME, currName);//putting data with Intent
                editReminder.putExtra(MESSAGE, currMessage);
                editReminder.putExtra("id", currID);

                String dateArr[] = currDate.split("/");
                String timeArr[] = currTime.split(":");

                int yearInt = Integer.parseInt(dateArr[2]);
                int monthInt = Integer.parseInt(dateArr[0]);
                int dayInt = Integer.parseInt(dateArr[1]);

                int hourInt = Integer.parseInt(timeArr[0]);
                int minInt = Integer.parseInt(timeArr[1]);

                editReminder.putExtra("dateYear", yearInt);
                editReminder.putExtra("dateMonth", monthInt);
                editReminder.putExtra("dateDay", dayInt);
                editReminder.putExtra("dateHour", hourInt);
                editReminder.putExtra("dateMin", minInt);

                Toast.makeText(MainActivity.this, "Year = " + yearInt + "\n" + "Month = " + monthInt +"\n" + "Day = " + dayInt + "\n"
                + "Hour = " + hourInt + "\n" + "Minute = " + minInt + "\n" + "ID = " + mainList.get(position).getId(), Toast.LENGTH_LONG).show();//ID = POSITION + 1
                startActivity(editReminder);//start to activity to edit reminder
            }
        });

        updateColor();//update color from settings
        showAllData();//show data from db in listview
    }

    public void showAllData(){
        Cursor res = reminderDb.getAllData();
        if(res.getCount() == 0) {//if no data available
            Toast.makeText(MainActivity.this, "No Data to Show", Toast.LENGTH_LONG).show();
            return;
        }
        while(res.moveToNext()){//put reminders from db to arraylist to show in listview
            Reminder newReminder = new Reminder();
            newReminder.setId(Integer.parseInt(res.getString(0)));
            newReminder.setName(res.getString(1));
            newReminder.setMessage(res.getString(2));
            newReminder.setDate(res.getString(3));
            newReminder.setTime(res.getString(4));
            mainList.add(newReminder);
        }
        for(Reminder r : mainList){
            StringList.add(r.toString());
        }
    }

    public void updateColor(){
        int newColor = getIntent().getIntExtra("backgroundColor", 0);
        View currentLayout = findViewById(R.id.main_layout);
        currentLayout.setBackgroundColor(newColor);
    }

    public void goToAddReminder(View view){
        //create an Intent to start the newReminder Activity
        Intent reminderIntent = new Intent(this, NewReminderActivity.class);
        startActivity(reminderIntent);
    }

    public void goToSettings(View view){
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }


    public void sendOnChannel1(View v){
        Notification notification = new NotificationCompat.Builder(this, ReminderApp.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_clock)
                .setContentTitle(mainList.get(0).getName())
                .setContentText(mainList.get(0).getMessage())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
    }

    public void sendOnChannel2(View v){
        Notification notification = new NotificationCompat.Builder(this, ReminderApp.CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_clock)
                .setContentTitle(mainList.get(1).getName())
                .setContentText(mainList.get(1).getMessage())
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
        notificationManager.notify(2, notification);

    }
}
