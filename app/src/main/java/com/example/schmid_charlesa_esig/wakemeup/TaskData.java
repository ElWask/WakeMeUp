package com.example.schmid_charlesa_esig.wakemeup;

/**
 * Created by SCHMID_CHARLESA-ESIG on 06.03.2017.
 */

public class TaskData {
    //private variables
    int id;
    String title;
    String desc;
    int year;
    int month;
    int day;
    int hour;
    int minute;
    // empty constructor
    public TaskData(){
    }

    // constructor
    public TaskData(int id, String name, String desc, int year, int month, int day, int hour, int minute){
        this.id = id;
        this.title = name;
        this.desc = desc;
        this.year = year;
        this.month = month;
        this.day = day;
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

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    public int getMonth() {
        return this.month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
    public int getDay() {
        return this.day;
    }

    public void setDay(int day) {
        this.day = day;
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
        return "TaskInfo [name=" + title + ", desc=" + desc + ", Date= "+ day + "/" + month + "/" + year + ", hours= " +hour + ", minute=" + minute+"]";
    }

}
