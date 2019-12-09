package com.example.secondtry;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

import static com.example.secondtry.MainActivity.reminderDb;

public class NewReminderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);
    }

    public void saveReminder(View view){//action that is taken when save button is clicked
        //take all current strings in textfields
        EditText nameInput = (EditText) findViewById(R.id.titleet);
        EditText messageInput = (EditText) findViewById(R.id.messageet);
        EditText yearInput = (EditText) findViewById(R.id.dateyearet);
        EditText monthInput = (EditText) findViewById(R.id.datemonthet);
        EditText dayInput = (EditText) findViewById(R.id.datedayet);
        EditText hourInput = (EditText) findViewById(R.id.datehouret);
        EditText minInput = (EditText) findViewById(R.id.dateminuteet);

        //convert textfields to strings
        String nameString = nameInput.getText().toString();
        String messageString = messageInput.getText().toString();
        String yearString = yearInput.getText().toString();
        String monthString = monthInput.getText().toString();
        String dayString = dayInput.getText().toString();
        String hourString = hourInput.getText().toString();
        String minString = minInput.getText().toString();

        //check if strings are valid
        if (nameString.matches("") || messageString.matches("") || yearString.matches("") ||
                monthString.matches("") || dayString.matches("") || hourString.matches("") || minString.matches("")) {
            Toast.makeText(this, "Please correctly fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //turn strings from date into ints
        int yearInt = Integer.parseInt(yearString);
        int monthInt = Integer.parseInt(monthString);
        int dayInt = Integer.parseInt(dayString);
        int hourInt = Integer.parseInt(hourString);
        int minInt = Integer.parseInt(minString);

        //check if date is valid
        if(yearInt > 3000 || monthInt > 12 || dayInt > 31 || hourInt > 24 || minInt > 60){
            Toast.makeText(this, "Please correctly fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //setAlarm(yearInt, monthInt, dayInt, hourInt, minInt, nameString, messageString);
        //set Alarm

        String newReminderDate = monthInt + "/" + dayInt + "/" + yearInt;
        String newReminderTime = hourInt + ":" + minInt;

        boolean isInserted = reminderDb.insertData(nameString, messageString, newReminderDate, newReminderTime);
        if(isInserted == true)
            Toast.makeText(NewReminderActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(NewReminderActivity.this, "Data Not Inserted", Toast.LENGTH_LONG).show();

        //send off Intent to MainActivity with all the data needed for a new Reminder to be added
        Intent addReminder = new Intent(this, MainActivity.class);
        addReminder.putExtra("name", nameString);
        addReminder.putExtra("message", messageString);
        addReminder.putExtra("dateYear", yearInt);
        addReminder.putExtra("dateMonth", monthInt);
        addReminder.putExtra("dateDay", dayInt);
        addReminder.putExtra("dateHour", hourInt);
        addReminder.putExtra("dateMin", minInt);
        addReminder.putExtra("position", -1);

        startActivity(addReminder);
    }

    public void setAlarm(int year, int month, int day, int hourOfDay, int minute, String name, String message){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        //updateTimeText(c);
        startAlarm(c, name, message);

    }

    private void updateTimeText(Calendar c){
        String timeText = "Alarm set for: ";
        timeText += DateFormat.getTimeInstance(DateFormat.LONG).format(c);//SHORT??

        //mTextView.setText(timeText);
    }

    private void startAlarm(Calendar c, String name, String message){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("name", name);
        intent.putExtra("message", message);

        Calendar z = Calendar.getInstance();
        z.add(Calendar.SECOND, 5);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, z.getTimeInMillis(), pendingIntent);
    }
}
