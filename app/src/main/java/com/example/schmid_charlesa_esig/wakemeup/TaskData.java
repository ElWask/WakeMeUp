package com.example.schmid_charlesa_esig.wakemeup;

/**
 * Created by SCHMID_CHARLESA-ESIG on 06.03.2017.
 */

public class TaskData {
    //private variables
    int id;
    String title;
    String desc;
    int date;
    int hour;
    int minute;
    // empty constructor
    public TaskData(){
    }

    // constructor
    public TaskData(int id, String name, String desc, int date, int hour, int minute){
        this.id = id;
        this.title = name;
        this.desc = desc;
        this.date = date;
        this.hour = hour;
        this.minute = minute;

    }

    // getting ID
    public int getID(){
        return this.id;
    }

    // setting id
    public void setID(int id){
        this.id = id;
    }

    // getting name
    public String getName(){
        return this.title;
    }

    // setting name
    public void setName(String name){
        this.title = name;
    }

    // getting desc
    public String getDesc(){
        return this.desc;
    }

    // setting desc
    public void setDesc(String desc){
        this.desc = desc;
    }

    public int getDate() {
        return this.date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
    @Override
    public String toString() {
        return "TaskInfo [name=" + title + ", desc=" + desc + ", Date= "+ date + ", hours= " +hour + ", minute=" + minute+"]";
    }

}
