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
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static DatabaseHelper reminderDb;
    private NotificationManagerCompat notificationManager;

   private static final String NAME = "reminder_name";//for putting data in Intents
    private static final String MESSAGE = "reminder_message";
    private static final String DATE = "reminder_date";

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


        /*for(Reminder r : mainList){//StringList will be what we see on viewList
            StringList.add(r.toString());
        }*/

//        updateColor();//update color from settings*/
//        showAllData();//show data from db in listview

        reminderListView = findViewById(R.id.reminderList);//ListView stuff
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, StringList);
        reminderListView.setAdapter(adapter);

        reminderListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {//when an item is long-clicked on the list view
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
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
                        String ID = String.valueOf(position + 1);
                        Integer deletedRows = reminderDb.deleteData(ID);
                        if(deletedRows > 0)
                            Toast.makeText(MainActivity.this, "" + "Reminder Deleted", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MainActivity.this, "" + "Deletion failed", Toast.LENGTH_SHORT).show();
                        //mainList.remove(position);
                        //StringList.remove(position);
                        finish();
                        startActivity(getIntent());
                        //Toast.makeText(MainActivity.this, "" + "Reminder Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();

                return false;
            }


        });


        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//when an item is clicked in our listView
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editReminder = new Intent(MainActivity.this, EditReminderActivity.class);//might need to make a new activity for editing reminders;DONE

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

                int yearInt = Integer.parseInt(dateArr[2]);//probably crashes app
                int monthInt = Integer.parseInt(dateArr[0]);
                int dayInt = Integer.parseInt(dateArr[1]);

                int hourInt = Integer.parseInt(timeArr[0]);
                int minInt = Integer.parseInt(timeArr[1]);

                editReminder.putExtra("dateYear", yearInt);
                editReminder.putExtra("dateMonth", monthInt);
                editReminder.putExtra("dateDay", dayInt);
                editReminder.putExtra("dateHour", hourInt);
                editReminder.putExtra("dateMin", minInt);
               // editReminder.putExtra("id",currID );//keep this to edit reminder when we go back here



                Toast.makeText(MainActivity.this, "Year = " + yearInt + "\n" + "Month = " + monthInt +"\n" + "Day = " + dayInt + "\n"
                + "Hour = " + hourInt + "\n" + "Minute = " + minInt + "\n" + "ID = " + mainList.get(position).getId(), Toast.LENGTH_LONG).show();//ID = POSITION + 1
                startActivity(editReminder);//start to activity to edit reminder
                //Toast.makeText(MainActivity.this, "" + mainList.get(position).getName() + ", " + mainList.get(position).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        updateColor();//update color from settings*/
        showAllData();//show data from db in listview
    }

    public void showAllData(){
        Cursor res = reminderDb.getAllData();
        if(res.getCount() == 0) {//if no data available
            Toast.makeText(MainActivity.this, "No Data to Show", Toast.LENGTH_LONG).show();
            return;
        }
       // Toast.makeText(MainActivity.this, "Data to Show", Toast.LENGTH_LONG).show();
        while(res.moveToNext()){
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

    public void addReminderToList(){//probably can get rid of this
        String newReminderName = getIntent().getStringExtra("name");
        String newReminderMessage = getIntent().getStringExtra("message");
        int newReminderYear = getIntent().getIntExtra("dateYear", 0);
        int newReminderMonth = getIntent().getIntExtra("dateMonth", 0);
        int newReminderDay = getIntent().getIntExtra("dateDay", 0);
        int newReminderHour = getIntent().getIntExtra("dateHour", 0);
        int newReminderMin = getIntent().getIntExtra("dateMin", 0);
        int ReminderPosition = getIntent().getIntExtra("position", -1);

        String newReminderDate = newReminderMonth + "/" + newReminderDay + "/" + newReminderYear;
        String newReminderTime = newReminderHour + ":" + newReminderMin;

        if(newReminderYear == 0){
            return;
        }

        //GregorianCalendar newReminderDate = new GregorianCalendar(newReminderYear, newReminderMonth, newReminderDay, newReminderHour, newReminderMin);
       // Reminder newReminder = new Reminder(newReminderName, newReminderMessage, newReminderDate);

        /*boolean isInserted = reminderDb.insertData(newReminderName, newReminderMessage, newReminderDate, newReminderTime);
        if(isInserted == true)
            Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(MainActivity.this, "Data Not Inserted", Toast.LENGTH_LONG).show();*/

        /*if(ReminderPosition == -1) {//meaning if you are adding a new reminder, not editing an existing one
            mainList.add(newReminder);
            nameList.add(newReminderName);
        } else {
            mainList.set(ReminderPosition, newReminder);//VERY SUSPECT
            nameList.set(ReminderPosition, newReminder.getName());
        }*/

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
