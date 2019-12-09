package com.example.secondtry;


public class Reminder {
    private String name;
    private  String message;
    private String date;
    private String time;
    private int id;

    public Reminder(){
        this("name", "message");
    }

    public Reminder(String name, String message){
        this(name, message, "date", "time");
    }

    public Reminder(String name, String message, String date) {this(name, message, date, "time");}

    public Reminder(String name, String message, String date, String time){
        this.name = name;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public int getId() { return id; }

    public String getName(){
        return name;
    }

    public String getMessage(){
        return message;
    }

    public String getDate(){
        return date;
    }

    public String getTime() { return time; }

    public void setId(int id) { this.id = id; }

    public void setName(String name){
        this.name = name;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String toString(){
        return name + ", " + message + "\n"
                + date + ", " + time;
    }
}
