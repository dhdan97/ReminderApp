package com.example.secondtry;

import java.util.Date;

public class Reminder {
    private String name;
    private  String message;
    private Date remindDate;

    public Reminder(){
        this("", "");
    }

    public Reminder(String name, String message){
        this("name", "message", new Date());
    }

    public Reminder(String name, String message, Date remindDate){
        this.name = name;
        this.message = message;
        this.remindDate = remindDate;
    }

    public String getName(){
        return name;
    }

    public String getMessage(){
        return message;
    }

    public Date getRemindDate(){
        return remindDate;
    }
}
