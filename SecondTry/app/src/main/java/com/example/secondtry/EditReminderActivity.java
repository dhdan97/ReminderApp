package com.example.secondtry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.secondtry.MainActivity.reminderDb;

public class EditReminderActivity extends AppCompatActivity {

    //int ReminderPosition = getIntent().getIntExtra("position", -1);//keep an eye out in case this overrwrites the first element in mainList?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);

        //gets data from incoming Intent
        String reminderNameString = getIntent().getStringExtra("reminder_name");
        String reminderMessageString = getIntent().getStringExtra("reminder_message");
        int ReminderYear = getIntent().getIntExtra("dateYear", 0);
        int ReminderMonth = getIntent().getIntExtra("dateMonth", 0);
        int ReminderDay = getIntent().getIntExtra("dateDay", 0);
        int ReminderHour = getIntent().getIntExtra("dateHour", 0);
        int ReminderMin = getIntent().getIntExtra("dateMin", 0);

        //set textfields to data we got from the incoming Intent
        EditText oldReminderName = (EditText) findViewById(R.id.titleet);
        oldReminderName.setText(reminderNameString);
        EditText oldReminderMessage = (EditText) findViewById(R.id.messageet);
        oldReminderMessage.setText(reminderMessageString);
        EditText oldReminderYear = (EditText) findViewById(R.id.dateyearet);
        oldReminderYear.setText(String.valueOf(ReminderYear));
        EditText oldReminderMonth = (EditText) findViewById(R.id.datemonthet);
        oldReminderMonth.setText(String.valueOf(ReminderMonth));
        EditText oldReminderDay = (EditText) findViewById(R.id.datedayet);
        oldReminderDay.setText(String.valueOf(ReminderDay));
        EditText oldReminderHour = (EditText) findViewById(R.id.datehouret);
        oldReminderHour.setText(String.valueOf(ReminderHour));
        EditText oldReminderMin = (EditText) findViewById(R.id.dateminuteet);
        oldReminderMin.setText(String.valueOf(ReminderMin));
    }

    public void editReminder(View view){//event when edit button is clicked

        //gets data from textfields
        EditText nameInput = (EditText) findViewById(R.id.titleet);
        EditText messageInput = (EditText) findViewById(R.id.messageet);
        EditText yearInput = (EditText) findViewById(R.id.dateyearet);
        EditText monthInput = (EditText) findViewById(R.id.datemonthet);
        EditText dayInput = (EditText) findViewById(R.id.datedayet);
        EditText hourInput = (EditText) findViewById(R.id.datehouret);
        EditText minInput = (EditText) findViewById(R.id.dateminuteet);
        int ReminderID = getIntent().getIntExtra("id", 0);
        Toast.makeText(this, String.valueOf(ReminderID), Toast.LENGTH_SHORT).show();


        String nameString = nameInput.getText().toString();
        String messageString = messageInput.getText().toString();
        String yearString = yearInput.getText().toString();
        String monthString = monthInput.getText().toString();
        String dayString = dayInput.getText().toString();
        String hourString = hourInput.getText().toString();
        String minString = minInput.getText().toString();

        if (nameString.matches("") || messageString.matches("") || yearString.matches("") ||
                monthString.matches("") || dayString.matches("") || hourString.matches("") || minString.matches("")) {
            Toast.makeText(this, "Please correctly fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int yearInt = Integer.parseInt(yearString);
        int monthInt = Integer.parseInt(monthString);
        int dayInt = Integer.parseInt(dayString);
        int hourInt = Integer.parseInt(hourString);
        int minInt = Integer.parseInt(minString);

        if(yearInt > 3000 || monthInt > 12 || dayInt > 31 || hourInt > 24 || minInt > 60){
            Toast.makeText(this, "Please correctly fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String dateString = monthString + "/" + dayString + "/" + yearString;
        String timeString = hourString + ":" + minString;

        boolean isUpdate = reminderDb.updateData(String.valueOf(ReminderID), nameString, messageString, dateString, timeString);
        if(isUpdate == true)
            Toast.makeText(this, "Sucessfully updated Reminder #" + ReminderID, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "failed to update", Toast.LENGTH_SHORT).show();


        Intent addReminder = new Intent(this, MainActivity.class);
        /*addReminder.putExtra("name", nameString);
        addReminder.putExtra("message", messageString);
        addReminder.putExtra("dateYear", yearInt);
        addReminder.putExtra("dateMonth", monthInt);
        addReminder.putExtra("dateDay", dayInt);
        addReminder.putExtra("dateHour", hourInt);
        addReminder.putExtra("dateMin", minInt);
        addReminder.putExtra("position", ReminderPosition);*/

        //sends data AND position of the reminder we edited to mainActivity
        startActivity(addReminder);
    }
}
